package ru.practicum.user.dto;

import lombok.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSmDto {

    @NotNull
    private Long id;

    @NotBlank
    private String name;

}