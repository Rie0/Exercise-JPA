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

    @Column(columnDefinition = "double not null")
    @NotNull(message = "Price cannot be empty")
    @Positive(message = "Price must be greater than 0")
    private double price;

    @Column(columnDefinition = "int not null")
    @NotNull(message = "CategoryID cannot be empty")
    @Positive(message = "CategoryID cannot be a zero or a negative number")
    @Min(value = 1, message = "CategoryID cannot be less than 1")
    private Integer categoryId;

    @Column(columnDefinition = "boolean not null default false")
    @NotNull(message = "On sale cannot be null")
    private boolean onSale;

    @Column(columnDefinition = "int not null default 0")
    @NotNull(message = "Number of reviews cannot be null")
    @PositiveOrZero(message ="Number of reviews cannot be a negative number" )
    @Range(min=0, message = "Number of reviews cannot be less than 0")
    private Integer numberOfReview;

    @Column(columnDefinition = "double not null default 0")
    @NotNull(message = "Average score cannot be null")
    @Range(min=0,max=5, message = "Products have a review range of 1 to 5")
    @Max(value = 5, message = "Average score cannot be greater than 5")
    private double averageScore;
}
