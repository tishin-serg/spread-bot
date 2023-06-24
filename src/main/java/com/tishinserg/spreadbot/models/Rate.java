package com.tishinserg.spreadbot.models;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@MappedSuperclass
@Data
public class Rate {

    @Column(name = "currency_from")
    private String currencyFrom;

    @Column(name = "currency_to")
    private String currencyTo;

    @Column
    private BigDecimal rate;

    @Column
    private LocalDateTime date;

    public Rate(String currencyFrom, String currencyTo, BigDecimal rate, LocalDateTime date) {
        this.currencyFrom = currencyFrom;
        this.currencyTo = currencyTo;
        this.rate = rate;
        this.date = date;
    }

    public Rate() {
    }
}
