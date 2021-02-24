package com.epam.esm.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "user")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    @Min(1)
    @Id
    @GeneratedValue
    private Long id;
    @Size(min = 1, max = 255)
    private String name;
    @Valid
    @OneToMany(mappedBy = "user")
    private List<Order> orders;
}
