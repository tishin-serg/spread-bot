package com.tishinserg.spreadbot.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "parsing.korona")
@ConstructorBinding
@Data
public class KoronaRateParsingServiceProperties {
    private String url;
    private String amount;
    private String sendingCountryId;
}
