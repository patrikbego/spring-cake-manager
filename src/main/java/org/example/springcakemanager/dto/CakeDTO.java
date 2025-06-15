package org.example.springcakemanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CakeDTO(

        Long id,

        @NotBlank(message = "Title cannot be blank")
        @Size(max = 100, message = "Title must be at most 100 characters")
        String title,

        @Size(max = 255, message = "Description must be at most 255 characters")
        String description,

        @Size(max = 300, message = "Image URL must be at most 300 characters")
        @Pattern(regexp = "^(https?://[^\\s]+)$", message = "Image URL must be a valid URL")
        String image

) {}
