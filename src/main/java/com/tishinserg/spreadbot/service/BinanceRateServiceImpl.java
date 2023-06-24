package com.tishinserg.spreadbot.service;

import com.tishinserg.spreadbot.converters.BinanceConverter;
import com.tishinserg.spreadbot.models.Rate;
import com.tishinserg.spreadbot.parsing.BinanceRateParsingService;
import com.tishinserg.spreadbot.repository.BinanceRateRepository;
import com.tishinserg.spreadbot.repository.entity.BinanceRate;
import com.tishinserg.spreadbot.repository.entity.GroupSub;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class BinanceRateServiceImpl implements BinanceRateService {
    private final BinanceRateRepository binanceRepository;
    private final BinanceRateParsingService parsingService;

    @Override
    public void save(BinanceRate binanceRate) {
        binanceRepository.save(binanceRate);
    }

    @Override
    public BinanceRate getCurrentRate(String payMethod, String currencyFrom, String currencyTo, String tradeType) {
        return BinanceConverter.modelToEntity(Objects.requireNonNull(parsingService.getRate(payMethod, currencyFrom, currencyTo, tradeType).block()),
                payMethod, tradeType);
    }

    @Override
    public BinanceRate getLastRate(String payMethod, String currencyFrom, String currencyTo, String tradeType) {
        return binanceRepository.findLastRateCurrency(payMethod, currencyFrom, currencyTo, tradeType).orElseThrow(NotFoundException::new);
    }

    @Override
    public Boolean compareRates(Rate lastRate, Rate currentRate) {
        if (lastRate == null) return true;
        return currentRate.getRate().subtract(lastRate.getRate()).abs().compareTo(BigDecimal.valueOf(0.03)) >= 0;
    }

    @Override
    public CompletableFuture<Rate> getCurrentRate(GroupSub groupSub) {
        return parsingService
                .getRate(groupSub.getRequestDetails().getPaymentMethod(), groupSub.getCurrencyFrom(), groupSub.getCurrencyTo(),
                        groupSub.getRequestDetails().getTradeType())
                .map(r -> {
                    Rate rate = new Rate();
                    rate.setRate(r.getRate());
                    rate.setDate(LocalDateTime.now());
                    rate.setCurrencyTo(r.getCurrencyTo());
                    rate.setCurrencyFrom(r.getCurrencyFrom());
                    return rate;
                })
                .toFuture();
    }

    @Override
    public Optional<Rate> getLastRate(GroupSub groupSub) {
        return binanceRepository.findLastRateCurrency(groupSub.getRequestDetails().getPaymentMethod(),
                groupSub.getCurrencyFrom(), groupSub.getCurrencyTo(),
                groupSub.getRequestDetails().getTradeType()).map(binanceRate -> (Rate) binanceRate);
    }

    @Override
    public void save(Rate rate, GroupSub groupSub) {
        BinanceRate binanceRate = new BinanceRate();
        binanceRate.setRate(rate.getRate());
        binanceRate.setDate(rate.getDate());
        binanceRate.setCurrencyFrom(rate.getCurrencyFrom());
        binanceRate.setCurrencyTo(rate.getCurrencyTo());
        binanceRate.setTradeType(groupSub.getRequestDetails().getTradeType());
        binanceRate.setPaymentMethod(groupSub.getRequestDetails().getPaymentMethod());
        binanceRepository.save(binanceRate);
    }

}
