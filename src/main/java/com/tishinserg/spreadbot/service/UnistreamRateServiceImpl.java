package com.tishinserg.spreadbot.service;

import com.tishinserg.spreadbot.converters.UnistreamConverter;
import com.tishinserg.spreadbot.models.UnistreamAnswerDetailedJson;
import com.tishinserg.spreadbot.parsing.UnistreamRateParsingService;
import com.tishinserg.spreadbot.repository.UnistreamRateRepository;
import com.tishinserg.spreadbot.repository.entity.UnistreamRate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class UnistreamRateServiceImpl implements UnistreamRateService {
    private final UnistreamRateRepository unistreamRateRepository;
    private final UnistreamRateParsingService unistreamRateParsingService;

    @Override
    public void save(UnistreamRate unistreamRate) {
        unistreamRateRepository.save(unistreamRate);
    }

    //todo корректно ли использовать этот метод здесь, а не в unistreamRateParsingService
    @Override
    public UnistreamRate getCurrentRate(String countryName, String currencyFrom, String currencyTo) {
        UnistreamAnswerDetailedJson json =
                unistreamRateParsingService.getRate(countryName, currencyFrom, currencyTo).getFees().get(0);
        return UnistreamConverter.jSonToEntity(json, countryName);
    }

    //todo вынести шаг изменения цены в проперти
    @Override
    public Boolean compareUnistreamRates(UnistreamRate lastUnistreamRateFromDb, UnistreamRate currentUnistreamRate) {
        if (lastUnistreamRateFromDb == null) return true;
        return currentUnistreamRate.getRate().subtract(lastUnistreamRateFromDb.getRate()).abs().compareTo(BigDecimal.valueOf(0.05)) >= 0;
    }

    @Override
    public UnistreamRate getLastRate(String country, String currency) {
        return unistreamRateRepository.findLastRateCurrency(country, currency);
    }
}

