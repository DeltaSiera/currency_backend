package com.demo.repository;

import com.demo.model.CurrencyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

import java.util.List;
import java.util.Optional;

@Repository
public interface CurrencyRepository extends JpaRepository<CurrencyEntity, Integer> {
    Optional<CurrencyEntity> findByNameAndRatioAndDate(String currencyName, double ratio, LocalDate date);
    List<CurrencyEntity> findAllByName(String name);
}
