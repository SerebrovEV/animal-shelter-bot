package com.animalshelter.animalshelterbot.service;

import com.animalshelter.animalshelterbot.model.AdoptedCat;
import com.animalshelter.animalshelterbot.repository.AdoptedCatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <i>Сервис для добавления, редактирования, получения, удаления,
 *  * получения всех AdoptedCat (кошек), а также списка всех не усыновленных кошек, питомцев
 *  * находящихся у хозяев с испытательном сроком и списка кошек, у которых период адаптации закончился в/из базы
 *  * данных приюта для кошек.
 * {@link AdoptedCat} в/из базы данных приюта для кошек</i>
 */

@Service
@RequiredArgsConstructor
public class AdoptedCatService {
    private final AdoptedCatRepository adoptedCatRepository;

    public AdoptedCat addAdoptedCat(AdoptedCat adoptedCat) {
        return adoptedCatRepository.save(adoptedCat);
    }

    public void deleteAdoptedCat(Long idAdoptedCat) {
        adoptedCatRepository.deleteById(idAdoptedCat);
    }

    public Optional<AdoptedCat> getAdoptedCat(Long idAdoptedCat) {
        return adoptedCatRepository.findById(idAdoptedCat);
    }

    public AdoptedCat editAdoptedCat(AdoptedCat adoptedCat) {
        return adoptedCatRepository.save(adoptedCat);
    }

    public List<AdoptedCat> getAllCat() {
        return List.copyOf(adoptedCatRepository.findAll());
    }

    public List<AdoptedCat> getAllBusyCat() {
        return List.copyOf(adoptedCatRepository.findAllByCatUserNotNull());
    }

    public List<AdoptedCat> getAllFreeCat() {
        return List.copyOf(adoptedCatRepository.findAllByCatUserIsNull());
    }

    public List<AdoptedCat> getAllCatWithEndPeriod() {

        return List.copyOf(getAllBusyCat()).stream()
                .filter(s -> !s.getAdoptionDate().toLocalDate().plusDays(30).isAfter(LocalDate.now()))
                .collect(Collectors.toList());
}
}
