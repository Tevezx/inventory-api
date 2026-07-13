package api.inventory.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Setter
public class ProdutoData {
    @Getter
    private final List<Produto> produtoList = new ArrayList<>();
}
