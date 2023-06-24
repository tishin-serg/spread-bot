package com.tishinserg.spreadbot.service;

import com.tishinserg.spreadbot.repository.entity.BinanceRate;

public interface BinanceRateService extends RateService {
    void save(BinanceRate binanceRate);

    BinanceRate getCurrentRate(String payMethod, String currencyFrom, String currencyTo, String tradeType);

    BinanceRate getLastRate(String payMethod, String currencyFrom, String currencyTo, String tradeType);
}
