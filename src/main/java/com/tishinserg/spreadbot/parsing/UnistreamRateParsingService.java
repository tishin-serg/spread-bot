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

    //todo вынести хедеры в проперти
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
                .header("accept", "application/json, text/javascript, */*; q=0.01")
                .header("accept-language", "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7")
                .header("referer", "https://online.unistream.ru/card2cash/?form_encoded=v2.local.tPQwvMYyt_f-mNqSqBiYI-SIlbeDGRbz0SshdsOr2ZHUVRSZqkNFZZV777c3-tagRmLVk5dk9Y2KBMA8sakb_Dm_5C7lrg2VbTJDeXR0R_jtqKRNcXvT424JNvL2TSXp7cLMtctb0uEjkzkQKASKTBTa457aQrGkmVNiNfA0AfAoKDsRo9hAPr9wYbsp6lXJDJFUOSzhqjzRw0ISO5YYET7KQjfTMwuhhSrg5adMSTwJTs4LPSP5ECpZBSjy5nJgzY_bUks_j6y8GDFcQVjLBXvwg3lGv6iuQXsQ7_PUGCz1Y6eg8UpX-DwmCA8nUcQtutbKclxr3B_ogLulBZFZmVLs1nUNv_SNc4tJi7MozouLvgEj4jTmBGe8R9ka-Ynap2-bv_JO6IAHMOEx8VHW-X-XGQCZ10i-viRd3a4zm-8GRCqT53iUEARr&utm_source=account.unistream.ru&utm_medium=referral&utm_campaign=account.unistream.ru&utm_referrer=account.unistream.ru")
                .header("sec-ch-ua", "\"Google Chrome\";v=\"113\", \"Chromium\";v=\"113\", \"Not-A.Brand\";v=\"24\"")
                .header("sec-ch-ua-mobile", "?0")
                .header("sec-ch-ua-platform", "\"macOS\"")
                .header("sec-fetch-dest", "empty")
                .header("sec-fetch-mode", "cors")
                .header("sec-fetch-site", "same-origin")
                .header("cookie", "_ym_uid=1668782517749595536; tmr_lvid=3a980b5a5ae8b3f2d26bcfbc931e60e3; tmr_lvidTS=1668782516649; _ym_d=1685079906; uni_c2c_source=https://unistream.ru/online/; _clck=hiaso|2|fcp|0|1241; __lhash_=e0fef6335dbfc2e4c4e6e17430faba71; __js_p_=977,1800,1,0,0; __jhash_=743; __jua_=Mozilla%2F5.0%20%28Macintosh%3B%20Intel%20Mac%20OS%20X%2010_15_7%29%20AppleWebKit%2F537.36%20%28KHTML%2C%20like%20Gecko%29%20Chrome%2F113.0.0.0%20Safari%2F537.36; __hash_=35b05443114fc004275dc688c5f174e9; PHPSESSID=ro24e6iqtn7rvl7k0v37lrgh3r; _ym_isad=2; tmr_detect=0%7C1687954984978")
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
