package com.epam.esm.entity;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tag")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tag extends RepresentationModel<Tag> implements Serializable {
    @Min(1)
    @Id
    @GeneratedValue
    private Long id;
    @Size(min = 1, max = 255)
    private String name;
}
