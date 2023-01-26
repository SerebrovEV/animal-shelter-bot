package com.animalshelter.animalshelterbot.controller;

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

/**
 * Контроллер для работы с отчетами приюта для кошек.
 */
@Component
@RequiredArgsConstructor
public class CatReportController implements CommandController {

    private final CatReportService catReportService;

    private final String photoPattern = "(?U)(\\w+)(\\.)(.+)";

    private final String getAllCatReports = "/getCatReports";

    private final String getCatDateReport = "/getCatDayReport (\\d{4}-\\d{2}-\\d{2})";
    private final String deleteCatFromReportByCatId = "/deleteCatsFromReportById ([\\d]+)";
    private final String deleteCatFromReportById = "/deleteCatReportId ([\\d]+)";
    private final String getCatsFromBadUsers = "/getCatBadUsers (\\d{4}-\\d{2}-\\d{2})";
    private final String sendWarningToUser = "/sendCatWarning (\\d+)";
    private final String getHelp = "/catReportHelp";
    private final String helpMessage = "*/getCatReports* - выводит все отчеты из БД\n" +
            "*/getCatDayReport 2023-01-12* - выводит отчеты из БД за указанную дату\n" +
            "*/deleteCatsFromReportById 1* - удаляет из БД всех кошек по id = 1(adopted_cat)\n" +
            "*/deleteCatReportId 1* - удаляет из БД запись с id = 1\n" +
            "*/getCatBadUsers 2023-01-12* - выводит список кошек, у которых нет записи в БД на дату\n" +
            "*/sendCatWarning 1* - отправляет предупреждение владельцу по id кошки(adopted_cat)\n";


    @Command(name = getHelp)
    public SendMessage getHelp(Message message) {
        return new SendMessage(message.from().id(), helpMessage).parseMode(ParseMode.Markdown);
    }

    /**
     * Метод "запоминает" намерения пользователя добавить отчет.
     *
     * @return Пример отчета
     */
    @com.animalshelter.animalshelterbot.handler.Callback(name = Callbacks.CAT_REPORT)
    public SendMessage addReportCatCallback(CallbackQuery callback) {
        return catReportService.addInquiryCatReport(callback);
    }

    /**
     * Метод для удаления пользователя из списков на отправку отчета.
     *
     * @return {@link SendMessage}
     */
    @com.animalshelter.animalshelterbot.handler.Callback(name = Callbacks.CAT_ADD_REPORT_NO)
    public SendMessage deleteUserFromTempReport(CallbackQuery callback) {
        return catReportService.closeInquiryCatReport(callback);
    }

    /**
     * Метод на добавление отчета в БД
     *
     * @return {@link SendMessage}
     */
    @com.animalshelter.animalshelterbot.handler.Callback(name = Callbacks.CAT_ADD_REPORT_YES)
    public SendMessage createReport(CallbackQuery callback) {
        return catReportService.addReport(callback);
    }

    /**
     * Метод принимает сообщения к фото
     *
     * @return {@link SendMessage}
     */
    @Command(pattern = photoPattern)
    public SendMessage cacheReport(Message message) {
        return catReportService.validateReport(message);
    }

    /**
     * Метод на получение всех записей из БД отчетов по кошкам
     *
     * @return {@link SendMessage}
     */
    @Command(name = getAllCatReports)
    public SendMessage getReportByToday(Message message) {
        return catReportService.getAllCatReports(message);
    }

    /**
     * Метод на получение всех записей из БД отчетов по кошкам по указанной дате
     *
     * @return {@link SendMessage}
     */
    @Command(pattern = getCatDateReport)
    public SendMessage getReportByDay(Message message) {
        return catReportService.getCatReportByDay(message);
    }

    /**
     * Удаляет всех кошек из БД отчетов по идентификатору adopted_cat_id.
     *
     * @return {@link SendMessage}
     */
    @Command(pattern = deleteCatFromReportByCatId)
    public SendMessage deleteCatsFromReportById(Message message) {
        return catReportService.deleteCatsFromReportByCatId(message);
    }

    /**
     * Удаляет запись из БД отчетов.
     *
     * @return {@link SendMessage}
     */
    @Command(pattern = deleteCatFromReportById)
    public SendMessage deleteCatReport(Message message) {
        return catReportService.deleteCatReport(message);
    }

    /**
     * Выводит список питомцев, которые не имеют отчетов в указанную дату.
     *
     * @return Список питомцев, не имеющих отчет
     */
    @Command(pattern = getCatsFromBadUsers)
    public List<SendMessage> getCatsWithoutReport(Message message) {
        return catReportService.getMissingReports(message);
    }

    /**
     * Отправляет предупреждение хозяину.
     *
     * @param message хранит id усыновленного питомца
     * @return Список {@link SendMessage}
     */
    @Command(pattern = sendWarningToUser)
    public List<SendMessage> sendWarningToUser(Message message) {
        return catReportService.sendWarning(message);
    }
}
