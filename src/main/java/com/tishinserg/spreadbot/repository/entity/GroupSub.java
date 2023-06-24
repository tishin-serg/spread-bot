package com.tishinserg.spreadbot.repository.entity;

import com.tishinserg.spreadbot.models.RequestDetails;
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
public class GroupSub implements Comparable<GroupSub> {
    @Id
    @Column
    private Long id;

    @Column
    private String tittle;

    @Column
    private String service;

    @Column(name = "currency_from")
    private String currencyFrom;

    @Column(name = "currency_to")
    private String currencyTo;

    @Column(name = "last_rate")
    private BigDecimal lastRate;

    @Embedded
    private RequestDetails requestDetails;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "group_x_user",
            joinColumns = @JoinColumn(name = "group_sub_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<TelegramUser> users = new ArrayList<>();

    @Override
    public int compareTo(GroupSub o) {
        return Long.compare(this.id, o.id);
    }
}
