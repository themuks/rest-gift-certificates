package com.epam.esm.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "`order`")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order implements Serializable {
    @Min(1)
    @Id
    @GeneratedValue
    private Long id;
    @NotNull
    @Valid
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @NotNull
    @Valid
    @ManyToOne
    @JoinColumn(name = "gift_certificate_id", nullable = false)
    private GiftCertificate giftCertificate;
    @NotNull
    @Positive
    private BigDecimal cost;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    @NotNull
    @PastOrPresent
    @Column(name = "order_date")
    private LocalDateTime orderDate;
}
