package api.inventory.commons;

import api.inventory.model.Produto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProdutoUtils {
    public List<Produto> newProdutoList() {
        var mouse = Produto
                .builder()
                .id(1L)
                .nome("Mouse Logitech")
                .descricao("Mouse logitech de precisão")
                .preco(200.0)
                .qtdEstoque(2)
                .build();

        var teclado = Produto
                .builder()
                .id(2L)
                .nome("Teclado Logitech")
                .descricao("Teclado logitech Gamer")
                .preco(400.0)
                .qtdEstoque(7)
                .build();

        return new ArrayList<>(List.of(mouse, teclado));
    }
}
