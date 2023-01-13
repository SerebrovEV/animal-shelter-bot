package com.animalshelter.animalshelterbot.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Date;

@Getter
@Setter
@Entity
public class AdoptedCat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String catName;
    private Date adoptionDate;
    private Integer trialPeriod;

    public AdoptedCat() {
    }

    public AdoptedCat(String catName, Date adoptionDate, Integer trialPeriod){
        this.catName = catName;
        this.adoptionDate = adoptionDate;
        this.trialPeriod = trialPeriod;
    }

    @Override

    public String toString(){
        return "Кошка по кличке:" + catName + "взято из приюта:" + adoptionDate
                + "период адаптации:" + trialPeriod;
    }

}
