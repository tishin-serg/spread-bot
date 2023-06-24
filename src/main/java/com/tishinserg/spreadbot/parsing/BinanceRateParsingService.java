package com.tishinserg.spreadbot.parsing;

import com.tishinserg.spreadbot.models.Rate;
import com.tishinserg.spreadbot.models.binance.BinanceAnswer;
import com.tishinserg.spreadbot.models.binance.BinanceAnswerDetailed;
import com.tishinserg.spreadbot.models.binance.BinanceOrder;
import com.tishinserg.spreadbot.models.binance.BinanceRequest;
import com.tishinserg.spreadbot.properties.BinanceRateParsingServiceProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(BinanceRateParsingServiceProperties.class)
@Slf4j
public class BinanceRateParsingService implements ParsingService {

    private final WebClient binanceRateParsingServiceWebClient;

    /**
     * Retrieves the rate for the specified parameters from the Binance rate parsing service.
     *
     * @param params the parameters required to retrieve the rate. The order of parameters is as follows:
     *               1. payType - name of bank for transaction.
     *               2. currencyFrom - the currency from which the rate is being calculated.
     *               3. currencyTo - the currency to which the rate is being calculated.
     *               4. tradeType - the trade type SELL or BUY.
     * @return a Mono emitting the Rate object representing the calculated rate.
     */
    @Override
    public Mono<Rate> getRate(String... params) {
        String payType = params[0];
        String currencyFrom = params[1];
        String currencyTo = params[2];
        String tradeType = params[3];

        return binanceRateParsingServiceWebClient
                .post()
                .bodyValue(new BinanceRequest(payType, currencyFrom, currencyTo, tradeType))
                .retrieve()
                .bodyToMono(BinanceAnswer.class)
                .log()
                .map(binanceAnswer -> {
                    double average = binanceAnswer.getData().stream()
                            .map(BinanceAnswerDetailed::getOrder)
                            .filter(o -> o.getSurplusAmount() > 5000)
                            .mapToDouble(BinanceOrder::getPrice)
                            .average()
                            .orElse(Double.NaN);
                    log.info(String.valueOf(average));
                    return new Rate(currencyFrom, currencyTo, BigDecimal.valueOf(average).setScale(2, RoundingMode.CEILING), LocalDateTime.now());
                });


    }
}
