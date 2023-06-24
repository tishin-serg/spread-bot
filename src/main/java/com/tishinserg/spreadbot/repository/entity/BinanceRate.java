package com.tishinserg.spreadbot.repository.entity;

import com.tishinserg.spreadbot.models.Rate;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "binance_rate")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class BinanceRate extends Rate {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "trade_type")
    private String tradeType;

    public BinanceRate() {
    }

    public BinanceRate(String currencyFrom, String currencyTo, BigDecimal rate, LocalDateTime date, String paymentMethod, String tradeType) {
        super(currencyFrom, currencyTo, rate, date);
        this.paymentMethod = paymentMethod;
        this.tradeType = tradeType;
    }

    public BinanceRate(String paymentMethod, String tradeType) {
        this.paymentMethod = paymentMethod;
        this.tradeType = tradeType;
    }
}
