package com.example.petstore.model;

import lombok.Data;
import java.util.List;

@Data
public class Pet {
    private Long id;
    private String name;
    private Long age;
}