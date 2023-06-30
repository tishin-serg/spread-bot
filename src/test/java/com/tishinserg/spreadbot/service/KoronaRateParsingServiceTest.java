package com.tishinserg.spreadbot.service;

import com.tishinserg.spreadbot.models.Rate;
import com.tishinserg.spreadbot.parsing.KoronaRateParsingService;
import com.tishinserg.spreadbot.properties.KoronaRateParsingServiceProperties;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import okhttp3.mockwebserver.SocketPolicy;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("Integration-level testing for KoronaRateParsingServiceImpl")
public class KoronaRateParsingServiceTest {
    private MockWebServer mockWebServer;
    private KoronaRateParsingService koronaRateParsingService;

    @BeforeEach
    void setupMockWebServer() {
        mockWebServer = new MockWebServer();
        KoronaRateParsingServiceProperties properties = new KoronaRateParsingServiceProperties();
        koronaRateParsingService = new KoronaRateParsingService(WebClient.create(mockWebServer.url("/").url().toString()),
                properties);
    }

    @Test
    void makesTheCorrectRequest() throws InterruptedException {
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(HttpStatus.OK.value())
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .setBody(getJson("korona-message-response.json"))
        );

        StepVerifier.create(koronaRateParsingService.getRate("GEO", "840", "810"))
                .expectNextMatches(rate -> rate.getCurrencyFrom().equals("840")
                        && rate.getCurrencyTo().equals("810")
                        && rate.getRate().equals(BigDecimal.valueOf(91.49)))
                .verifyComplete();

        RecordedRequest request = mockWebServer.takeRequest();
        String query = request.getRequestUrl().query();
        assertThat(request.getMethod()).isEqualTo("GET");
        assertThat(query).contains("sendingCurrencyId=810");
        assertThat(query).contains("receivingCountryId=GEO");
        assertThat(query).contains("receivingCurrencyId=840");
        assertThat(request.getRequestUrl().querySize()).isEqualTo(8);
    }

    @Test
    void shouldProperlyReturnsErrorWhenNoDataFound() {
        // given
        String countryName = "GEO";
        String currencyFrom = "840";
        String currencyTo = "810";

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody("[]"));

        // when
        Mono<Rate> result = koronaRateParsingService.getRate(countryName, currencyFrom, currencyTo);

        // then
        StepVerifier.create(result)
                .expectError(IllegalStateException.class)
                .verify();
    }

    //todo придумать к сэмулировать выброс исключения webclientrequestexception для теста
    /*@Test
    void getRate_RetriesOnWebClientRequestException() {
        // Arrange
        String countryName = "USA";
        String currencyFrom = "USD";
        String currencyTo = "EUR";

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.BAD_REQUEST.value()));

        // Act
        Mono<Rate> result = koronaRateParsingService.getRate(countryName, currencyFrom, currencyTo);

        // Assert
        StepVerifier.create(result)
                .expectError(WebClientRequestException.class)
                .verify(Duration.ofSeconds(30)); // Verify that the error is thrown within 10 seconds

        // Verify the number of requests sent by WebClient (retry + initial request)
        assertThat(mockWebServer.getRequestCount()).isGreaterThan(1);
    }*/

    @AfterEach
    void tearDown() throws Exception {
        mockWebServer.shutdown();
    }

    private String getJson(String json) {
        try (InputStream jsonStream = this.getClass().getClassLoader().getResourceAsStream(json)) {
            assert jsonStream != null;
            return new String(jsonStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

}
