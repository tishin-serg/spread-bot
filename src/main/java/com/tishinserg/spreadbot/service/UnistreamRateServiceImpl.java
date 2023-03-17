package com.tishinserg.spreadbot.service;

import com.tishinserg.spreadbot.converters.UnistreamConverter;
import com.tishinserg.spreadbot.parsing.UnistreamRateParsingService;
import com.tishinserg.spreadbot.repository.UnistreamRateRepository;
import com.tishinserg.spreadbot.repository.entity.UnistreamRate;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UnistreamRateServiceImpl implements UnistreamRateService {
    private final UnistreamRateRepository unistreamRateRepository;
    private final UnistreamRateParsingService unistreamRateParsingService;

    @Override
    public void save(UnistreamRate unistreamRate) {
        unistreamRateRepository.save(unistreamRate);
    }

    //todo возвращать optional
    @Override
    public UnistreamRate findLastRate() {
        return unistreamRateRepository.findTopByOrderByIdDesc();
    }

    @Override
    public UnistreamRate getActualRate() {
        return UnistreamConverter.jSonToEntity(unistreamRateParsingService.getRate().getFees().get(0));
    }

    // @Scheduled(fixedRate = 15000)
    public void saveActualRate() {
        unistreamRateRepository.save(getActualRate());
    }
}
