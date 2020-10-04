package com.demo.service;

import com.demo.CurrencyName;
import com.demo.model.CurrencyEntity;
import com.demo.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CurrencyService {
    private final CurrencyRepository currencyRepository;

    @Autowired
    public CurrencyService(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    public List<CurrencyEntity> getCurrencyRecords() {
        Map<String, CurrencyEntity> collect = currencyRepository.findAll()
                                                                .stream()
                                                                .collect(Collectors.toMap(CurrencyEntity::getName, Function.identity(), BinaryOperator.maxBy(Comparator.comparing(CurrencyEntity::getDate))));

        return new ArrayList<>(collect.values());
    }

    public List<CurrencyEntity> getCurrencyHistoryData(String currencyName) {
        String shortName = CurrencyName.getShortName(currencyName);
        return currencyRepository.findAllByName(shortName);
    }

    public CurrencyEntity getCurrencyByName(String name) {
        return currencyRepository.findAllByName(name)
                                 .stream()
                                 .max(Comparator.comparing(CurrencyEntity::getDate))
                                 .get();
    }

    public List<String> getCountriesNames() {
        return getCurrencyRecords().stream()
                                   .map(CurrencyEntity::getName)
                                   .collect(Collectors.toList());
    }
}
