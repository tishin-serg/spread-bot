package com.tishinserg.spreadbot.parsing;

import com.tishinserg.spreadbot.models.Rate;
import com.tishinserg.spreadbot.models.koronapay.KoronaAnswerModel;
import com.tishinserg.spreadbot.properties.KoronaRateParsingServiceProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(KoronaRateParsingServiceProperties.class)
public class KoronaRateParsingService implements ParsingService {

    private final WebClient koronaRateParsingServiceWebClient;
    private final KoronaRateParsingServiceProperties properties;

    @Override
    public Mono<Rate> getRate(String... params) {
        String countryName = params[0];
        String currencyFrom = params[1];
        String currencyTo = params[2];
        return koronaRateParsingServiceWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("sendingCountryId", properties.getSendingCountryId())
                        .queryParam("sendingCurrencyId", currencyTo)
                        .queryParam("receivingCountryId", countryName)
                        .queryParam("receivingCurrencyId", currencyFrom)
                        .queryParam("paymentMethod", "debitCard")
                        .queryParam("receivingAmount", properties.getAmount())
                        .queryParam("receivingMethod", "cash")
                        .queryParam("paidNotificationEnabled", "false")
                        .build())
                .retrieve()
                .bodyToMono(KoronaAnswerModel[].class)
                .log()
                .flatMap(koronaAnswerModels -> {
                    if (koronaAnswerModels.length > 0) {
                        KoronaAnswerModel koronaAnswerModel = koronaAnswerModels[0]; // Получаем первый элемент массива
                        return Mono.just(new Rate(currencyFrom, currencyTo, BigDecimal.valueOf(koronaAnswerModel.getExchangeRate()),
                                LocalDateTime.now()));
                    } else {
                        return Mono.error(new IllegalStateException("No data found."));
                    }
                })
                .retryWhen(Retry.backoff(10, Duration.ofSeconds(10))
                        .filter(throwable -> throwable instanceof WebClientRequestException));
    }
}
