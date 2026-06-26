package api.inventory.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
public class Produto {
    @EqualsAndHashCode.Include
    private final Long id;
    private final String nome;
    private String descricao;
    private Double preco;
    private Integer qtdEstoque;
}
