package api.inventory.dtos;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProdutoRequestDTO {
    @NotBlank(message = "Nome obrigatório")
    private String nome;
    @NotBlank(message = "Descrição obrigatória")
    private String descricao;
    @NotNull(message = "Preço obrigatório")
    @Positive(message = "Preço deve ser positivo")
    private Double preco;
    @NotNull(message = "Quantidade em estoque obrigatório")
    @PositiveOrZero(message = "Quantidade em estoque deve ser maior ou igual a zero")
    private Integer qtdEstoque;
}
