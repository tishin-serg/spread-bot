package com.tishinserg.spreadbot.repository.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "group_sub")
@EqualsAndHashCode(exclude = "users")
public class GroupSub {
    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "tittle")
    private String tittle;
    @Column(name = "service")
    private String service;
    @Column(name = "country")
    private String country;
    @Column(name = "currency_from")
    private String currencyFrom;
    @Column(name = "currency_to")
    private String currencyTo;
    // колонка для последнего значения курса
    @Column(name = "last_rate")
    private BigDecimal lastRate;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "group_x_user",
            joinColumns = @JoinColumn(name = "group_sub_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<TelegramUser> users = new ArrayList<>();


}
