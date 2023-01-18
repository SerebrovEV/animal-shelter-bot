package com.animalshelter.animalshelterbot.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.util.Collection;

@Getter
@Setter
@Entity
public class AdoptedDog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String dogName;
    private Date adoptionDate;
    private Integer trialPeriod;

    @ManyToOne
    @JoinColumn(name = "dog_user_id")
    private DogUser dogUser;

    @OneToMany(mappedBy = "adoptedDog")
    private Collection<DogReport> dogReports;

    public AdoptedDog() {
    }

    public AdoptedDog(String dogName, Date adoptionDate, Integer trialPeriod) {
        this.dogName = dogName;
        this.adoptionDate = adoptionDate;
        this.trialPeriod = trialPeriod;
    }

    @Override
    public String toString() {
        return "Собака по кличке:" + dogName + "взята из приюта:" + adoptionDate
                + "период адаптации:" + trialPeriod;
    }
}

