package com.demo;

import java.util.Arrays;

public enum CurrencyName {
    AUD("AUD", "Australijos doleris"),
    CZK("CZK", "Čekijos krona"),
    DKK("CZK", "Danijos krona"),
    GBP("GBP", "Didžiosios Britanijos svaras"),
    JPY("USD", "Japonijos juanis"),
    NOK("USD", "Norvegijos krona"),
    PLN("USD", "Lenkijos zlotas"),
    RUB("USD", "Rusijos rublis"),
    SEK("USD", "Švedijos krona"),
    USD("USD", "JAV doleris");

    private final String shortName;
    private final String longName;

    CurrencyName(String shortName, String longName) {
        this.shortName = shortName;
        this.longName = longName;
    }

    public static String getLongName(String shortName) {
        return Arrays.stream(CurrencyName.values())
                     .filter(name -> name.shortName.equals(shortName))
                     .findFirst()
                     .get().longName;
    }

    public static String getShortName(String longName) {
        return Arrays.stream(CurrencyName.values())
                     .filter(name -> name.longName.equals(longName))
                     .findFirst()
                     .get().shortName;
    }
}
