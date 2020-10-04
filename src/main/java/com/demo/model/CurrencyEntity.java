package com.demo.model;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    @NotNull
    private String name;
    @Column
    private double ratio;
    @Column
    @NotNull
    private LocalDate date;

    public CurrencyEntity(String currencyName, double currencyCourse, LocalDate dateWhenWasTaken) {
        this.name = currencyName;
        this.ratio = currencyCourse;
        this.date = dateWhenWasTaken;
    }
}
