package com.animalshelter.animalshelterbot.service;

import com.pengrad.telegrambot.model.Message;

import java.util.regex.Pattern;

public interface ValidatePetService {

    Pattern ADD_PATTERN = Pattern.compile("([\\W]{9})(\\s)([\\W]{1})(\\s)([\\W]+)");
    Pattern FIND_AND_DELETE_PATTERN = Pattern.compile("([\\d]+)");
    Pattern EDIT_PATTERN = Pattern.compile("([\\d]+)(\\s)([\\W]+)");
    Pattern TAKE_PATTERN = Pattern.compile("([\\W]{9})(\\s)([\\d]+)(\\s)([\\W]{1})(\\s)([\\d]+)");
    Pattern RETURN_PATTERN = Pattern.compile("([\\d]+)");
    Pattern EXTEND_PATTERN = Pattern.compile("([\\d]+)(\\s)([\\W]{2})(\\s)([\\d]+)");
    String ATTENTION_MESSAGE = "Добрый день! Вам было назначено дополнительное время испытательного срока," +
            " новых дней: + ";


    String validateAddPet(Message message);
    String validateDeletePet(Message message);
    String validateGetPet(Message message);
    String validateEditPet(Message message);
    String validateTakePet(Message message);
    String validateReturnPet(Message message);
    String validateExtendPet(Message message);


}
