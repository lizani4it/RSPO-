package com.example.petstore.service;

import com.example.petstore.model.Pet;
import com.example.petstore.repository.PetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PetServiceTest {

    @Mock
    private PetRepository petRepository;

    @InjectMocks
    private PetService petService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addPet() {
        Pet pet = new Pet();
        pet.setId(1L);
        when(petRepository.save(pet)).thenReturn(pet);

        Pet savedPet = petService.addPet(pet);
        assertEquals(pet.getId(), savedPet.getId());
    }

    @Test
    void getPetById() {
        Pet pet = new Pet();
        pet.setId(1L);
        when(petRepository.findById(1L)).thenReturn(Optional.of(pet));

        Optional<Pet> foundPet = petService.getPetById(1L);
        assertTrue(foundPet.isPresent());
        assertEquals(pet.getId(), foundPet.get().getId());
    }
}