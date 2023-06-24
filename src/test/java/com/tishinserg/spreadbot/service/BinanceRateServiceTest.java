package com.tishinserg.spreadbot.service;

import com.tishinserg.spreadbot.models.Rate;
import com.tishinserg.spreadbot.models.RequestDetails;
import com.tishinserg.spreadbot.parsing.BinanceRateParsingService;
import com.tishinserg.spreadbot.repository.BinanceRateRepository;
import com.tishinserg.spreadbot.repository.entity.BinanceRate;
import com.tishinserg.spreadbot.repository.entity.GroupSub;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit-level testing for BinanceRateServiceImpl")
public class BinanceRateServiceTest {
    @Mock
    private BinanceRateRepository binanceRateRepository;

    @Mock
    private BinanceRateParsingService binanceRateParsingService;

    @InjectMocks
    private BinanceRateServiceImpl binanceRateService;

    @Test
    public void shouldCallRepositorySave() {
        BinanceRate binanceRate = new BinanceRate();
        binanceRateService.save(binanceRate);
        verify(binanceRateRepository, times(1)).save(binanceRate);
    }

    @Test
    public void shouldProperlyReturnCurrentRate() {
        //given
        RequestDetails requestDetails = new RequestDetails();
        requestDetails.setTradeType("SELL");
        requestDetails.setPaymentMethod("Tinkoff");
        GroupSub groupSub = new GroupSub();
        groupSub.setCurrencyFrom("USD");
        groupSub.setCurrencyTo("RUB");
        groupSub.setRequestDetails(requestDetails);

        Rate parsingResponse = new Rate();
        parsingResponse.setRate(BigDecimal.valueOf(1.5));
        parsingResponse.setCurrencyFrom("USD");
        parsingResponse.setCurrencyTo("RUB");
        Mono<Rate> rateMono = Mono.just(parsingResponse);

        when(binanceRateParsingService.getRate(groupSub.getRequestDetails().getPaymentMethod(), groupSub.getCurrencyFrom(),
                groupSub.getCurrencyTo(), groupSub.getRequestDetails().getTradeType())).thenReturn(rateMono);

        BinanceRate expectedRate = new BinanceRate();
        expectedRate.setRate(BigDecimal.valueOf(1.5));
        expectedRate.setCurrencyFrom("USD");
        expectedRate.setCurrencyTo("RUB");
        expectedRate.setTradeType("SELL");
        expectedRate.setPaymentMethod("Tinkoff");

        //when

        CompletableFuture<Rate> futureCurrentRate = binanceRateService.getCurrentRate(groupSub);
        Rate currentRate = futureCurrentRate.join();

        //then

        assertNotNull(currentRate);
        assertEquals(expectedRate.getRate(), currentRate.getRate());
        assertEquals(expectedRate.getCurrencyFrom(), currentRate.getCurrencyFrom());
        assertEquals(expectedRate.getCurrencyTo(), currentRate.getCurrencyTo());

        verify(binanceRateParsingService, times(1)).getRate(groupSub.getRequestDetails().getPaymentMethod(),
                groupSub.getCurrencyFrom(), groupSub.getCurrencyTo(), groupSub.getRequestDetails().getTradeType());
    }

    @Test
    public void shouldReturnLastRateWhenRateExists() {
        //given
        String payMethod = "Tinkoff";
        String currencyFrom = "USD";
        String currencyTo = "RUB";
        String tradeType = "SELL";

        BinanceRate expectedRate = new BinanceRate();
        expectedRate.setRate(BigDecimal.valueOf(1.2));
        expectedRate.setCurrencyFrom(currencyFrom);
        expectedRate.setCurrencyTo(currencyTo);
        expectedRate.setTradeType(tradeType);

        when(binanceRateRepository.findLastRateCurrency(payMethod, currencyFrom, currencyTo, tradeType))
                .thenReturn(Optional.of(expectedRate));

        //when
        BinanceRate lastRate = binanceRateService.getLastRate(payMethod, currencyFrom, currencyTo, tradeType);

        //then
        assertNotNull(lastRate);
        assertEquals(expectedRate.getRate(), lastRate.getRate());
        assertEquals(expectedRate.getCurrencyFrom(), lastRate.getCurrencyFrom());
        assertEquals(expectedRate.getCurrencyTo(), lastRate.getCurrencyTo());
        assertEquals(expectedRate.getTradeType(), lastRate.getTradeType());

        verify(binanceRateRepository, times(1)).findLastRateCurrency(payMethod, currencyFrom, currencyTo, tradeType);
    }

    @Test
    public void shouldReturnTrueWhenRateDifferenceIsGreaterThanOrEqual() {
        Rate lastRate = new Rate();
        lastRate.setRate(BigDecimal.valueOf(1.5));

        Rate currentRate = new Rate();
        currentRate.setRate(BigDecimal.valueOf(1.55));

        assertTrue(binanceRateService.compareRates(lastRate, currentRate));
    }

    @Test
    public void shouldReturnFalseWhenRateDifferenceIsLess() {
        Rate lastRate = new Rate();
        lastRate.setRate(BigDecimal.valueOf(1.5));

        Rate currentRate = new Rate();
        currentRate.setRate(BigDecimal.valueOf(1.52));

        assertFalse(binanceRateService.compareRates(lastRate, currentRate));
    }





}
