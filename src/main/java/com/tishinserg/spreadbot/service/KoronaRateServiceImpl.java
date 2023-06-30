package com.tishinserg.spreadbot.service;

import com.tishinserg.spreadbot.models.Rate;
import com.tishinserg.spreadbot.parsing.KoronaRateParsingService;
import com.tishinserg.spreadbot.repository.KoronaRateRepository;
import com.tishinserg.spreadbot.repository.entity.GroupSub;
import com.tishinserg.spreadbot.repository.entity.KoronaRate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class KoronaRateServiceImpl implements KoronaPayRateService {
    private final KoronaRateParsingService parsingService;
    private final KoronaRateRepository rateRepository;

    @Override
    public Boolean compareRates(Rate lastRate, Rate currentRate) {
        if (lastRate == null) return true;
        return currentRate.getRate().subtract(lastRate.getRate()).abs().compareTo(BigDecimal.valueOf(0.05)) >= 0;
    }

    @Override
    public CompletableFuture<Rate> getCurrentRate(GroupSub groupSub) {
        return parsingService.getRate(groupSub.getRequestDetails().getCountry(), groupSub.getCurrencyFrom(),
                groupSub.getCurrencyTo())
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
        return rateRepository.findLastRateCurrency(groupSub.getRequestDetails().getCountry(),
                groupSub.getCurrencyFrom(),
                groupSub.getCurrencyTo()).map(koronaRate -> (Rate) koronaRate);
    }

    @Override
    public void save(Rate rate, GroupSub groupSub) {
        KoronaRate koronaRate = new KoronaRate();
        koronaRate.setRate(rate.getRate());
        koronaRate.setDate(rate.getDate());
        koronaRate.setCurrencyFrom(rate.getCurrencyFrom());
        koronaRate.setCurrencyTo(rate.getCurrencyTo());
        koronaRate.setCountry(groupSub.getRequestDetails().getCountry());
        rateRepository.save(koronaRate);
    }
}
