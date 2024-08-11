package org.twspring.exercisejpa.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "varchar(25) not null unique")
    @NotEmpty(message = "Username cannot be empty")
    @Size(min=6, max=25, message = "Username must have between 6 and 25 characters")
    private String username;

    @Column(columnDefinition = "varchar(25) not null")
    @NotEmpty(message = "Password cannot be empty")
    @Size(min = 7, max=25, message = "Password must have between 7 and 25 characters")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{7,}$", message = "Password must have both characters and digits and a special character")
    private String password;

    @Column(columnDefinition = "varchar(30) not null")
    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Invalid email address")
    @Size(min= 10, max = 30, message = "Email must have between 10 to 30 characters")
    private String email;

    @Column(columnDefinition = "varchar(25) not null")
    @NotEmpty(message = "Role cannot be empty")
    @Pattern(regexp = "^(Admin|Customer)$", message = "Role can only be Admin or User.")
    @Size(min= 4, max = 25, message = "Role must have between 4 to 25 characters")
    private String role;

    @Column(columnDefinition = "double(6,2) not null")
    @NotNull(message = "Balance cannot be empty")
    @Positive(message = "Balance must be a positive number")
    @Max(value = 999999, message = "Balance cannot be greater than 999999")
    private double balance;

    @Column(columnDefinition = "boolean not null default false")
    @NotNull(message = "Is prime member cannot be empty")
    @AssertFalse(message = "New members cannot have prime membership")
    private boolean isPrimeMember;
}
