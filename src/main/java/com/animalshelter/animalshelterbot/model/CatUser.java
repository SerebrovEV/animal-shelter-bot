package com.animalshelter.animalshelterbot.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

/**
 * Модель для усыновителей из приюта для кошек.
 */
@Entity
@Setter
@Getter
public class CatUser extends PetUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long chatId;
    private String userName;
    private Long phoneNumber;
    @OneToMany(mappedBy = "catUser")
    private Collection<AdoptedCat> adoptedCats;

    public CatUser() {
    }

    public CatUser(String userName, long phoneNumber, long chatId) {
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.chatId = chatId;
    }

    public CatUser(String userName, long phoneNumber) {
        this.userName = userName;
        this.phoneNumber = phoneNumber;
    }


    @Override
    public String toString() {
        return "Усыновитель: " +
                "id = " + id +
                ", Имя = '" + userName + '\'' +
                ", Номер телефона = " + phoneNumber + ".\n";
    }

    public String toStringUser(){
        return "Пользователь " + userName + ". Телефон для связи " + phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CatUser dogUser = (CatUser) o;
        return Objects.equals(chatId, dogUser.chatId) &&
                Objects.equals(userName, dogUser.userName) &&
                Objects.equals(phoneNumber, dogUser.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userName, phoneNumber);
    }
}
