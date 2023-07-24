package com.animalshelter.animalshelterbot.service;

import com.pengrad.telegrambot.model.Message;

import java.util.regex.Pattern;

public interface ValidateUserService {

    Pattern ADD_PATTERN = Pattern.compile("([\\d]{11})(\\s)([\\W]+)");
    Pattern NUMBER_PATTERN = Pattern.compile("([\\d]+)");
    Pattern EDIT_PATTERN = Pattern.compile("([\\d]+)(\\s)([\\d]{11})(\\s)([\\W]+)");
    String CONGRATULATION_MESSAGE = "Поздравляем, вы прошли испытательный срок. Продолжайте и" +
            " впредь заботится о своем новом любимце и он ответит вам любовью ответ:)";

    String RETURN_MESSAGE = "К сожалению, вы не прошли испытательный срок. Вам требуется вернуть животное " +
            "в приют. Если вы не можете к нам приехать, мы можем направить к вам волонтера для возврата животного. Для " +
            "этого свяжитесь с нами.";

    String validateUser(Message message);
    String validateUserIdChat(Message message);
    String validateUserFromAdmin(Message message);
    String validateGetUserFromAdmin(Message message);
    String validateDeleteUserFromAdmin(Message message);
    String validateEditUserFromAdmin(Message message);
    String validateCongratulationUserFromAdmin(Message message);
    String validateReturnUserFromAdmin(Message message);

}
