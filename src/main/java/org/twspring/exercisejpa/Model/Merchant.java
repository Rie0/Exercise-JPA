package org.twspring.exercisejpa.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Merchant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "varchar(25) not null")
    @NotEmpty(message = "Name cannot be empty")
    @Size(min=4, max=25, message = "Name cannot have between 4 to 25 letters")
    private String name;

    @Column(columnDefinition = "boolean not null default false")
    @NotNull(message = "Certified cannot be null")
    @AssertFalse(message = "Certified must be initiated at false")
    private boolean certified;
}
