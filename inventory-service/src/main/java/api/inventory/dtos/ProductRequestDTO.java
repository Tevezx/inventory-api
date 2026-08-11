package api.inventory.dtos;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequestDTO {
    @NotBlank(message = "Name required")
    private String name;
    @NotBlank(message = "Description required")
    private String description;
    @NotNull(message = "Price required")
    @Positive(message = "The price should be positive")
    private Double price;
    @NotNull(message = "Quantity in stock required")
    @PositiveOrZero(message = "Quantity in stock must be greater than or equal to zero")
    private Integer qtdStock;
}
