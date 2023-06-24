package com.tishinserg.spreadbot;

import com.tishinserg.spreadbot.converters.UnistreamConverter;
import com.tishinserg.spreadbot.models.unistream.UnistreamAnswerDetailedJson;
import com.tishinserg.spreadbot.repository.entity.UnistreamRate;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UnistreamConverterTest {


    @Test
    void shouldProperlyConvertJsonToEntity() {
        // given
        UnistreamAnswerDetailedJson inputJson = new UnistreamAnswerDetailedJson();
        inputJson.setWithdrawCurrency("USD");
        inputJson.setAcceptedAmount(100.00);
        inputJson.setWithdrawAmount(20.00);

        // when
        UnistreamRate outputRate = UnistreamConverter.jSonToEntity(inputJson, "GEO");

        // then
        assertEquals("GEO", outputRate.getCountry());
        assertEquals("USD", outputRate.getCurrencyFrom());
        assertEquals(new BigDecimal("5.00"), outputRate.getRate());
        assertNotNull(outputRate.getDate());
    }
}
