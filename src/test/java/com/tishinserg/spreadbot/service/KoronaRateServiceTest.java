package com.tishinserg.spreadbot.service;

import com.tishinserg.spreadbot.models.Rate;
import com.tishinserg.spreadbot.models.RequestDetails;
import com.tishinserg.spreadbot.parsing.KoronaRateParsingService;
import com.tishinserg.spreadbot.repository.KoronaRateRepository;
import com.tishinserg.spreadbot.repository.entity.GroupSub;
import com.tishinserg.spreadbot.repository.entity.KoronaRate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class KoronaRateServiceTest {

    @InjectMocks
    private KoronaRateServiceImpl koronaRateService;

    @Mock
    private KoronaRateRepository rateRepository;

    @Mock
    private KoronaRateParsingService parsingService;

    @Test
    void shouldReturnsTrueWhenLastRateIsNullInCompareRates() {
        // given
        Rate lastRate = null;
        Rate currentRate = new Rate();
        currentRate.setRate(BigDecimal.valueOf(10.0));

        // when
        Boolean result = koronaRateService.compareRates(lastRate, currentRate);

        // then
        assertTrue(result);
    }

    @Test
    void shouldReturnsTrueWhenRateDifferenceIsGreaterThanOrEqualToThresholdInCompareRates() {
        // given
        Rate lastRate = new Rate();
        lastRate.setRate(BigDecimal.valueOf(10.0));
        Rate currentRate = new Rate();
        currentRate.setRate(BigDecimal.valueOf(10.06));

        // when
        Boolean result = koronaRateService.compareRates(lastRate, currentRate);

        // then
        assertTrue(result);
    }

    @Test
    void shouldReturnsFalseWhenRateDifferenceIsLessThanThresholdInCompareRates() {
        // given
        Rate lastRate = new Rate();
        lastRate.setRate(BigDecimal.valueOf(10.0));
        Rate currentRate = new Rate();
        currentRate.setRate(BigDecimal.valueOf(10.04));

        // when
        Boolean result = koronaRateService.compareRates(lastRate, currentRate);

        // then
        assertFalse(result);
    }

    @Test
    void shouldProperlyGetCurrentRate() {
        // given
        RequestDetails requestDetails = new RequestDetails("USA");
        GroupSub groupSub = new GroupSub();
        groupSub.setRequestDetails(requestDetails);
        groupSub.setCurrencyFrom("USD");
        groupSub.setCurrencyTo("EUR");

        Rate expectedRate = new Rate();
        expectedRate.setRate(BigDecimal.valueOf(1.2));
        expectedRate.setCurrencyFrom("USD");
        expectedRate.setCurrencyTo("EUR");
        expectedRate.setDate(LocalDateTime.now());

        when(parsingService.getRate(anyString(), anyString(), anyString()))
                .thenReturn(Mono.just(expectedRate));

        // when
        CompletableFuture<Rate> futureResult = koronaRateService.getCurrentRate(groupSub);
        Mono<Rate> result = Mono.fromCompletionStage(futureResult);


        // then
        StepVerifier.create(result)
                .expectNext(expectedRate)
                .expectComplete()
                .verify();

        verify(parsingService).getRate("USA", "USD", "EUR");
        verifyNoMoreInteractions(parsingService);
    }

    @Test
    void shouldReturnsOptionalEmptyWhenRateRepositoryReturnsEmpty() {
        // given
        RequestDetails requestDetails = new RequestDetails("USA");
        GroupSub groupSub = new GroupSub();
        groupSub.setRequestDetails(requestDetails);
        groupSub.setCurrencyFrom("USD");
        groupSub.setCurrencyTo("EUR");

        when(rateRepository.findLastRateCurrency("USA", "USD", "EUR")).thenReturn(Optional.empty());

        // when
        Optional<Rate> result = koronaRateService.getLastRate(groupSub);

        // then
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(rateRepository).findLastRateCurrency("USA", "USD", "EUR");
        verifyNoMoreInteractions(rateRepository);
    }

    @Test
    void shouldProperlyGetLastRate() {
        // given
        RequestDetails requestDetails = new RequestDetails("USA");
        GroupSub groupSub = new GroupSub();
        groupSub.setRequestDetails(requestDetails);
        groupSub.setCurrencyFrom("USD");
        groupSub.setCurrencyTo("EUR");

        KoronaRate koronaRate = new KoronaRate();
        koronaRate.setRate(BigDecimal.valueOf(1.2));
        koronaRate.setCurrencyFrom("USD");
        koronaRate.setCurrencyTo("EUR");

        when(rateRepository.findLastRateCurrency("USA", "USD", "EUR")).thenReturn(Optional.of(koronaRate));

        // when
        Optional<Rate> result = koronaRateService.getLastRate(groupSub);

        // then
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(koronaRate, result.get());

        verify(rateRepository).findLastRateCurrency("USA", "USD", "EUR");
        verifyNoMoreInteractions(rateRepository);
    }

    @Test
    void shouldProperlySaveWhenCallsRateRepositorySave() {
        // given
        Rate rate = new Rate();
        rate.setRate(BigDecimal.valueOf(1.2));
        rate.setDate(LocalDateTime.now());
        rate.setCurrencyFrom("USD");
        rate.setCurrencyTo("EUR");

        RequestDetails requestDetails = new RequestDetails("USA");
        GroupSub groupSub = new GroupSub();
        groupSub.setRequestDetails(requestDetails);
        groupSub.setCurrencyFrom("USD");
        groupSub.setCurrencyTo("EUR");

        // when
        koronaRateService.save(rate, groupSub);

        // then
        verify(rateRepository).save(any(KoronaRate.class));
        verifyNoMoreInteractions(rateRepository);
    }
}
