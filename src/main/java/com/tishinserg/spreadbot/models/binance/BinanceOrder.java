package com.tishinserg.spreadbot.models.binance;

import lombok.Data;

import java.io.Serializable;

@Data
public class BinanceOrder implements Serializable {

    private Double price;
    private Double surplusAmount;

    public BinanceOrder(Double price, Double surplusAmount) {
        this.price = price;
        this.surplusAmount = surplusAmount;
    }

    public BinanceOrder() {
    }
}
