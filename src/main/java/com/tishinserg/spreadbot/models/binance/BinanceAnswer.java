package com.tishinserg.spreadbot.models.binance;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Data
public class BinanceAnswer implements Serializable {

    private String code;
    private String message;
    private String messageDetail;
    @JsonProperty("data")
    List<BinanceAnswerDetailed> data;
    private Integer total;
    private Boolean success;
}
