package com.example.petstore.controller;

import com.example.petstore.model.Pet;
import com.example.petstore.service.PetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PetControllerTest {

    @Mock
    private PetService petService;

    @InjectMocks
    private PetController petController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addPet() {
        Pet pet = new Pet();
        pet.setId(1L);
        when(petService.addPet(pet)).thenReturn(pet);

        ResponseEntity<Pet> response = petController.addPet(pet);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(pet.getId(), response.getBody().getId());
    }

    @Test
    void getPetById() {
        Pet pet = new Pet();
        pet.setId(1L);
        when(petService.getPetById(1L)).thenReturn(Optional.of(pet));

        ResponseEntity<Pet> response = petController.getPetById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(pet.getId(), response.getBody().getId());
    }
}