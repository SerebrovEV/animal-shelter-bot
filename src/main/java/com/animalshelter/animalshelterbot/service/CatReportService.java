package com.animalshelter.animalshelterbot.service;

import com.animalshelter.animalshelterbot.model.AdoptedCat;
import com.animalshelter.animalshelterbot.model.CatReport;
import com.animalshelter.animalshelterbot.organisation.Callbacks;
import com.animalshelter.animalshelterbot.repository.AdoptedCatRepository;
import com.animalshelter.animalshelterbot.repository.CatReportRepository;
import com.animalshelter.animalshelterbot.repository.CatUserRepository;
import com.animalshelter.animalshelterbot.sender.TelegramBotSender;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

/**
 * Сервис для добавления, получения, редактирования и удаления отчетов усыновителей CatUser
 * в/из базы данных приюта для кошек.
 */
@Service
@RequiredArgsConstructor
public class CatReportService {

    private final AdoptedCatRepository adoptedCatRepository;

    private final CatReportRepository catReportRepository;

    private final CatUserRepository catUserRepository;

    private final ValidatorReportService validatorReportService;
    private final TelegramBotSender telegramBotSender;

    private final Map<Long, Message> reportTemp = new HashMap<>();
    private final String warningMessage =
            "Дорогой усыновитель, мы заметили, что ты заполняешь отчет не так подробно, как необходимо. " +
                    "Пожалуйста, подойди ответственнее к этому занятию. " +
                    "В противном случае волонтеры приюта будут обязаны самолично проверять условия содержания животного";

    private final String backButtonText = "Назад";
    /**
     * Проверяет, является ли, отправленное в отчете, имя питомца в списке усыновленных котов
     */
    private boolean isAdoptedCatBelongsToUser(Message message) {
        String name;
        name = message.caption().split("\\.")[0];
        AdoptedCat adoptedCat = adoptedCatRepository.findAdoptedCatByCatName(name).orElse(null);
        if (adoptedCat == null) {
            return false;
        }
        if (adoptedCat.getCatUser().equals(catUserRepository.findCatUserByChatId(message.from().id()).orElse(null))) {
            return true;
        }
        return false;
    }

    /**
     * Записывает в БД отчет о животном. Если за сегодняшний день, несколько отчетов по одному и тому же животному, то
     * запись перезаписывается.
     */
    public SendMessage addReport(CallbackQuery callback) {
        Message message = reportTemp.get(callback.from().id());
        String name = message.caption().split("\\.")[0];
        AdoptedCat adoptedCat = adoptedCatRepository.findAdoptedCatByCatName(name).orElse(null);
        CatReport catReport = createEntity(message);
        catReport.setAdoptedCat(adoptedCat);
        // Проверяем, был ли за сегодня отчет
        CatReport oldReport = catReportRepository
                .findCatReportByDateAndAdoptedCat_Id(Date.valueOf(LocalDate.now()), adoptedCat.getId())
                .orElse(null);
        if (oldReport != null) {
            catReport.setId(oldReport.getId());
        }
        catReportRepository.save(catReport);
        reportTemp.remove(callback.from().id());
        return new SendMessage(callback.from().id(), "Ваш отчет отправлен.");
    }

    private CatReport createEntity(Message message) {
        CatReport catReport = new CatReport();
        catReport.setDate(Date.valueOf(LocalDate.now()));
        catReport.setText(message.caption());
        catReport.setPhoto(Arrays.stream(message.photo()).findFirst().get().fileId());
        return catReport;
    }

    /**
     * Выводит спиок всех отчетов по кошкам
     */
    public SendMessage getAllCatReports(Message message) {
        Collection<CatReport> catReports = catReportRepository.findAll();
        sendReports(catReports, message.from().id());
        return new SendMessage(message.from().id(), "");
    }

    /**
     * Выводит список отчетов за указанный день день
     */
    public SendMessage getCatReportByDay(Message message) {
        Date date = validatorReportService.getDateFromMessage(message);
        Collection<CatReport> catReports = catReportRepository.findCatReportByDate(date);
        sendReports(catReports, message.from().id());
        return new SendMessage(message.from().id(), "");
    }

    /**
     * Удаляет отчет по идентификатору БД
     */
    public SendMessage deleteCatReport(Message message) {
        Long id = validatorReportService.getIdFromMessage(message);
        if (id == null) {
            return new SendMessage(message.from().id(), "Неверный запрос");
        }
        catReportRepository.deleteById(id);
        return new SendMessage(message.from().id(), "Отчет с id = " + id + " был удален.");
    }

    /**
     * Отправляет фото питомцев из БД с текстовым отчетом
     *
     * @param reports коллекция отчетов
     * @param id      chat_id, кто запрашивал
     */
    private void sendReports(Collection<CatReport> reports, Long id) {
        for (CatReport catReport : reports
        ) {
            SendPhoto sendPhoto = new SendPhoto(id, catReport.getPhoto())
                    .caption("cat_report_id = " + catReport.getId() + ".adopted_cat_id = " + catReport.getAdoptedCat().getId()
                            + ". Date=" + catReport.getDate() + ". " + catReport.getText());

            telegramBotSender.telegramSendPhoto(sendPhoto);
        }
    }

