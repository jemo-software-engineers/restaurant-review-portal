package com.jemo.RestaurantReviewPortal.restaurant;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Restaurant {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    @NonNull
    @NotEmpty(message = "name cannot be empty")
    private String name;

    @Column(nullable = false)
    @NonNull
    @NotEmpty(message = "address cannot be empty")
    private String address;

    @Column(nullable = false)
    @NonNull
    @NotEmpty(message = "phone cannot be empty")
    private String phone;

    @Email
    @NonNull
    @NotEmpty(message = "email cannot be empty")
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @NonNull
    @NotEmpty(message = "city cannot be empty")
    private String city;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

}
