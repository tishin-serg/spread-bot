package com.tishinserg.spreadbot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tishinserg.spreadbot.models.Rate;
import com.tishinserg.spreadbot.models.binance.BinanceRequest;
import com.tishinserg.spreadbot.parsing.BinanceRateParsingService;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BinanceRateParsingServiceTest {
    private MockWebServer mockWebServer;
    private BinanceRateParsingService binanceRateParsingService;

    @BeforeEach
    void setupMockWebServer() {
        mockWebServer = new MockWebServer();
        binanceRateParsingService = new BinanceRateParsingService(WebClient.create(mockWebServer.url("/").url().toString()));
    }

    @Test
    void makesCorrectRequest() throws InterruptedException, JsonProcessingException {
        //given
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(HttpStatus.OK.value())
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .setBody(getJson("binance-message-response.json"))
        );

        String paymentMethod = "RaiffeisenBank";
        String currencyFrom = "USDT";
        String currencyTo = "RUB";
        String tradeType = "SELL";

        BinanceRequest binanceRequest = new BinanceRequest(paymentMethod, currencyFrom, currencyTo, tradeType);
        ObjectMapper objectMapper = new ObjectMapper();
        String expectedBody = objectMapper.writeValueAsString(binanceRequest);

        //when
        Mono<Rate> rateMono = binanceRateParsingService.getRate(paymentMethod, currencyFrom, currencyTo, tradeType);

        //then
        StepVerifier.create(rateMono)
                .assertNext(rate -> {
                    assertEquals(currencyFrom, rate.getCurrencyFrom());
                    assertEquals(currencyTo, rate.getCurrencyTo());
                    assertNotNull(rate.getRate());
                    assertNotNull(rate.getDate());
                })
                .expectComplete()
                .verify();

        RecordedRequest request = mockWebServer.takeRequest();
        String actualBody = new String(request.getBody().readByteArray(), StandardCharsets.UTF_8);
        assertThat(request.getMethod()).isEqualTo("POST");
        assertThat(actualBody).isEqualTo(expectedBody);
    }

    @AfterEach
    void tearDown() throws Exception {
        mockWebServer.shutdown();
    }

    private String getJson(String path) {
        try (InputStream jsonStream = this.getClass().getClassLoader().getResourceAsStream(path)) {
            assert jsonStream != null;
            return new String(jsonStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
