package com.tishinserg.spreadbot.parsing;


public enum CurrencyName {

    US_DOLLAR("USD"),
    RUBLE("RUB");

    private final String currencyName;

    CurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getCurrencyName() {
        return currencyName;
    }
}
