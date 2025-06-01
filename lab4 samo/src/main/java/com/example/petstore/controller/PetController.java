package com.example.petstore.controller;

import com.example.petstore.model.Pet;
import com.example.petstore.service.PetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/person")
public class PetController {
    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @PostMapping
    public ResponseEntity<Pet> addPet(@RequestBody Pet pet) {
        Pet savedPet = petService.addPet(pet);
        return new ResponseEntity<>(savedPet, HttpStatus.CREATED);
    }

    @GetMapping("/{petId}")
    public ResponseEntity<Pet> getPetById(@PathVariable Long petId) {
        Optional<Pet> pet = petService.getPetById(petId);
        return pet.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}