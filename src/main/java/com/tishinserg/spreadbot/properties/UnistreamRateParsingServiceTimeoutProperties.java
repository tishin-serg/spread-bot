package com.tishinserg.spreadbot.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "parsing.unistream.timeouts")
@Data
public class UnistreamRateParsingServiceTimeoutProperties {
    private Integer connect;
    private Integer read;
    private Integer write;
}
