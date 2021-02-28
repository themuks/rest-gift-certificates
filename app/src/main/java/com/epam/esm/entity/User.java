package com.epam.esm.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "user")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends RepresentationModel<User> implements Serializable {
    @Min(1)
    @Id
    @GeneratedValue
    private Long id;
    @Size(min = 1, max = 255)
    private String name;
}
