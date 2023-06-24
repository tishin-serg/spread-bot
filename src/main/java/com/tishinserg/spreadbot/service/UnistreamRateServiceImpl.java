package com.tishinserg.spreadbot.service;

import com.tishinserg.spreadbot.converters.UnistreamConverter;
import com.tishinserg.spreadbot.models.Rate;
import com.tishinserg.spreadbot.parsing.UnistreamRateParsingService;
import com.tishinserg.spreadbot.repository.UnistreamRateRepository;
import com.tishinserg.spreadbot.repository.entity.GroupSub;
import com.tishinserg.spreadbot.repository.entity.UnistreamRate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class UnistreamRateServiceImpl implements UnistreamRateService {
    private final UnistreamRateRepository unistreamRateRepository;
    private final UnistreamRateParsingService unistreamRateParsingService;

    @Override
    public UnistreamRate getCurrentRate(String countryName, String currencyFrom, String currencyTo) {
        Rate rate = unistreamRateParsingService.getRate(countryName, currencyFrom, currencyTo).block();
        return UnistreamConverter.modelToEntity(rate, countryName);
    }

    @Override
    public CompletableFuture<Rate> getCurrentRate(GroupSub groupSub) {
        return unistreamRateParsingService
                .getRate(groupSub.getRequestDetails().getCountry(), groupSub.getCurrencyFrom(), groupSub.getCurrencyTo())
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
    public UnistreamRate getLastRate(String country, String currencyFrom, String currencyTo) {
        return unistreamRateRepository.findLastRateCurrency(country, currencyFrom, currencyTo).orElse(new UnistreamRate());
    }

    @Override
    public Optional<Rate> getLastRate(GroupSub groupSub) {
        return unistreamRateRepository.findLastRateCurrency(groupSub.getRequestDetails().getCountry(),
                groupSub.getCurrencyFrom(),
                groupSub.getCurrencyTo()).map(unistreamRate -> (Rate) unistreamRate);
    }

    @Override
    public Boolean compareRates(Rate lastRate, Rate currentRate) {
        if (lastRate == null) return true;
        return currentRate.getRate().subtract(lastRate.getRate()).abs().compareTo(BigDecimal.valueOf(0.05)) >= 0;
    }

    /**
     * Compares the last known rate of a currency pair with the current rate and returns true if the
     * current rate is higher or lower than the last rate for 0.05 RUB.
     *
     * @param groupSub The group subscription for which to compare the rates.
     * @return true if the current rate is higher or lower than the last rate for 0.05 RUB, false otherwise.
     */
    public Boolean compareRates(GroupSub groupSub) {
        Rate lastRate = getLastRate(groupSub.getRequestDetails().getCountry(), groupSub.getCurrencyFrom(), groupSub.getCurrencyTo());
        Rate currentRate = getCurrentRate(groupSub.getRequestDetails().getCountry(), groupSub.getCurrencyFrom(), groupSub.getCurrencyTo());
        return compareRates(lastRate, currentRate);
    }

    @Override
    public void save(Rate rate, GroupSub groupSub) {
        UnistreamRate unistreamRate = new UnistreamRate();
        unistreamRate.setDate(rate.getDate());
        unistreamRate.setCurrencyFrom(rate.getCurrencyFrom());
        unistreamRate.setCurrencyTo(rate.getCurrencyTo());
        unistreamRate.setCountry(groupSub.getRequestDetails().getCountry());
        unistreamRate.setRate(rate.getRate());
        unistreamRateRepository.save(unistreamRate);
    }

    @Override
    public void save(UnistreamRate unistreamRate) {
        unistreamRateRepository.save(unistreamRate);
    }

}

