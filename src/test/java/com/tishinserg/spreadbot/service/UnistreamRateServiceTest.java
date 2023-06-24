package com.tishinserg.spreadbot.service;

import com.tishinserg.spreadbot.converters.UnistreamConverter;
import com.tishinserg.spreadbot.parsing.UnistreamRateParsingService;
import com.tishinserg.spreadbot.repository.UnistreamRateRepository;
import com.tishinserg.spreadbot.repository.entity.UnistreamRate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit-level testing for UnistreamRateServiceImpl")
public class UnistreamRateServiceTest {

    @InjectMocks
    private UnistreamRateServiceImpl unistreamRateService;

    @Mock
    private UnistreamRateRepository unistreamRateRepository;

    @Mock
    private UnistreamRateParsingService unistreamRateParsingService;

    @Mock
    private UnistreamConverter converter;

    //todo переписать
    @Test
    void shouldProperlyCompareUnistreamRate() {
        //given

        UnistreamRate unistreamRate1 = new UnistreamRate();
        unistreamRate1.setRate(BigDecimal.valueOf(77.00));

        UnistreamRate unistreamRate2 = new UnistreamRate();
        unistreamRate2.setRate(BigDecimal.valueOf(77.01));

        UnistreamRate unistreamRate3 = new UnistreamRate();
        unistreamRate3.setRate(BigDecimal.valueOf(77.21));

        //when
//        Boolean isDifferentFirstAndSecondUniRate = unistreamRateService.compareUnistreamRates(unistreamRate1, unistreamRate2);
//        Boolean isDifferentFirstAndThirdUniRate = unistreamRateService.compareUnistreamRates(unistreamRate1, unistreamRate3);
//
//        //then
//        assertThat(isDifferentFirstAndSecondUniRate).isFalse();
//        assertThat(isDifferentFirstAndThirdUniRate).isTrue();
    }

    @Test
    void shouldProperlySave() {
        //given
        UnistreamRate givenUnistreamRate = new UnistreamRate();
        givenUnistreamRate.setCountry("GEO");
        givenUnistreamRate.setCurrencyFrom("USD");
        givenUnistreamRate.setDate(LocalDateTime.now());
        givenUnistreamRate.setRate(BigDecimal.valueOf(77.00));

        //when
        unistreamRateService.save(givenUnistreamRate);

        //then
        verify(unistreamRateRepository).save(givenUnistreamRate);
    }

    /*
    @Test
    void shouldProperlyGetCurrentRate() {
        //given
        UnistreamRate givenUnistreamRate = new UnistreamRate();
        givenUnistreamRate.setRate(BigDecimal.valueOf(2.01));
        givenUnistreamRate.setCurrencyFrom("USD");
        givenUnistreamRate.setCountry("GEO");

        UnistreamAnswerDetailedJson json = new UnistreamAnswerDetailedJson();
        json.setWithdrawCurrency("USD");
        json.setWithdrawAmount(1.0);
        json.setAcceptedAmount(2.01);
        UnistreamAnswerModel unistreamAnswerModel = Mockito.mock(UnistreamAnswerModel.class);
        when(unistreamRateParsingService.getRate("GEO", "USD", "RUB")).thenReturn(unistreamAnswerModel);
        when(unistreamAnswerModel.getFees()).thenReturn(Collections.singletonList(json));

        //when
        UnistreamRate unistreamRate = unistreamRateService.getCurrentRate("GEO", "USD", "RUB");

        //then
        verify(unistreamRateParsingService).getRate("GEO", "USD", "RUB");
        assertThat(unistreamRate.getCountry()).isEqualTo(givenUnistreamRate.getCountry());
        assertThat(unistreamRate.getCurrencyFrom()).isEqualTo(givenUnistreamRate.getCurrencyFrom());
        assertThat(unistreamRate.getRate()).isEqualTo(givenUnistreamRate.getRate());
    }
     */

}
