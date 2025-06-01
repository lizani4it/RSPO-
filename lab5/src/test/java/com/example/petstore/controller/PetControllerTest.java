package com.example.petstore.controller;

import com.example.petstore.model.Pet;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Sql(scripts = "/scripts/insert-pet.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "/scripts/cleanup-pet.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
public class PetControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String PET_URL = "/pet";

    @Test
    public void testAddPet() {
        Pet newPet = new Pet("Buddy");

        ResponseEntity<Pet> response = restTemplate.postForEntity(PET_URL, newPet, Pet.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Buddy");
    }

    @Test
    public void testGetPetById() {
        ResponseEntity<Pet> response = restTemplate.getForEntity(PET_URL + "/1", Pet.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(1L);
    }

    @Test
    public void testGetAllPets() {
        ResponseEntity<List> response = restTemplate.getForEntity(PET_URL, List.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isGreaterThan(0);
    }

    @Test
    public void testDeletePet() {
        ResponseEntity<Void> response = restTemplate
                .exchange(PET_URL + "/1", HttpMethod.DELETE, null, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<Pet> getResponse = restTemplate.getForEntity(PET_URL + "/1", Pet.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}