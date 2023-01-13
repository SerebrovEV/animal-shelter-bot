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
public class DogReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date date;

    private String photo;

    private String text;


}
