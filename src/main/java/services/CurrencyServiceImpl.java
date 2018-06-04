package services;

import model.Currency;
import model.DateLoaded;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import repository.CurrencyRepository;
import repository.DateRepository;
import to.BaseTo;
import to.CurrencyCursOnDay;
import to.CurrencyCursDynamic;
import to.CurrencyEnum;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

import static Utils.Checkers.CURRENCY_PROPERTIES;
import static Utils.Checkers.checkNonNull;

@EnableScheduling
@Service
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyRepository repository;
    private final DateRepository dateRepository;

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    public CurrencyServiceImpl(CurrencyRepository repository, DateRepository dateRepository) {
        this.repository = repository;
        this.dateRepository = dateRepository;
    }

    @Scheduled(cron = "0 0 10 * * *")
    public void updateCurrencyTask() {
        log.info("update curs on date {}", LocalDate.now());
        this.getCursOnDate(LocalDate.now());
    }

    @Override
    public List<CurrencyEnum> getEnumCurrency() {
        log.info("get enum currency");

        String xmlBody = "<EnumValutesXML xmlns=\"http://web.cbr.ru/\">\n" +
                "<Seld>false</Seld>\n" +
                "</EnumValutesXML>\n";

        String resultString = this.getResponseToCbr(xmlBody);

        return checkNonNull(this.sortList(this.parseXml(resultString, "EnumValutes", CurrencyEnum.class)),"result is empty");
    }

    @Override
    public List<CurrencyCursDynamic> getCursDynamic(LocalDate dateFrom, LocalDate dateTo, String currencyCode) {
        log.info("get dynamic curs from date {} to date {} for code {}", dateFrom, dateTo, currencyCode);

        String xmlBody = String.format("<GetCursDynamicXML xmlns=\"http://web.cbr.ru/\">\n" +
                        "      <FromDate>%s</FromDate>\n" +
                        "      <ToDate>%s</ToDate>\n" +
                        "      <ValutaCode>%s</ValutaCode>\n" +
                        "    </GetCursDynamicXML>"
                , dateFrom, dateTo, currencyCode);

        String resultString = this.getResponseToCbr(xmlBody);
        return checkNonNull(this.parseXml(resultString, "ValuteCursDynamic", CurrencyCursDynamic.class),"result is empty");
    }

    @Override
    public <T extends BaseTo> List<T> getCursOnDate(LocalDate date) {
        log.info("get curs on date {}", date);

        List<CurrencyCursOnDay> resultTo;

        if (dateRepository.findByDate(date) == null) {
            String xmlBody = "<GetCursOnDateXML xmlns=\"http://web.cbr.ru/\">\n" +
                    "<On_date>" + date + "</On_date>\n" +
                    "</GetCursOnDateXML>";

            String resultString = this.getResponseToCbr(xmlBody);
            resultTo = this.parseXml(resultString, "ValuteCursOnDate", CurrencyCursOnDay.class);
            this.saveRepository(date, resultTo);

             return checkNonNull((List<T>) resultTo,"result is empty");
        } else {
            List<Currency> result = repository.findByDate(date);
            return checkNonNull((List<T>) result,"result is empty");
        }

    }

    @Transactional
    void saveRepository(LocalDate date, List<CurrencyCursOnDay> resultTo) {
        log.info("save new data to repositories for date {}", date);

        DateLoaded dateLoaded = new DateLoaded(date);
        dateRepository.save(dateLoaded);
        resultTo.forEach(c -> repository.save(new Currency(c.getvCode(), dateLoaded, new BigDecimal(c.getvCurs()), c.getvName(), c.getvNom(), c.getvChCode())));

    }

    private String getResponseToCbr(String soapBody) {

        String CBR_URL = "http://www.cbr.ru/DailyInfoWebServ/DailyInfo.asmx";
        HttpPost post = new HttpPost(CBR_URL);
        StringEntity xmlBody = null;
        try {
            xmlBody = new StringEntity("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                    "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                    "  <soap:Body>\n" +
                    soapBody +
                    "  </soap:Body>\n" +
                    "</soap:Envelope>");
            post.setEntity(xmlBody);
            post.setHeader("Content-type", "text/xml");
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpResponse responsePOST = httpClient.execute(post);
            return EntityUtils.toString(responsePOST.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private <T extends BaseTo> List<T> parseXml(String resultForParse, String rootElement, Class<T> classForParse) {

        List<T> resultList = new ArrayList<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource inputSource = new InputSource(new StringReader(resultForParse));
            Document doc = builder.parse(inputSource);
            NodeList list = doc.getElementsByTagName(rootElement);
            Field[] fields = classForParse.getDeclaredFields();
            Constructor constructor = Class.forName(classForParse.getName()).getConstructor();

            for (int i = 0; i < list.getLength(); i++) {
                NodeList nodes = list.item(i).getChildNodes();

                Object newObject = constructor.newInstance();

                for (int j = 0; j < fields.length; j++) {
                    Field field = fields[j];
                    Node childNode = nodes.item(j);
                    if (childNode != null) {
                        field.setAccessible(true);
                        field.set(newObject, childNode.getTextContent().trim());
                    }
                }
                resultList.add((T) newObject);
            }
            return resultList;
        } catch (IOException | IllegalAccessException | ClassNotFoundException | ParserConfigurationException | SAXException | InvocationTargetException | InstantiationException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private <T extends BaseTo> List<T> sortList(List<T> listCurrency) {

        try {
            Path file = Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource(CURRENCY_PROPERTIES)).toURI());

            listCurrency.sort((o1, o2) -> {
                String str1 = Objects.isNull(o1.getVCharCode()) ? "" : o1.getVCharCode();
                String str2 = Objects.isNull(o2.getVCharCode()) ? "" : o2.getVCharCode();
                return str1.compareTo(str2);
            });

            String str = Files.readAllLines(file).toString();
            List<String> listSort = Arrays.asList(str.substring(1, str.length() - 1).split(","));
            List<T> listSortedCurrency = new ArrayList<>();
            for (int i = 0; i < listSort.size(); i++) {
                listSortedCurrency.add(null);
            }

            for (int i = 0; i < listCurrency.size(); i++) {
                T element = listCurrency.get(i);
                String elementVCharCode = element.getVCharCode();
                if (listSort.contains(elementVCharCode)) {
                    listSortedCurrency.set(listSort.indexOf(elementVCharCode), element);
                    listCurrency.remove(element);
                }
            }
            for (int i = 0; i < listSortedCurrency.size(); i++) {
                T element = listSortedCurrency.get(i);
                if (element == null) {
                    listSortedCurrency.remove(i);
                    i--;
                }
            }
            listCurrency.addAll(0, listSortedCurrency);

        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }

        return listCurrency;
    }

}
