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
public class AdoptedDog {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String dogName;
        private Date adoptionDate;
        private Integer trialPeriod;

        public AdoptedDog() {
        }
        public AdoptedDog(String dogName, Date adoptionDate, Integer trialPeriod){
            this.dogName = dogName;
            this.adoptionDate = adoptionDate;
            this.trialPeriod = trialPeriod;
        }

        @Override
        public String toString () {
            return "Собака по кличке:" + dogName + "взято из приюта:" + adoptionDate
                    + "период адаптации:" + trialPeriod;
        }
    }

