package com.animalshelter.animalshelterbot.model;

import liquibase.repackaged.net.sf.jsqlparser.expression.operators.relational.EqualsTo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class BotUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long chatId;
    private String userName;
    private Long phoneNumber;

    public BotUser() {

    }

    public BotUser(String userName, long phoneNumber, long chatId) {
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.chatId = chatId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long userId) {
        this.chatId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "Пользователь " + userName + ". Телефон для связи " + phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BotUser botUser = (BotUser) o;
        return Objects.equals(chatId, botUser.chatId) &&
                Objects.equals(userName, botUser.userName) &&
                Objects.equals(phoneNumber, botUser.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId, userName, phoneNumber);
    }
}
