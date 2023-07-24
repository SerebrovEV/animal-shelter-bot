package com.animalshelter.animalshelterbot.service.impl;

import com.animalshelter.animalshelterbot.model.AdoptedDog;
import com.animalshelter.animalshelterbot.model.Pet;
import com.animalshelter.animalshelterbot.repository.AdoptedDogRepository;
import com.animalshelter.animalshelterbot.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <i>Сервис для добавления, редактирования, получения, удаления,
 * * получения всех AdoptedDog (собак), а также списка всех не усыновленных собак, питомцев
 * * находящихся у хозяев с испытательном сроком и списка собак, у которых период адаптации закончился в/из базы
 * * данных приюта для собак.
 * {@link AdoptedDog} в/из базы данных приюта для собак</i>
 */
@Service
@RequiredArgsConstructor
public class AdoptedDogService implements PetService {

    private final AdoptedDogRepository adoptedDogRepository;

    @Override
    public AdoptedDog addPet(Pet adoptedDog) {
        return adoptedDogRepository.save((AdoptedDog) adoptedDog);
    }

    @Override
    public Optional<AdoptedDog> getPet(Long id) {
        return adoptedDogRepository.findById(id);
    }

    @Override
    public AdoptedDog editPet(Pet adoptedDog) {
        return adoptedDogRepository.save((AdoptedDog) adoptedDog);
    }

    @Override
    public void deletePet(Long id) {
        adoptedDogRepository.deleteById(id);
    }

    @Override
    public List<AdoptedDog> getAllPet() {
        return List.copyOf(adoptedDogRepository.findAll());
    }

    @Override
    public List<AdoptedDog> getAllFreePet() {
        return List.copyOf(adoptedDogRepository.findAllByDogUserIsNull());
    }

    @Override
    public List<AdoptedDog> getAllBusyPet() {
        return List.copyOf(adoptedDogRepository.findAllByDogUserIsNotNull());
    }

    @Override
    public List<AdoptedDog> getAllPetWithEndPeriod() {
        return List.copyOf(getAllBusyPet()).stream()
                .filter(s -> !s.getAdoptionDate().toLocalDate().plusDays(30).isAfter(LocalDate.now()))
                .collect(Collectors.toList());
    }

}


