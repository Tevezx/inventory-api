package api.inventory.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
public class Produto {
    @EqualsAndHashCode.Include
    private final Long id;
    private final String nome;
    private String descricao;
    private Double preco;
    private Double qtdEstoque;
}
