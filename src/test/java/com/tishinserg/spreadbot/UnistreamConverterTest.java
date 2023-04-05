package com.tishinserg.spreadbot;

import com.tishinserg.spreadbot.converters.UnistreamConverter;
import com.tishinserg.spreadbot.models.UnistreamAnswerDetailedJson;
import com.tishinserg.spreadbot.repository.entity.UnistreamRate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class UnistreamConverterTest {

    private UnistreamConverter unistreamConverter;

    @Test
    void shouldProperlyConvertJsonToEntity() {
        //given
        UnistreamRate givenEntity = new UnistreamRate();
        givenEntity.setCurrency("USD");
        givenEntity.setCountry("GEO");
        givenEntity.setRate(BigDecimal.valueOf(43.48));
        givenEntity.setDate(LocalDateTime.now());

        UnistreamAnswerDetailedJson givenJson = new UnistreamAnswerDetailedJson();
        givenJson.setWithdrawCurrency("USD");
        givenJson.setAcceptedAmount(1000.0);
        givenJson.setWithdrawAmount(23.0);

        //when
        UnistreamRate converted = UnistreamConverter.jSonToEntity(givenJson, "GEO");

        //then
        Assertions.assertEquals(givenEntity.getRate(), converted.getRate());
        Assertions.assertEquals(givenEntity.getCurrency(), converted.getCurrency());
    }
}
