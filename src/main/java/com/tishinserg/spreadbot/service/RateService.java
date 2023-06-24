package com.tishinserg.spreadbot.service;

import com.tishinserg.spreadbot.models.Rate;
import com.tishinserg.spreadbot.repository.entity.GroupSub;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface RateService {

    Boolean compareRates(Rate lastRate, Rate currentRate);

    CompletableFuture<Rate> getCurrentRate(GroupSub groupSub);

    Optional<Rate> getLastRate(GroupSub groupSub);

    void save(Rate rate, GroupSub groupSub);

}
