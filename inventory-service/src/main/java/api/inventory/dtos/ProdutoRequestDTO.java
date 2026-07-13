package api.inventory.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProdutoRequestDTO {
    private String nome;
    private String descricao;
    private Double preco;
    private Integer qtdEstoque;
}
