package api.inventory.model;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProdutoData {
    @Getter
    private final List<Produto> produtoList = new ArrayList<>();
}
