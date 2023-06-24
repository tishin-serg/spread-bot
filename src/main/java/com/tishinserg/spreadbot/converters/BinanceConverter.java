package com.tishinserg.spreadbot.converters;

import com.tishinserg.spreadbot.models.Rate;
import com.tishinserg.spreadbot.repository.entity.BinanceRate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BinanceConverter {
    public static BinanceRate modelToEntity(Rate rate, String payMethod, String tradeType) {
        BinanceRate binanceRate = new BinanceRate();
        binanceRate.setRate(rate.getRate());
        binanceRate.setDate(rate.getDate());
        binanceRate.setCurrencyFrom(rate.getCurrencyFrom());
        binanceRate.setCurrencyTo(rate.getCurrencyTo());
        binanceRate.setPaymentMethod(payMethod);
        binanceRate.setTradeType(tradeType);
        return binanceRate;
    }
}
