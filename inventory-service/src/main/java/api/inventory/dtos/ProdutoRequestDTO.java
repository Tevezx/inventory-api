package api.inventory.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProdutoRequestDTO {
    @NotBlank(message = "Nome obrigatório")
    private String nome;
    @NotBlank(message = "Descrição obrigatória")
    private String descricao;
    @NotBlank(message = "Preço obrigatório")
    private Double preco;
    @NotBlank(message = "Quantidade em estoque obrigatório")
    private Integer qtdEstoque;
}
