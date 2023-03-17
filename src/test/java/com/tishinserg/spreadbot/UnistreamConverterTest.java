package com.tishinserg.spreadbot;

import com.tishinserg.spreadbot.converters.UnistreamConverter;
import com.tishinserg.spreadbot.models.UnistreamAnswerDetailedJson;
import com.tishinserg.spreadbot.repository.entity.UnistreamRate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class UnistreamConverterTest {

    private UnistreamConverter unistreamConverter;

    @Test
    void shouldProperlyConvertJsonToEntity() {
        //given
        UnistreamRate givenEntity = new UnistreamRate();
        givenEntity.setCurrency("RUB");
        givenEntity.setRate(50.0);
        givenEntity.setDate(LocalDateTime.now());

        UnistreamAnswerDetailedJson givenJson = new UnistreamAnswerDetailedJson();
        givenJson.setAcceptedCurrency("RUB");
        givenJson.setAcceptedAmount(1000.0);
        givenJson.setWithdrawAmount(20.0);

        //when
        UnistreamRate converted = UnistreamConverter.jSonToEntity(givenJson);

        //then
        Assertions.assertEquals(givenEntity.getRate(), converted.getRate());
        Assertions.assertEquals(givenEntity.getCurrency(), converted.getCurrency());



    }
}
