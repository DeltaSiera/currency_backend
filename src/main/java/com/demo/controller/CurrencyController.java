package com.demo.controller;

import com.demo.model.CurrencyEntity;
import com.demo.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/currencies")
public class CurrencyController {
    private final CurrencyService currencyService;

    @Autowired
    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping("/newest")
    public List<CurrencyEntity> getNewestCurrenciesRecord() {
        return currencyService.getCurrencyRecords();
    }

    @GetMapping("/history/{name}")
    public List<CurrencyEntity> getCurrencyHistory(@PathVariable String name) {
        return currencyService.getCurrencyHistoryData(name);
    }

    @GetMapping("/{name}")
    public CurrencyEntity getCurrencyInformation(@PathVariable String name) {
        return currencyService.getCurrencyByName(name);
    }

    @GetMapping("/countries")
    public List<String> getCountries() {
        return currencyService.getCountriesNames();
    }
}
