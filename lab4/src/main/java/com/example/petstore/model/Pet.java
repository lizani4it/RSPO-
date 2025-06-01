package com.example.petstore.model;

import lombok.Data;
import java.util.List;

@Data
public class Pet {
    private Long id;
    private String name;
    private Category category;
    private List<Tag> tags;
    private String status;
}