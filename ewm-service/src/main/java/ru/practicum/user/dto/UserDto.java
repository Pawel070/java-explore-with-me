package ru.practicum.user.dto;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserDto {

    Long id;

    @NotBlank
    @Size(min = 6, max = 250)

    @Email
    private String email;

    @NotBlank
    @Size(min = 2, max = 250)
    private String name;

}