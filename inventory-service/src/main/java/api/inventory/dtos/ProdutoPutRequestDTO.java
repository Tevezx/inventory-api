package api.inventory.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProdutoPutRequestDTO {
    @NotNull(message = "Id obrigatório")
    private Long id;
    @NotBlank(message = "Nome obrigatório")
    private String nome;
    @NotBlank(message = "Descrição obrigatória")
    private String descricao;
    @NotBlank(message = "Preço obrigatório")
    @Positive(message = "Preço deve ser positivo")
    private Double preco;
    @NotBlank(message = "Quantidade em estoque obrigatório")
    private Integer qtdEstoque;
}
