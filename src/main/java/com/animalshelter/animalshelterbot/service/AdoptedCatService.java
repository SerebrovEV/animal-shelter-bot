package com.animalshelter.animalshelterbot.service;

import com.animalshelter.animalshelterbot.model.AdoptedCat;
import com.animalshelter.animalshelterbot.repository.AdoptedCatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
/**
 * <i>Сервис для добавления, получения, редактирования и удаления кошек
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
    public AdoptedCat editAdoptedCat(AdoptedCat adoptedCat){
        return adoptedCatRepository.save(adoptedCat);
    }

    public List<AdoptedCat> getAllCat() {
        return List.copyOf(adoptedCatRepository.findAll());
    }
}
