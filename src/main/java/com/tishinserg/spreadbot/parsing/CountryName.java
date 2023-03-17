package com.tishinserg.spreadbot.parsing;

public enum CountryName {

    GEORGIA("GEO");

    private final String countryName;

    CountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryName() {
        return countryName;
    }
}
