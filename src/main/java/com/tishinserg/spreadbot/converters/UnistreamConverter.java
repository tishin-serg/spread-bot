package com.tishinserg.spreadbot.converters;

import com.tishinserg.spreadbot.models.Rate;
import com.tishinserg.spreadbot.models.unistream.UnistreamAnswerDetailedJson;
import com.tishinserg.spreadbot.repository.entity.UnistreamRate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

// todo уточнить уместно ли использовать метод этого класса статическим
@Component
@RequiredArgsConstructor
public class UnistreamConverter {

    public static UnistreamRate modelToEntity(Rate rate, String countryName) {
        UnistreamRate unistreamRate = new UnistreamRate();
        unistreamRate.setCountry(countryName);
        unistreamRate.setCurrencyFrom(rate.getCurrencyFrom());
        unistreamRate.setCurrencyTo(rate.getCurrencyTo());
        unistreamRate.setRate(rate.getRate());
        unistreamRate.setDate(LocalDateTime.now());
        return unistreamRate;
    }

    public static Rate entityToModel(UnistreamRate unistreamRate) {
        return new Rate(unistreamRate.getCurrencyFrom(), unistreamRate.getCurrencyTo(), unistreamRate.getRate(), unistreamRate.getDate());
    }

    public static UnistreamRate jSonToEntity(UnistreamAnswerDetailedJson unistreamAnswerDetailedJson, String countryName) {
        UnistreamRate unistreamRate = new UnistreamRate();
        unistreamRate.setCountry(countryName);
        unistreamRate.setCurrencyFrom(unistreamAnswerDetailedJson.getWithdrawCurrency());
        BigDecimal rate = feesToRate(unistreamAnswerDetailedJson);
        unistreamRate.setRate(rate);
        unistreamRate.setDate(LocalDateTime.now());
        return unistreamRate;
    }

    public static BigDecimal feesToRate(UnistreamAnswerDetailedJson unistreamAnswerDetailedJson) {
        return BigDecimal.valueOf(unistreamAnswerDetailedJson.getAcceptedAmount())
                .divide(BigDecimal.valueOf(unistreamAnswerDetailedJson.getWithdrawAmount()), 2, RoundingMode.CEILING);
    }

}
