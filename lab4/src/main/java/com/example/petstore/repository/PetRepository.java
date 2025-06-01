package com.example.petstore.repository;

import com.example.petstore.model.Pet;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PetRepository {
    private final List<Pet> pets = new ArrayList<>();

    public List<Pet> findAll() {
        return pets;
    }

    public Optional<Pet> findById(Long id) {
        return pets.stream()
                .filter(pet -> pet.getId() != null && pet.getId().equals(id))
                .findFirst();
    }

    public Pet save(Pet pet) {
        pets.add(pet);
        return pet;
    }

    public void deleteById(Long id) {
        pets.removeIf(pet -> pet.getId().equals(id));
    }

    public Pet update(Pet updatedPet) {
        int index = -1;
        for (int i = 0; i < pets.size(); i++) {
            if (pets.get(i).getId() != null && pets.get(i).getId().equals(updatedPet.getId())) {
                index = i;
                break;
            }
        }

        if (index != -1) {
            pets.set(index, updatedPet);
            return updatedPet;
        } else {
            throw new RuntimeException("Pet with id " + updatedPet.getId() + " not found");
        }
    }
}