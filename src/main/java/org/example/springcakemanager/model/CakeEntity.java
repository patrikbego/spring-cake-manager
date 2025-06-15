package org.example.springcakemanager.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cakes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CakeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @JsonProperty("desc")
    private String description;

    private String image;
}

