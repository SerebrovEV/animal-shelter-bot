package com.animalshelter.animalshelterbot.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Date;


@Entity
public class AdoptedPet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String petName;
    private Date adoptionDate;
    private Integer trialPeriod;

    public AdoptedPet() {
    }
    public AdoptedPet(Long userId, String petName, Date adoptionDate, Integer trialPeriod){
        this.userId = userId;
        this.petName = petName;
        this.adoptionDate = adoptionDate;
        this.trialPeriod = trialPeriod;
    }

    public Long getId() { return id;  }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() {return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getPetName() { return petName; }
    public void setPetName(String petName) { this.petName = petName; }

    public Date getAdoptionDate() {return adoptionDate; }
    public void setAdoptionDate(Date adoptionDate) {this.adoptionDate = adoptionDate; }

    public Integer getTrialPeriod() { return trialPeriod; }
    public void setTrialPeriod(Integer trialPeriod) { this.trialPeriod = trialPeriod; }

    @Override
    public String toString () {
        return "Животное по кличке:" + petName + "взято из приюта:" + adoptionDate
                + "пользователем:" + userId + "период адаптации:" + trialPeriod;
    }
}
