package api.inventory.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
public class ProdutoData {
    private static final List<Produto> produtoList = new ArrayList<>();

    static {
        var mouseLogitech = produtoList.add(Produto.
                builder()
                .id(1L)
                .nome("Mouse Gamer Logitech G403 HERO")
                .descricao("RGB LIGHTSYNC, 6 Botões Programáveis, Ajuste de Peso e Sensor HERO 25K - 910-005631")
                .preco(190.0)
                .qtdEstoque(10)
                .build());
    }
}
