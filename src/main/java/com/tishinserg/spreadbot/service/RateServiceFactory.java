package com.tishinserg.spreadbot.service;

import org.springframework.stereotype.Service;

@Service
public class RateServiceFactory {
    private final UnistreamRateService unistreamRateService;
    private final KoronaPayRateService koronaPayRateService;
    private final BinanceRateService binanceRateService;


    public RateServiceFactory(UnistreamRateService unistreamRateService, KoronaPayRateService koronaPayRateService, BinanceRateService binanceRateService) {
        this.unistreamRateService = unistreamRateService;
        this.koronaPayRateService = koronaPayRateService;
        this.binanceRateService = binanceRateService;
    }

    public RateService createInstance(String service) {
        switch (service) {
            case "unistream":
                return unistreamRateService;
            case "koronapay":
                return koronaPayRateService;
            case "binance":
                return binanceRateService;
            default:
                throw new IllegalArgumentException("Unknown service value: " + service);
        }
    }
}
