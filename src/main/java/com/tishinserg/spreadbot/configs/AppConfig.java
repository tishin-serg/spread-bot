package com.tishinserg.spreadbot.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tishinserg.spreadbot.properties.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.pool.ChannelPool;
import io.netty.channel.pool.ChannelPoolHandler;
import io.netty.channel.pool.FixedChannelPool;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.netty.tcp.TcpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties({UnistreamRateParsingServiceTimeoutProperties.class,
        BinanceRateParsingServiceTimeoutProperties.class, KoronaRateParsingServiceTimeoutProperties.class})
public class AppConfig {
    private final UnistreamRateParsingServiceProperties unistreamRateParsingServiceProperties;
    private final UnistreamRateParsingServiceTimeoutProperties unistreamRateParsingServiceTimeoutProperties;
    private final BinanceRateParsingServiceProperties binanceRateParsingServiceProperties;
    private final BinanceRateParsingServiceTimeoutProperties binanceRateParsingServiceTimeoutProperties;
    private final KoronaRateParsingServiceProperties koronaRateParsingServiceProperties;
    private final KoronaRateParsingServiceTimeoutProperties koronaRateParsingServiceTimeoutProperties;

    @Bean
    public WebClient uniRateParsingServiceWebClient() {
        TcpClient tcpClient = TcpClient
                .create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, unistreamRateParsingServiceTimeoutProperties.getConnect())
                .doOnConnected(connection -> {
                    connection.addHandlerLast(new ReadTimeoutHandler(unistreamRateParsingServiceTimeoutProperties.getRead(), TimeUnit.MILLISECONDS));
                    connection.addHandlerLast(new WriteTimeoutHandler(unistreamRateParsingServiceTimeoutProperties.getWrite(), TimeUnit.MILLISECONDS));
                });

        return WebClient
                .builder()
                .baseUrl(unistreamRateParsingServiceProperties.getUrl())
                .clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient)))
                .build();
    }

    //todo подумать как отключить переиспользование акторов
    @Bean
    public WebClient koronaRateParsingServiceWebClient() {
        TcpClient tcpClient = TcpClient
                .create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, koronaRateParsingServiceTimeoutProperties.getConnect())
                .doOnConnected(connection -> {
                    connection.addHandlerLast(new ReadTimeoutHandler(koronaRateParsingServiceTimeoutProperties.getRead(), TimeUnit.MILLISECONDS));
                    connection.addHandlerLast(new WriteTimeoutHandler(koronaRateParsingServiceTimeoutProperties.getWrite(), TimeUnit.MILLISECONDS));
                });

        return WebClient
                .builder()
                .baseUrl(koronaRateParsingServiceProperties.getUrl())
                .clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient)))
                .build();
    }


    @Bean
    public WebClient binanceRateParsingServiceWebClient() {
        TcpClient tcpClient = TcpClient
                .create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, binanceRateParsingServiceTimeoutProperties.getConnect())
                .doOnConnected(connection -> {
                    connection.addHandlerLast(new ReadTimeoutHandler(binanceRateParsingServiceTimeoutProperties.getRead(), TimeUnit.MILLISECONDS));
                    connection.addHandlerLast(new WriteTimeoutHandler(binanceRateParsingServiceTimeoutProperties.getWrite(), TimeUnit.MILLISECONDS));
                });

        return WebClient
                .builder()
                .baseUrl(binanceRateParsingServiceProperties.getUrl())
                .clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient)))
                .build();
    }

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
