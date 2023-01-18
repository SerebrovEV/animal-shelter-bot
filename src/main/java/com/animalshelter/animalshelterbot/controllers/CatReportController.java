package com.animalshelter.animalshelterbot.controllers;

import com.animalshelter.animalshelterbot.handler.Callback;
import com.animalshelter.animalshelterbot.handler.Command;
import com.animalshelter.animalshelterbot.handler.CommandController;
import com.animalshelter.animalshelterbot.organisation.Callbacks;
import com.animalshelter.animalshelterbot.service.CatReportService;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CatReportController implements CommandController {

    private final CatReportService catReportService;

    private final String PHOTO_PATTERN = "(?U)(\\w+)(\\.)(.+)";

    private final String GET_ALL_CAT_REPORTS = "/getCatReports";

    private final String GET_CAT_DATE_REPORT = "/getCatDayReport (\\d{4}-\\d{2}-\\d{2})";
    private final String DELETE_CAT_FROM_REPORT_BY_CAT_ID = "/deleteCatsFromReportById ([\\d]+)";
    private final String DELETE_CAT_FROM_REPORT_BY_ID = "/deleteCatReportId ([\\d]+)";
    private final String GET_CATS_FROM_BAD_USERS = "/getBadUsers (\\d{4}-\\d{2}-\\d{2})";
    private final String SEND_WARNING_TO_USER = "/sendWarning (\\d+)";
    private final String GET_HELP = "/catReportHelp";
    private final String HELP_MESSAGE = "*/getCatReports* - выводит все отчеты из БД\n" +
            "*/getCatDayReport 2021-01-12* - выводит отчеты из БД за указанную дату\n" +
            "*/deleteCatsFromReportById 1* - удаляет из БД всех кошек по id = 1(adopted_cat)\n" +
            "*/deleteCatReportId 1* - удаляет из БД запись с id = 1\n" +
            "*/getBadUsers 2021-01-12* - выводит список кошек, у которых нет записи в БД на дату\n" +
            "*/sendWarning 1* - отправляет предупреждение владельцу по id кошки(adopted_cat)\n";


    @Command(name = GET_HELP)
    public SendMessage getHelp(Message message) {
        return new SendMessage(message.from().id(), HELP_MESSAGE).parseMode(ParseMode.Markdown);
    }

    /**
     * Метод "запоминает" намерения пользователя добавить отчет.
     *
     * @return Пример отчета
     */
    @Callback(name = Callbacks.CAT_REPORT)
    public SendMessage addReportCatCallback(CallbackQuery callback) {
        return catReportService.addInquiryCatReport(callback);
    }

    /**
     * Метод для удаления пользователя из списков на отправку отчета.
     *
     * @return {@link SendMessage}
     */
    @Callback(name = Callbacks.CAT_ADD_REPORT_NO)
    public SendMessage deleteUserFromTempReport(CallbackQuery callback) {
        return catReportService.closeInquiryCatReport(callback);
    }

    /**
     * Метод на добавление отчета в БД
     *
     * @return {@link SendMessage}
     */
    @Callback(name = Callbacks.CAT_ADD_REPORT_YES)
    public SendMessage createReport(CallbackQuery callback) {
        return catReportService.addReport(callback);
    }

    /**
     * Метод принимает сообщения к фото
     *
     * @return {@link SendMessage}
     */
    @Command(pattern = PHOTO_PATTERN)
    public SendMessage cacheReport(Message message) {
        return catReportService.validateReport(message);
    }

    /**
     * Метод на получение всех записей из БД отчетов по кошкам
     *
     * @return {@link SendMessage}
     */
    @Command(name = GET_ALL_CAT_REPORTS)
    public SendMessage getReportByToday(Message message) {
        return catReportService.getAllCatReports(message);
    }

    /**
     * Метод на получение всех записей из БД отчетов по кошкам по указанной дате
     *
     * @return {@link SendMessage}
     */
    @Command(pattern = GET_CAT_DATE_REPORT)
    public SendMessage getReportByDay(Message message) {
        return catReportService.getCatReportByDay(message);
    }

    /**
     * Удаляет всех кошек из БД отчетов по идентификатору adopted_cat_id.
     *
     * @return {@link SendMessage}
     */
    @Command(pattern = DELETE_CAT_FROM_REPORT_BY_CAT_ID)
    public SendMessage deleteCatsFromReportById(Message message) {
        return catReportService.deleteCatsFromReportByCatId(message);
    }

    /**
     * Удаляет запись из БД отчетов.
     *
     * @return {@link SendMessage}
     */
    @Command(pattern = DELETE_CAT_FROM_REPORT_BY_ID)
    public SendMessage deleteCatReport(Message message) {
        return catReportService.deleteCatReport(message);
    }

    /**
     * Выводит список питомце, которые не имеют отчетов в указанную дату.
     *
     * @return Список питомцев, не имеющих отчет
     */
    @Command(pattern = GET_CATS_FROM_BAD_USERS)
    public List<SendMessage> getCatsWithoutReport(Message message) {
        return catReportService.getMissingReports(message);
    }

    /**
     * Отправляет предупреждение хозяину.
     *
     * @param message хранит id усыновленного питомца
     * @return Список {@link SendMessage}
     */
    @Command(pattern = SEND_WARNING_TO_USER)
    public List<SendMessage> sendWarningToUser(Message message) {
        return catReportService.sendWarning(message);
    }
}
