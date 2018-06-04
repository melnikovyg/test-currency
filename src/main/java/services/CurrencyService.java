package services;

import org.xml.sax.SAXException;
import to.BaseTo;
import to.CurrencyCursDynamic;
import to.CurrencyEnum;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface CurrencyService {

    void updateCurrencyTask();

    List<CurrencyEnum> getEnumCurrency() throws IOException, ParserConfigurationException, SAXException;

    List<CurrencyCursDynamic> getCursDynamic(LocalDate dateFrom, LocalDate dateTo, String currencyCode) throws IOException;

    <T extends BaseTo> List<T> getCursOnDate(LocalDate date) throws IOException;
 }
