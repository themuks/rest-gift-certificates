package com.epam.esm.entity;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto extends RepresentationModel<UserDto> {
    private Long id;
    private String name;
    private String surname;
    private LocalDateTime registrationDate;
}
