package com.tishinserg.spreadbot.converters;

import com.tishinserg.spreadbot.models.UnistreamAnswerDetailedJson;
import com.tishinserg.spreadbot.repository.entity.UnistreamRate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

// todo уточнить уместно ли использовать метод этого класса статическим
@Component
@RequiredArgsConstructor
public class UnistreamConverter {

    public static UnistreamRate jSonToEntity(UnistreamAnswerDetailedJson unistreamAnswerDetailedJson) {
        UnistreamRate unistreamRate = new UnistreamRate();
        unistreamRate.setCurrency(unistreamAnswerDetailedJson.getAcceptedCurrency());
        Double rate = unistreamAnswerDetailedJson.getAcceptedAmount() / unistreamAnswerDetailedJson.getWithdrawAmount();
        unistreamRate.setRate(rate);
        unistreamRate.setDate(LocalDateTime.now());
        return unistreamRate;
    }

}
