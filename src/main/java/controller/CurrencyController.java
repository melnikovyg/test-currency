package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import services.CurrencyService;

@Controller
@RequestMapping(value = "/")
public class CurrencyController {

    @Autowired
    CurrencyService service;

    @GetMapping("/")
    public String rootMock() {
        service.updateCurrencyTask();
        return "redirect:/swagger-ui.html";
    }

}
