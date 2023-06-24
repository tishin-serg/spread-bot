package com.tishinserg.spreadbot.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "parsing.binance")
@Data
public class BinanceRateParsingServiceProperties {
    private String url;
}
