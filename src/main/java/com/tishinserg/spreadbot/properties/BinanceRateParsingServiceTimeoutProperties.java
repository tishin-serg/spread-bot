package com.tishinserg.spreadbot.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "parsing.binance.timeouts")
@Data
public class BinanceRateParsingServiceTimeoutProperties {
    private Integer connect;
    private Integer read;
    private Integer write;
}
