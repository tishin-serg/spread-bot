package com.tishinserg.spreadbot.service;


import com.tishinserg.spreadbot.parsing.UnistreamRateParsingService;
import com.tishinserg.spreadbot.properties.UnistreamRateParsingServiceProperties;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.HttpHeaders;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

public class UnistreamRateParsingServiceTest {
    private MockWebServer mockWebServer;
    private UnistreamRateParsingService unistreamRateParsingService;
    private final static String TOKEN = "Bearer 5a48ece0c457d11ee87f520e20ad68d6f28cb2e6aeae397ea92d4cb550cf2545";
    private final static String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like " +
            "Gecko) Chrome/111.0.0.0 Safari/537.36";

    @BeforeEach
    void setupMockWebServer() {
        mockWebServer = new MockWebServer();
        UnistreamRateParsingServiceProperties properties = new UnistreamRateParsingServiceProperties();
        properties.setPromoId("445859");
        properties.setToken(TOKEN);
        properties.setUserAgent(USER_AGENT);
        unistreamRateParsingService = new UnistreamRateParsingService(WebClient.create(mockWebServer.url("/").url().toString()), properties);
    }

    @Test
    void makesTheCorrectRequest() throws InterruptedException {
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(HttpStatus.OK.value())
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .setBody(getJson("unistream-message-response.json"))
        );

        // unistreamRateParsingService.getRate("GEO", "USD", "RUB").block();
        StepVerifier.create(unistreamRateParsingService.getRate("GEO", "USD", "RUB"))
                .expectNextMatches(rate -> rate.getCurrencyFrom().equals("USD") && rate.getCurrencyTo().equals("RUB"))
                .verifyComplete();

        RecordedRequest request = mockWebServer.takeRequest();
        String query = request.getRequestUrl().query();
        assertThat(request.getMethod()).isEqualTo("GET");
        assertThat(query.contains("destination=GEO"));
        assertThat(query.contains("currency=USD"));
        assertThat(query.contains("accepted_currency=RUB"));
        assertThat(request.getHeader("authorization")).isEqualTo(TOKEN);
        assertThat(request.getHeader("user-agent")).isEqualTo(USER_AGENT);
        assertThat(request.getRequestUrl().querySize()).isEqualTo(7);
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
