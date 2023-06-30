package com.tishinserg.spreadbot.models;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rate rate1 = (Rate) o;
        return Objects.equals(currencyFrom, rate1.currencyFrom) &&
                Objects.equals(currencyTo, rate1.currencyTo) &&
                Objects.equals(rate, rate1.rate) &&
                Objects.equals(date.truncatedTo(ChronoUnit.MINUTES), rate1.date.truncatedTo(ChronoUnit.MINUTES));
    }

    @Override
    public int hashCode() {
        return Objects.hash(currencyFrom, currencyTo, rate, date);
    }
}
