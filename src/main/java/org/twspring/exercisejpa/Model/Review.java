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
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "int not null")
    @NotNull(message = "User ID cannot be empty")
    @Positive(message = "User ID cannot be a zero or a negative number")
    @Min(value = 1, message = "User ID cannot be less than 1")
    private Integer userId;

    @Column(columnDefinition = "int not null")
    @NotNull(message = "Product ID cannot be empty")
    @Positive(message = "Product ID cannot be a zero or a negative number")
    @Min(value = 1, message = "Product ID cannot be less than 1")
    private Integer productId;

    @Column(columnDefinition = "int not null")
    @NotNull(message = "Score cannot be empty")
    @Positive(message = "Score cannot be a zero or a negative number")
    @Range(min = 1, max = 5, message = "Score must be between 1 to 5")
    private Integer score;

    @Column(columnDefinition = "varchar(255) not null")
    @NotEmpty(message = "Comment cannot be empty")
    @Size(min = 5, max = 255, message = "Comment must have between 5 to 255 characters")
    private String comment;
}
