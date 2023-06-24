package com.tishinserg.spreadbot.parsing;

import com.tishinserg.spreadbot.converters.UnistreamConverter;
import com.tishinserg.spreadbot.models.Rate;
import com.tishinserg.spreadbot.models.unistream.UnistreamAnswerDetailedJson;
import com.tishinserg.spreadbot.models.unistream.UnistreamAnswerModel;
import com.tishinserg.spreadbot.properties.UnistreamRateParsingServiceProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

// todo перенес хедеры сюда из appconfig чтобы было легче тестировать. как правильно?

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(UnistreamRateParsingServiceProperties.class)
public class UnistreamRateParsingService implements ParsingService {
    private final WebClient uniRateParsingServiceWebClient;
    private final UnistreamRateParsingServiceProperties parsingServiceProperties;

    @Override
    public Mono<Rate> getRate(String... params) {
        String countryName = params[0];
        String currencyFrom = params[1];
        String currencyTo = params[2];
        return uniRateParsingServiceWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("payout_type", "cash")
                        .queryParam("destination", countryName)
                        .queryParam("amount", 2000)
                        .queryParam("currency", currencyFrom)
                        .queryParam("accepted_currency", currencyTo)
                        .queryParam("profile", "unistream")
                        .queryParam("promo_id", parsingServiceProperties.getPromoId())
                        .build())
                .header(HttpHeaders.AUTHORIZATION, parsingServiceProperties.getToken())
                .header(HttpHeaders.USER_AGENT, parsingServiceProperties.getUserAgent())
                .header("authority", "online.unistream.ru")
                .header("_ym_uid=1668782517749595536; tmr_lvid=3a980b5a5ae8b3f2d26bcfbc931e60e3; tmr_lvidTS=1668782516649; " +
                        "_ym_d=1685079906; uni_c2c_source=https://unistream.ru/online/; _ym_isad=2; _clck=hiaso|2|fcp|0|1241; _clsk=func5u|1687525632259|2|1|x.clarity.ms/collect; __js_p_=465,1800,1,0,0; __jhash_=781; __jua_=Mozilla%2F5.0%20%28Macintosh%3B%20Intel%20Mac%20OS%20X%2010_15_7%29%20AppleWebKit%2F537.36%20%28KHTML%2C%20like%20Gecko%29%20Chrome%2F113.0.0.0%20Safari%2F537.36; __hash_=4ffb7b038d0bbf9cf755e8fee8a903d9; __lhash_=e0fef6335dbfc2e4c4e6e17430faba71; PHPSESSID=c81528pa9gso8gt5nm66ssof4i; tmr_detect=0%7C1687530471908")
                .header("x-requested-with", "XMLHttpRequest")
                .retrieve()
                .bodyToMono(UnistreamAnswerModel.class)
                .log()
                .map(answerModel -> {
                    List<UnistreamAnswerDetailedJson> fees = answerModel.getFees();
                    if (fees != null && !fees.isEmpty()) {
                        UnistreamAnswerDetailedJson fee = fees.stream()
                                .min(Comparator.comparing(UnistreamAnswerDetailedJson::getAcceptedAmount))
                                .get();
                        return new Rate(currencyFrom, currencyTo, UnistreamConverter.feesToRate(fee), LocalDateTime.now());
                    }
                    return null;
                });
    }

}
