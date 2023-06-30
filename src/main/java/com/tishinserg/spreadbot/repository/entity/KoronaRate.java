package com.tishinserg.spreadbot.repository.entity;

import com.tishinserg.spreadbot.models.Rate;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "korona_rate")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class KoronaRate extends Rate {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String country;
}
