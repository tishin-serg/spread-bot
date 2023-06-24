package com.tishinserg.spreadbot.models.binance;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Data
public class BinanceAnswerDetailed implements Serializable {

    @JsonProperty("adv")
    private BinanceOrder order;
    @JsonProperty("advertiser")
    private Advertiser advertiser;


}
