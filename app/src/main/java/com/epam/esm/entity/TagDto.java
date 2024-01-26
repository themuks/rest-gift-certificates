package com.epam.esm.entity;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagDto extends RepresentationModel<TagDto> {
    private Long id;
    private String name;
}
