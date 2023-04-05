package com.tishinserg.spreadbot.converters;

import com.tishinserg.spreadbot.models.UnistreamAnswerDetailedJson;
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


    public static UnistreamRate jSonToEntity(UnistreamAnswerDetailedJson unistreamAnswerDetailedJson, String countryName) {
        UnistreamRate unistreamRate = new UnistreamRate();
        unistreamRate.setCountry(countryName);
        unistreamRate.setCurrency(unistreamAnswerDetailedJson.getWithdrawCurrency());
        BigDecimal rate = BigDecimal.valueOf(unistreamAnswerDetailedJson.getAcceptedAmount())
                .divide(BigDecimal.valueOf(unistreamAnswerDetailedJson.getWithdrawAmount()), 2, RoundingMode.CEILING);
        unistreamRate.setRate(rate);
        unistreamRate.setDate(LocalDateTime.now());
        return unistreamRate;
    }

}
