package com.epam.esm.entity;

import lombok.Data;

@Data
public class AuthenticationRequestDto {
    private String email;
    private String password;
}
