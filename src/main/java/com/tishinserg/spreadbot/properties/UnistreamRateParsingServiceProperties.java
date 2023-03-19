package com.tishinserg.spreadbot.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "parsing.unistream")
@Data
public class UnistreamRateParsingServiceProperties {
    private String url;

    @Value("promo-id")
    private String promoId;

    @Value("${parsing.unistream.headers.token}")
    private String token;

    @Value("${parsing.unistream.headers.user-agent}")
    private String userAgent;

}
