package com.tishinserg.spreadbot.job;

import com.tishinserg.spreadbot.service.FindRateUpdatesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Slf4j
@Component
public class FindRateUpdatesJob {

    private final FindRateUpdatesService findRateUpdatesService;

    @Autowired
    public FindRateUpdatesJob(FindRateUpdatesService findRateUpdatesService) {
        this.findRateUpdatesService = findRateUpdatesService;
    }

    @Scheduled(fixedRate = 300000)
    public void findRateUpdates() {
        LocalDateTime start = LocalDateTime.now();

        log.info("Find rates updates job started.");

        findRateUpdatesService.findRateUpdates();

        LocalDateTime end = LocalDateTime.now();

        log.info("Find rate updates job finished. Took seconds: {}",
                end.toEpochSecond(ZoneOffset.UTC) - start.toEpochSecond(ZoneOffset.UTC));
    }
}
