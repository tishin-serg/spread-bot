package com.tishinserg.spreadbot.parsing;

import com.tishinserg.spreadbot.models.Rate;
import reactor.core.publisher.Mono;

public interface ParsingService {
    Mono<Rate> getRate(String ...params);
}
