package com.animalshelter.animalshelterbot.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;

@Setter
@Getter
@EqualsAndHashCode
@Entity
public class CatReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date date;

    private String photo;

    private String text;

    @ManyToOne
    @JoinColumn(name = "adopted_cat_id")
    private AdoptedCat adoptedCat;

}