    /**
     * Удаляет из БД всех кошек с идентификатором усыновленного питомца
     */
    public SendMessage deleteCatsFromReportByCatId(Message message) {
        Long id = validatorReportService.getIdFromMessage(message);
        if (id == null) {
            return new SendMessage(message.from().id(), "Неверный запрос");
        }
        Collection<CatReport> reports = catReportRepository.findCatReportsByAdoptedCat_Id(id);
        catReportRepository.deleteAll(reports);
        return new SendMessage(message.from().id(), "Все отчеты с кошкой с id = " + id + " были удалены.");
    }

    /**
     * Выводит списки питомцев, для которых на указанную дату отстутствуют отчеты
     */
    public List<SendMessage> getMissingReports(Message message) {
        Date date = validatorReportService.getDateFromMessage(message);
        List<SendMessage> sendMessageList = new ArrayList<>();
        if (date == null) {
            sendMessageList.add(new SendMessage(message.from().id(), "Неверный запрос даты! Отправьте в формате yyyy-mm-dd"));
            return List.copyOf(sendMessageList);
        }
        Collection<AdoptedCat> adoptedCats = adoptedCatRepository
                .findMissingReportAdoptedCat(date);
        adoptedCats.forEach(s -> sendMessageList.add(new SendMessage(message.from().id(), s.toString())));
        return List.copyOf(sendMessageList);
    }

    /**
     * Отправляет предупреждение хозяину питомца, что нужно вести отчеты.
     *
     * @return Предупредительное сообщение
     */
    public List<SendMessage> sendWarning(Message message) {
        Long id = validatorReportService.getIdFromMessage(message);
        List<SendMessage> sendMessageList = new ArrayList<>();
        if (id == null) {
            sendMessageList.add(new SendMessage(message.from().id(), "Неверный запрос"));
            return List.copyOf(sendMessageList);
        }
        AdoptedCat adoptedCat = adoptedCatRepository.findById(id).orElse(null);
        if (adoptedCat != null) {
            sendMessageList.add(new SendMessage(message.from().id(), "Предупреждение отправлено"));
            sendMessageList.add(new SendMessage(adoptedCat.getCatUser().getChatId(), warningMessage));
            return List.copyOf(sendMessageList);
        }
        sendMessageList.add(new SendMessage(message.from().id(), "Кошки с таким id не найдено"));
        return sendMessageList;
    }

    /**
     * Метод для проверки входящего фото на наличие подписи и соответствия усыновленного питомца.
     *
     * @return {@link SendMessage}
     */
    public SendMessage validateReport(Message message) {
        if (message.photo() != null & reportTemp.containsKey(message.from().id())) {

            if (message.caption() == null) {
                return new SendMessage(message.from().id(), "Добавьте к фото пояснительное сообщение и повторите отправку фото!");
            }
            if (!isAdoptedCatBelongsToUser(message)) {
                return new SendMessage(message.from().id(), "Мы не нашли усыновленную Вами кошку с таким именем!");
            }
            // Если ранее был запрос на отправку отчета.

            reportTemp.put(message.from().id(), message);
            return new SendMessage(message.from().id(), "Вы хотите отправить отчет?")
                    .replyMarkup(new InlineKeyboardMarkup(
                            new InlineKeyboardButton("Да").callbackData(Callbacks.CAT_ADD_REPORT_YES.name()),
                            new InlineKeyboardButton("Отправлю позже").callbackData(Callbacks.CAT_ADD_REPORT_NO.name())
                    ));
        } else if (!reportTemp.containsKey(message.from().id())) {
            // TODO Если пользователь отправил фото, но не через меню
            //return new SendMessage(message.from().id(), "Если Вы хотите отправить отчет, то сделайте это через меню.");
            return new SendMessage(message.from().id(), "");
        } else {
            return new SendMessage(message.from().id(), "");
        }
    }

    private final String exampleText = "Следуя примеру выше, отправьте фото питомца с подписью к фото, где укажите имя кошки, а после точки рацион питания, " +
            "общее самочувствие и привыкание к новому месту, а также изменение в поведении: отказ от старых привычек, приобретение новых.";
    private final String photoFile = "src/main/resources/images/reportExamplePhoto.jpg";

    /**
     * Метод отправляет образец заполнения отчета
     *
     * @return {@link SendMessage}
     */
    public SendMessage addInquiryCatReport(CallbackQuery callback) {
        reportTemp.put(callback.from().id(), null);
        telegramBotSender.telegramSendPhoto(examplePhoto(photoFile, callback.from().id()));
        return new SendMessage(callback.from().id(), exampleText)
                .replyMarkup(new InlineKeyboardMarkup(new InlineKeyboardButton(backButtonText)
                        .callbackData(Callbacks.CAT_MENU.name())));
    }

    /**
     * Метод для загрузки примера фото отчета
     *
     * @param photoPathFile путь к файлу с примером фото
     * @param id            chat_id
     * @return {@link SendMessage}
     */
    private SendPhoto examplePhoto(String photoPathFile, Long id) {
        return new SendPhoto(id, new File(photoPathFile))
                .caption("Маруся. В рацион кошки входит сухое питание Hills Premium. Чувствует себя более уверенно, стала играть игрушками и выходить в другие комнаты.");
    }

    public SendMessage closeInquiryCatReport(CallbackQuery callback) {
        reportTemp.remove(callback.from().id());
        return new SendMessage(callback.from().id(), "Ваш отчет не был отправлен.")
                .replyMarkup(new InlineKeyboardMarkup(
                        new InlineKeyboardButton("Повторить").callbackData(Callbacks.CAT_REPORT.name()),
                        new InlineKeyboardButton("Назад").callbackData(Callbacks.CAT_MENU.name())
                ));
    }
}
