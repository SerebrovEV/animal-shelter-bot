package com.animalshelter.animalshelterbot.model;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
@RequiredArgsConstructor
@Entity
public class BotUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String userName;
    private Long phoneNumber;

    public BotUser() {

    }

    public BotUser(String userName, long phoneNumber, long userId) {
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.userId = userId;
    }


    @Override
    public String toString() {
        return "Пользователь " + userName + ". Телефон для связи " + phoneNumber;
    }
}
