package com.example.lab8.model;

import com.example.lab8.model.value.Address;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // Имя пользователя (логин), используется для связи с Keycloak

    @Embedded
    private Address address; // Адрес - данные приложения
}