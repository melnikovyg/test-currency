package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;
import services.CurrencyService;
import to.CurrencyCursDynamic;
import to.CurrencyCursOnDay;
import to.CurrencyEnum;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "/rest", produces="application/json;charset=UTF-8")
public class CurrencyRestController {

    @Autowired
    CurrencyService service;

    @GetMapping(value = "/enum")
    public List<CurrencyEnum> getEnumCurrency() throws ParserConfigurationException, SAXException, IOException {
        return service.getEnumCurrency();
    }

    @GetMapping(value = "/day")
    public List<CurrencyCursOnDay> getCursOnDay(@RequestParam(value = "date") String date) throws IOException {
        return service.getCursOnDate(LocalDate.parse(date));
    }

    @GetMapping(value = "/dynamic")
    public List<CurrencyCursDynamic> getCursDynamicXML(@RequestParam(value = "dateFrom") String dateFrom, @RequestParam(value = "dateTo") String dateTo,@RequestParam(value = "vCode") String vCode) throws IOException {
        return service.getCursDynamic(LocalDate.parse(dateFrom), LocalDate.parse(dateTo), vCode);
    }

}
