package ru.practicum.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDto {

    private Long id;

    @NotBlank
    @Size(min = 1, max = 50)
    private String name;

}