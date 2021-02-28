package com.epam.esm.entity;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "order")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order extends RepresentationModel<Order> implements Serializable {
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
    @NotNull
    @PastOrPresent
    @Column(name = "order_date")
    private LocalDateTime orderDate;
}
