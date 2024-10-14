package com.jemo.RestaurantReviewPortal.menuitem;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Menuitem {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    @NonNull
    @NotEmpty(message = "name cannot be empty")
    private String name;

    @Column(nullable = false)
    @NonNull
    @NotEmpty(message = "description cannot be empty")
    private String description;

    @Column(nullable = false)
    @NonNull
    private BigDecimal price;

    @Column(nullable = false)
    @NonNull
    private Long menuId;

    @Column(nullable = false)
    @NonNull
    @Enumerated(EnumType.STRING)
    private Availability availability;

    @Column(nullable = false)
    @NonNull
    @NotEmpty(message = "dietary info cannot be empty")
    private String dietaryInfo;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
