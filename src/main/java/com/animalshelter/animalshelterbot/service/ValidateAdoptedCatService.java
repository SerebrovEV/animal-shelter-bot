package com.animalshelter.animalshelterbot.service;

import com.animalshelter.animalshelterbot.controllers.AdminCatController;
import com.animalshelter.animalshelterbot.model.AdoptedCat;
import com.animalshelter.animalshelterbot.controllers.CatUserController;
import com.pengrad.telegrambot.model.Message;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <i> Сервис для обработки входящих сообщений с контроллеров
 * {@link CatUserController} и  из телеграма
 * и подготовки ответного сообщения пользователю</i>
 */
@Service
@RequiredArgsConstructor
public class ValidateAdoptedCatService {
    private final AdoptedCatService adoptedCatService;
    private final Logger LOG = LoggerFactory.getLogger(ValidateAdoptedCatService.class);
    private final Pattern ADD_PATTERN = Pattern.compile("([\\W]{9})(\\s)([\\W]{1})(\\s)([\\W]+)");
    private final Pattern FIND_AND_DELETE_PATTERN = Pattern.compile("([\\d]+)");

    private final Pattern EDIT_PATTERN = Pattern.compile("([\\d]+)(\\s)([\\W]+)");

    /**
     * <i> Метод для проверки и обработки входящего сообщения на сохранение кошек от администратора.
     * <br>
     * Запрос выполняется через метод {@link AdminCatController#handleCreateCat(Message)}. </i>
     *
     * @param message
     * @return String в зависимости от результата обработки
     */
    public String validateAddCat(Message message) {
        Matcher matcher = ADD_PATTERN.matcher(message.text());
        if (matcher.find()) {
            String name = matcher.group(5);
            AdoptedCat cat = adoptedCatService.addAdoptedCat(new AdoptedCat(name));
            return "Добавлена запись кота в базу данных приюта для кошек: " + cat.getCatName();
        }
        return "Некорректный запрос";
    }

    /**
     * <i> Метод для проверки и обработки входящего сообщения на удаление кошек от администратора.
     * <br>
     * Запрос выполняется через метод {@link AdminCatController#handleDeleteCat(Message)}. </i>
     *
     * @param message
     * @return String в зависимости от результата обработки
     */
    public String validateDeleteCat(Message message) {
        Matcher matcher = FIND_AND_DELETE_PATTERN.matcher(message.text());
        if (matcher.find()) {
            Long idCat = Long.valueOf(matcher.group(1));
            Optional<AdoptedCat> deleteCat = adoptedCatService.getAdoptedCat(idCat);
            if (deleteCat.isEmpty()) {
                return "Кошка не найдена в базе данных приюта для кошек, проверьте правильность введения id.";
            }
            adoptedCatService.deleteAdoptedCat(idCat);
            return deleteCat.get() + " удалена из базы данных приюта для кошек.";
        }
        return "Некорректный запрос";
    }

    /**
     * <i> Метод для проверки и обработки входящего сообщения на поиск кошек от администратора.
     * <br>
     * Запрос выполняется через метод {@link AdminCatController#handleGetCat(Message)}. </i>
     *
     * @param message
     * @return String в зависимости от результата обработки
     */
    public String validateGetCat(Message message) {
        Matcher matcher = FIND_AND_DELETE_PATTERN.matcher(message.text());
        if (matcher.find()) {
            Long id = Long.valueOf(matcher.group(1));
            Optional<AdoptedCat> findCat = adoptedCatService.getAdoptedCat(id);
            if (findCat.isEmpty()) {
                return "Кошка не найдена в базе данных приюта для кошек, проверьте правильность введения id.";
            }
            return findCat.get() + " из базы данных приюта для кошек.";
        }
        return "Некорректный запрос";
    }

    /**
     * <i> Метод для проверки и обработки входящего сообщения на изменение кошек от администратора.
     * <br>
     * Запрос выполняется через метод {@link AdminCatController#handleEditCat(Message)}. </i>
     *
     * @param message
     * @return String в зависимости от результата обработки
     */
    public String validateEditCat(Message message) {
        Matcher matcher = EDIT_PATTERN.matcher(message.text());
        if (matcher.find()) {
            Long id = Long.valueOf(matcher.group(1));
            Optional<AdoptedCat> editCat = adoptedCatService.getAdoptedCat(id);
            if (editCat.isEmpty()) {
                return "Кошка не найдена в базе данных приюта для кошек, проверьте правильность введения id.";
            }
            AdoptedCat newCat = editCat.get();
            newCat.setCatName(matcher.group(3));
            adoptedCatService.editAdoptedCat(newCat);
            return editCat.get() + " изменен в базе данных приюта для кошек.";
        }
        return "Некорректный запрос";
    }
}
