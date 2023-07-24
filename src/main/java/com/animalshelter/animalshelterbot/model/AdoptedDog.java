package com.animalshelter.animalshelterbot.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.util.Collection;
import java.util.Optional;

/**
 * Модель для собак из приюта для собак.
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@Entity
public class AdoptedDog extends Pet{
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
        public AdoptedDog(String dogName, Date adoptionDate, Integer trialPeriod){
            this.dogName = dogName;
            this.adoptionDate = adoptionDate;
            this.trialPeriod = trialPeriod;
        }

        public AdoptedDog(String dogName) {
            this.dogName = dogName;
        }

        @Override
            public String toString () {
            return "Собака: ID: "+ id + ", имя: " + dogName + ", взята из приюта: " + adoptionDate
            + ", период адаптации: " + trialPeriod + " дней, " + validateDogUser();
            }
        private String validateDogUser() {
            Optional<DogUser> isDogUser = Optional.ofNullable(this.dogUser);
            if (isDogUser.isEmpty()) {
                return "без усыновителя\n";
            } else {
                return isDogUser.get().toString();
            }
    }
}

