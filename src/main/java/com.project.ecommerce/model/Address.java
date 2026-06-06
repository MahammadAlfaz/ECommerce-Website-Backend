package com.project.ecommerce.model;

import jakarta.persistence.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;




@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;
    @NotBlank
    @Size(min=5, message = "Street name must be more than 4 character")
    private String street;
    @NotBlank
    @Size(min=5, message = "Building  name must be more than 4 character")
    private String buildingName;

    @NotBlank
    @Size(min=5, message = "state name  must be more than 4 character")
    private String state;
    @NotBlank
    @Size(min=5, message = "country name must be more than 4 character")
    private String country;

    @NotBlank
    @Size(min=5, message = "Pin Code  must be more than 4 character")
    private String pinCode;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
