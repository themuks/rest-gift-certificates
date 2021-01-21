package com.epam.esm.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tag {
    @Min(1)
    private Long id;
    @Size(min = 1, max = 255)
    private String name;
    private List<GiftCertificate> giftCertificates;
}
