package com.tishinserg.spreadbot.repository.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@Entity
@Table(name = "uni_rate")
public class UnistreamRate {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "currency")
    private String currency;

    @Column(name = "rate")
    private Double rate;

    @Column(name = "date")
    private LocalDateTime date;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnistreamRate that = (UnistreamRate) o;
        return id.equals(that.id) &&
                currency.equals(that.currency) &&
                rate.equals(that.rate) &&
                date.equals(that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, currency, rate, date);
    }
}
