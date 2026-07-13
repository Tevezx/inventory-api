package api.inventory.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProdutoPutRequestDTO {
    private Long id;
    private String nome;
    private String descricao;
    private Double preco;
    private Integer qtdEstoque;
}
