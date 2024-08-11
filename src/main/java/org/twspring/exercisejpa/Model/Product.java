package org.twspring.exercisejpa.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "varchar(25) not null")
    @NotEmpty(message = "Name cannot be empty")
    @Size(min=4,max=25, message = "Name must be between 4 to 25 characters")
    private String name;

    @Column(columnDefinition = "double(6,2) not null")
    @NotNull(message = "Price cannot be empty")
    @Positive(message = "Price must be greater than 0")
    @Max(value = 999999, message = "Price cannot be greater than 999999")
    private double price;

    @Column(columnDefinition = "int not null")
    @NotNull(message = "CategoryID cannot be empty")
    @Positive(message = "CategoryID cannot be a zero or a negative number")
    @Min(value = 1, message = "CategoryID cannot be less than 1")
    private Integer categoryId;

    @Column(columnDefinition = "boolean not null default false")
    @NotNull(message = "On sale cannot be null")
    @AssertFalse(message = "On sale must be initiated to false")
    private boolean onSale;

    @Column(columnDefinition = "int not null default 0")
    @NotNull(message = "Number of reviews cannot be null")
    @Range(min=0,max=0, message = "Number of reviews must be 0 at creation")
    private Integer numberOfReview;

    @Column(columnDefinition = "double(2,2) not null default 0")
    @NotNull(message = "Average score cannot be null")
    @Range(min=0,max=0, message = "Products must have 0 score at creation")
    @Max(value = 5, message = "Average score cannot be greater than 5")
    private double averageScore;
}
