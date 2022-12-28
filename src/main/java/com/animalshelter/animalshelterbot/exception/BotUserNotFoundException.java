package com.animalshelter.animalshelterbot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * <i>Собственное исключение. Вызывается при отсутвии переданного {@link com.animalshelter.animalshelterbot.model.BotUser}
 * в базе данных</i>
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class BotUserNotFoundException extends RuntimeException{

}
