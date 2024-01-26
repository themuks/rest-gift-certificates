package com.epam.esm.entity;

import lombok.Data;

@Data
public class RegistrationRequestDto {
    private String email;
    private String password;
    private String name;
    private String surname;
}
