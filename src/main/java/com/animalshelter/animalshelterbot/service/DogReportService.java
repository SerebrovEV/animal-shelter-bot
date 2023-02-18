package com.animalshelter.animalshelterbot.service;

import com.animalshelter.animalshelterbot.model.AdoptedDog;
import com.animalshelter.animalshelterbot.model.DogReport;
import com.animalshelter.animalshelterbot.organisation.Callbacks;
import com.animalshelter.animalshelterbot.repository.AdoptedDogRepository;
import com.animalshelter.animalshelterbot.repository.DogReportRepository;
import com.animalshelter.animalshelterbot.repository.DogUserRepository;
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
 * Сервис для добавления, получения, редактирования и удаления отчетов усыновителей DogUser
 * в/из базы данных приюта для собак.
 */
@Service
@RequiredArgsConstructor
public class DogReportService {

    private final AdoptedDogRepository adoptedDogRepository;

    private final DogReportRepository dogReportRepository;

    private final DogUserRepository dogUserRepository;

    private final ValidatorReportService validatorReportService;
    private final TelegramBotSender telegramBotSender;

    private final Map<Long, Message> reportTemp = new HashMap<>();
    private final String warningMessage =
            "Дорогой усыновитель, мы заметили, что ты заполняешь отчет не так подробно, как необходимо. " +
                    "Пожалуйста, подойди ответственнее к этому занятию. " +
                    "В противном случае волонтеры приюта будут обязаны самолично проверять условия содержания животного";


    private final String backButtonText = "Назад";
    /**
     * Проверяет, является ли, отправленное в отчете, имя питомца в списке усыновленных собак
     */
    private boolean isAdoptedDogBelongsToUser(Message message) {
        String name;
        name = message.caption().split("\\.")[0];
        AdoptedDog adoptedDog = adoptedDogRepository.findAdoptedDogByDogName(name).orElse(null);
        if (adoptedDog == null) {
            return false;
        }
        if (adoptedDog.getDogUser().equals(dogUserRepository.findDogUserByChatId(message.from().id()).orElse(null))) {
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
        AdoptedDog adoptedDog = adoptedDogRepository.findAdoptedDogByDogName(name).orElse(null);
        DogReport dogReport = createEntity(message);
        dogReport.setAdoptedDog(adoptedDog);
        // Проверяем, был ли за сегодня отчет
        DogReport oldReport = dogReportRepository
                .findDogReportByDateAndAdoptedDog_Id(Date.valueOf(LocalDate.now()), adoptedDog.getId())
                .orElse(null);
        if (oldReport != null) {
            dogReport.setId(oldReport.getId());
        }
        dogReportRepository.save(dogReport);
        reportTemp.remove(callback.from().id());
        return new SendMessage(callback.from().id(), "Ваш отчет отправлен.");
    }

    private DogReport createEntity(Message message) {
        DogReport dogReport = new DogReport();
        dogReport.setDate(Date.valueOf(LocalDate.now()));
        dogReport.setText(message.caption());
        dogReport.setPhoto(Arrays.stream(message.photo()).findFirst().get().fileId());
        return dogReport;
    }

    /**
     * Выводит спиок всех отчетов по собакам
     */
    public SendMessage getAllDogReports(Message message) {
        Collection<DogReport> dogReports = dogReportRepository.findAll();
        sendReports(dogReports, message.from().id());
        return new SendMessage(message.from().id(), "");
    }

    /**
     * Выводит список отчетов за указанный день день
     */
    public SendMessage getDogReportByDay(Message message) {
        Date date = validatorReportService.getDateFromMessage(message);
        Collection<DogReport> dogReports = dogReportRepository.findDogReportByDate(date);
        sendReports(dogReports, message.from().id());
        return new SendMessage(message.from().id(), "");
    }

    /**
     * Удаляет отчет по идентификатору БД
     */
    public SendMessage deleteDogReport(Message message) {
        Long id = validatorReportService.getIdFromMessage(message);
        if (id == null) {
            return new SendMessage(message.from().id(), "Неверный запрос");
        }
        dogReportRepository.deleteById(id);
        return new SendMessage(message.from().id(), "Отчет с id = " + id + " был удален.");
    }

    /**
     * Отправляет фото питомцев из БД с текстовым отчетом
     *
     * @param reports коллекция отчетов
     * @param id      chat_id, кто запрашивал
     */
    private void sendReports(Collection<DogReport> reports, Long id) {
        for (DogReport dogReport : reports
        ) {
            SendPhoto sendPhoto = new SendPhoto(id, dogReport.getPhoto())
                    .caption("dog_report_id = " + dogReport.getId() + ".adopted_dog_id = " + dogReport.getAdoptedDog().getId()
                            + ". Date=" + dogReport.getDate() + ". " + dogReport.getText());

            telegramBotSender.telegramSendPhoto(sendPhoto);
        }
    }

    /**
     * Удаляет из БД всех собак с идентификатором усыновленного питомца
     */
    public SendMessage deleteDogsFromReportByDogId(Message message) {
        Long id = validatorReportService.getIdFromMessage(message);
        if (id == null) {
            return new SendMessage(message.from().id(), "Неверный запрос");
        }
        Collection<DogReport> reports = dogReportRepository.findDogReportsByAdoptedDog_Id(id);
        dogReportRepository.deleteAll(reports);
        return new SendMessage(message.from().id(), "Все отчеты с собакой с id = " + id + " были удалены.");
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
        Collection<AdoptedDog> adoptedDogs = adoptedDogRepository
                .findMissingReportAdoptedDog(date);
        adoptedDogs.forEach(s -> sendMessageList.add(new SendMessage(message.from().id(), s.toString())));
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
        AdoptedDog adoptedDog = adoptedDogRepository.findById(id).orElse(null);
        if (adoptedDog != null) {
            sendMessageList.add(new SendMessage(message.from().id(), "Предупреждение отправлено"));
            sendMessageList.add(new SendMessage(adoptedDog.getDogUser().getChatId(), warningMessage));
            return List.copyOf(sendMessageList);
        }
        sendMessageList.add(new SendMessage(message.from().id(), "Собаки с таким id не найдено"));
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
            if (!isAdoptedDogBelongsToUser(message)) {
                return new SendMessage(message.from().id(), "Мы не нашли усыновленную Вами собаку с таким именем!");
            }
            // Если ранее был запрос на отправку отчета.

            reportTemp.put(message.from().id(), message);
            return new SendMessage(message.from().id(), "Вы хотите отправить отчет?")
                    .replyMarkup(new InlineKeyboardMarkup(
                            new InlineKeyboardButton("Да").callbackData(Callbacks.DOG_ADD_REPORT_YES.name()),
                            new InlineKeyboardButton("Отправлю позже").callbackData(Callbacks.DOG_ADD_REPORT_NO.name())
                    ));
        } else if (!reportTemp.containsKey(message.from().id())) {
            // TODO Если пользователь отправил фото, но не через меню
//            return new SendMessage(message.from().id(), "Если Вы хотите отправить отчет, то сделайте это через меню.");
            return new SendMessage(message.from().id(), "");
        } else {
            return new SendMessage(message.from().id(), "");
        }
    }

