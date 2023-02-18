package com.animalshelter.animalshelterbot.service;

import com.animalshelter.animalshelterbot.model.AdoptedCat;
import com.animalshelter.animalshelterbot.model.AdoptedDog;
import com.animalshelter.animalshelterbot.repository.AdoptedDogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
/**
 * <i>Сервис для добавления, редактирования, получения, удаления,
 *  * получения всех AdoptedDog (собак), а также списка всех не усыновленных собак, питомцев
 *  * находящихся у хозяев с испытательном сроком и списка собак, у которых период адаптации закончился в/из базы
 *  * данных приюта для собак.
 * {@link AdoptedDog} в/из базы данных приюта для собак</i>
 */
@Service
@RequiredArgsConstructor
public class AdoptedDogService {

    private final AdoptedDogRepository adoptedDogRepository;

    public AdoptedDog addAdoptedDog (AdoptedDog adoptedDog){ return adoptedDogRepository.save(adoptedDog); }
    public Optional<AdoptedDog> getAdoptedDog (Long id){
        return adoptedDogRepository.findById(id);
    }
    public AdoptedDog editAdoptedDog(AdoptedDog adoptedDog){ return adoptedDogRepository.save(adoptedDog);}

    public void deleteAdoptedDog(Long id){ adoptedDogRepository.deleteById(id); }

    public List<AdoptedDog> getAllDog() {
        return List.copyOf(adoptedDogRepository.findAll());
    }

    public List<AdoptedDog> getAllFreeDog() {
        return  List.copyOf(adoptedDogRepository.findAllByDogUserIsNull());}

    public List<AdoptedDog> getAllDogOnTrialPeriod() {
        return List.copyOf(adoptedDogRepository.findAllByDogUserIsNotNull()); }

    public List<AdoptedDog> getAllDogWithEndPeriod() {
        return List.copyOf(getAllDogOnTrialPeriod()).stream()
                .filter(s -> !s.getAdoptionDate().toLocalDate().plusDays(30).isAfter(LocalDate.now()))
                .collect(Collectors.toList());
    }

}


