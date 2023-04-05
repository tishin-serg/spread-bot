package com.tishinserg.spreadbot.parsing;

import com.tishinserg.spreadbot.models.UnistreamAnswerModel;
import com.tishinserg.spreadbot.properties.UnistreamRateParsingServiceProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

// todo перенес хедеры сюда из appconfig чтобы было легче тестировать. как правильно?

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(UnistreamRateParsingServiceProperties.class)
public class UnistreamRateParsingService {
    private final WebClient uniRateParsingServiceWebClient;
    private final UnistreamRateParsingServiceProperties parsingServiceProperties;

    // https://online.unistream.ru/card2cash/calculate?payout_type=cash&destination=GEO&amount=2000&currency=USD
    // &accepted_currency=RUB&profile=unistream&promo_id=445859


    public UnistreamAnswerModel getRate(String countryName, String currencyFrom, String currencyTo) {
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
                .header("authorization", parsingServiceProperties.getToken())
                .header("user-agent", parsingServiceProperties.getUserAgent())
                .retrieve()
                .bodyToMono(UnistreamAnswerModel.class)
                .block();
    }
}
