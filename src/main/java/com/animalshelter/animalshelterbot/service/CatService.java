package com.animalshelter.animalshelterbot.service;

import com.animalshelter.animalshelterbot.model.AdoptedCat;
import com.animalshelter.animalshelterbot.repository.AdoptedCatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CatService {
    private final AdoptedCatRepository adoptedCatRepository;

    public AdoptedCat addAdoptedCat(AdoptedCat adoptedCat) {
        return adoptedCatRepository.save(adoptedCat);
    }

    public void deleteAdoptedCat(AdoptedCat adoptedCat) {
        adoptedCatRepository.delete(adoptedCat);
    }

    public AdoptedCat getAdoptedCat(Long idAdoptedCat) {
        return adoptedCatRepository.findById(idAdoptedCat).get();
    }
    public AdoptedCat editAdoptedCat(AdoptedCat adoptedCat){
        return adoptedCatRepository.save(adoptedCat);
    }

//    public AdoptedCat editCat(AdoptedCat adoptedCat){
//        AdoptedCat newCat = adoptedCatRepository.findById(adoptedCat.getId()).get();
//
//    }
}
