package com.animalshelter.animalshelterbot.service;

import com.animalshelter.animalshelterbot.model.Pet;

import java.util.List;
import java.util.Optional;

public interface PetService {

    Pet addPet(Pet pet);

    void deletePet(Long petId);

    <T> Optional<T> getPet(Long idPet);

    Pet editPet(Pet pet);

    <T> List<T> getAllPet();
    <T> List<T> getAllBusyPet();
    <T> List<T> getAllFreePet();
    <T> List<T> getAllPetWithEndPeriod();

}
