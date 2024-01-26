package com.epam.esm.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "gift_certificate")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GiftCertificate implements Serializable {
    @Min(1)
    @Id
    @GeneratedValue
    private Long id;
    @Size(min = 1, max = 255)
    private String name;
    @Size(min = 1, max = 65535)
    private String description;
    @Positive
    private BigDecimal price;
    @Positive
    @Column(name = "duration_in_days")
    private Integer durationInDays;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    @PastOrPresent
    @Column(name = "create_date")
    private LocalDateTime createDate;
    @PastOrPresent
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    @Column(name = "last_update_date")
    private LocalDateTime lastUpdateDate;
    @Valid
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "gift_certificate_has_tag",
            joinColumns = {@JoinColumn(name = "gift_certificate_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id")})
    private List<Tag> tags;
}