    private final String exampleText = "Следуя примеру выше, отправьте фото питомца с подписью к фото, где укажите имя собаки, а после точки рацион питания, " +
            "общее самочувствие и привыкание к новому месту, а также изменение в поведении: отказ от старых привычек, приобретение новых.";
    private final String photoFile = "src/main/resources/images/reportExamplePhoto.jpg";

    /**
     * Метод отправляет образец заполнения отчета
     *
     * @return {@link SendMessage}
     */
    public SendMessage addInquiryDogReport(CallbackQuery callback) {
        reportTemp.put(callback.from().id(), null);
        telegramBotSender.telegramSendPhoto(examplePhoto(photoFile, callback.from().id()));
        return new SendMessage(callback.from().id(), exampleText)
                .replyMarkup(new InlineKeyboardMarkup(new InlineKeyboardButton(backButtonText)
                        .callbackData(Callbacks.DOG_MENU.name())));
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
                .caption("Шарик. В рацион собаки входит сухое питание Hills Science Plan light. Чувствует себя более уверенно, " +
                        "выучил команду \"дай лапу\". Перестал грызть мебель.");
    }

    public SendMessage closeInquiryDogReport(CallbackQuery callback) {
        reportTemp.remove(callback.from().id());
        return new SendMessage(callback.from().id(), "Ваш отчет не был отправлен.")
                .replyMarkup(new InlineKeyboardMarkup(
                        new InlineKeyboardButton("Повторить").callbackData(Callbacks.DOG_REPORT.name()),
                        new InlineKeyboardButton("Назад").callbackData(Callbacks.DOG_MENU.name())
                ));
    }
}
