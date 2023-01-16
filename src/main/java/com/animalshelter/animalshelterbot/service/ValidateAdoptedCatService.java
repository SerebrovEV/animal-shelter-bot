package com.animalshelter.animalshelterbot.service;

import com.animalshelter.animalshelterbot.controllers.AdminCatController;
import com.animalshelter.animalshelterbot.model.AdoptedCat;
import com.animalshelter.animalshelterbot.controllers.CatUserController;
import com.animalshelter.animalshelterbot.model.CatUser;
import com.pengrad.telegrambot.model.Message;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
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
    private final CatUserService catUserService;
    private final Logger LOG = LoggerFactory.getLogger(ValidateAdoptedCatService.class);
    private final Pattern ADD_PATTERN = Pattern.compile("([\\W]{9})(\\s)([\\W]{1})(\\s)([\\W]+)");
    private final Pattern FIND_AND_DELETE_PATTERN = Pattern.compile("([\\d]+)");

    private final Pattern EDIT_PATTERN = Pattern.compile("([\\d]+)(\\s)([\\W]+)");
    private final Pattern TAKE_PATTERN = Pattern.compile("([\\W]{9})(\\s)([\\d]+)(\\s)([\\W]{1})(\\s)([\\d]+)");
    private final Pattern RETURN_PATTERN = Pattern.compile("([\\d]+)");
    private final Pattern EXTEND_PATTERN = Pattern.compile("([\\d]+)(\\s)([\\W]{2})(\\s)([\\d]+)");

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

    /**
     * <i> Метод для проверки и обработки входящего сообщения на усыновление кошки от администратора.
     * <br>
     * Запрос выполняется через метод {@link AdminCatController#handleTakeCat(Message)}. </i>
     *
     * @param message
     * @return String в зависимости от результата обработки
     */
    public String validateTakeCat(Message message) {
        Matcher matcher = TAKE_PATTERN.matcher(message.text());
        if (matcher.find()) {
            Long idCat = Long.valueOf(matcher.group(3));
            Optional<AdoptedCat> editCat = adoptedCatService.getAdoptedCat(idCat);
            if (editCat.isEmpty()) {
                return "Кошка не найдена в базе данных приюта для кошек, проверьте правильность введения id.";
            }
            Long idUser = Long.valueOf(matcher.group(7));
            Optional<CatUser> catUser = catUserService.getCatUser(idUser);
            if (catUser.isEmpty()) {
                return "Усыновитель не найден в базе данных приюта для кошек, проверьте правильность введения id.";
            }
            AdoptedCat newCat = editCat.get();
            newCat.setCatUser(catUser.get());
            newCat.setAdoptionDate(Date.valueOf(LocalDate.now()));
            newCat.setTrialPeriod(30);
            adoptedCatService.editAdoptedCat(newCat);
            return newCat.toString();
        }
        return "Некорректный запрос";
    }

    /**
     * <i> Метод для проверки и обработки входящего сообщения на возврат кошки от администратора.
     * <br>
     * Запрос выполняется через метод {@link AdminCatController#handleReturnCat(Message)}. </i>
     *
     * @param message
     * @return String в зависимости от результата обработки
     */
    public String validateReturnCat(Message message) {
        Matcher matcher = RETURN_PATTERN.matcher(message.text());
        if (matcher.find()) {
            Long idCat = Long.valueOf(matcher.group(1));
            Optional<AdoptedCat> editCat = adoptedCatService.getAdoptedCat(idCat);
            if (editCat.isEmpty()) {
                return "Кошка не найдена в базе данных приюта для кошек, проверьте правильность введения id.";
            }
            AdoptedCat newCat = editCat.get();
            newCat.setCatUser(null);
            newCat.setAdoptionDate(null);
            newCat.setTrialPeriod(30);
            adoptedCatService.editAdoptedCat(newCat);
            return newCat + " изменен в базе данных приюта для кошек.";
        }
        return "Некорректный запрос";
    }

    /**
     * <i> Метод для проверки и обработки входящего сообщения на продления периода адаптации кошки от администратора.
     * <br>
     * Запрос выполняется через метод {@link AdminCatController#handleExtendCat(Message)}. </i>
     *
     * @param message
     * @return String в зависимости от результата обработки
     */
    public String validateExtendCat(Message message) {
        Matcher matcher = EXTEND_PATTERN.matcher(message.text());
        if (matcher.find()) {
            Long idCat = Long.valueOf(matcher.group(1));
            Optional<AdoptedCat> editCat = adoptedCatService.getAdoptedCat(idCat);
            if (editCat.isEmpty()) {
                return "Кошка не найдена в базе данных приюта для кошек, проверьте правильность введения id.";
            }
            int newPeriod = Integer.parseInt(matcher.group(5));
            if (newPeriod == 14 || newPeriod == 30) {
                AdoptedCat newCat = editCat.get();
                newCat.setTrialPeriod(editCat.get().getTrialPeriod()+newPeriod);
                adoptedCatService.editAdoptedCat(newCat);
                return newCat + " изменен в базе данных приюта для кошек.";
            }
            return "Некорректный запрос на добавление дней к периоду адаптации, можно добавить либо 14, либо 30 дней.";
        }
        return "Некорректный запрос";
    }
}
