package org.twspring.exercisejpa.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MerchantStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "int not null")
    @NotNull(message = "ProductId cannot be empty")
    @Positive(message = "ProductId cannot be a zero or a negative number")
    @Min(value = 1, message = "ProductId cannot be less than 1")
    private Integer productId;

    @Column(columnDefinition = "int not null")
    @NotNull(message = "MerchantId cannot be empty")
    @Positive(message = "MerchantId cannot be a zero or a negative number")
    @Min(value = 1, message = "MerchantId cannot be less than 1")
    private Integer merchantId;

    @Column(columnDefinition = "int not null")
    @NotNull(message = "Stock cannot be empty")
    @Positive(message = "Stock cannot be a zero or a negative number")
    @Min(value = 11, message = "Stock cannot be less than 11")
    private Integer stock;
}
