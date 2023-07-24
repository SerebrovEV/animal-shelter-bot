package com.animalshelter.animalshelterbot.service.impl;

import com.animalshelter.animalshelterbot.model.AdoptedCat;
import com.animalshelter.animalshelterbot.model.Pet;
import com.animalshelter.animalshelterbot.repository.AdoptedCatRepository;
import com.animalshelter.animalshelterbot.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
public class AdoptedCatService implements PetService {
    private final AdoptedCatRepository adoptedCatRepository;

    @Override
    public AdoptedCat addPet(Pet adoptedCat) {
        return adoptedCatRepository.save((AdoptedCat) adoptedCat);
    }

    @Override
    public void deletePet(Long idAdoptedCat) {
        adoptedCatRepository.deleteById(idAdoptedCat);
    }

    @Override
    public Optional<AdoptedCat> getPet(Long idAdoptedCat) {
        return adoptedCatRepository.findById(idAdoptedCat);
    }

    @Override
    public AdoptedCat editPet(Pet adoptedCat) {
        return adoptedCatRepository.save((AdoptedCat) adoptedCat);
    }

    @Override
    public List<AdoptedCat> getAllPet() {
        return List.copyOf(adoptedCatRepository.findAll());
    }

    public List<AdoptedCat> getAllBusyPet() {
        return List.copyOf(adoptedCatRepository.findAllByCatUserNotNull());
    }

    public List<AdoptedCat> getAllFreePet() {
        return List.copyOf(adoptedCatRepository.findAllByCatUserIsNull());
    }

    public List<AdoptedCat> getAllPetWithEndPeriod() {

        return List.copyOf(getAllBusyPet()).stream()
                .filter(s -> !s.getAdoptionDate().toLocalDate().plusDays(30).isAfter(LocalDate.now()))
                .collect(Collectors.toList());
}
}
