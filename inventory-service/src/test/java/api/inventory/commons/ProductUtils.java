package api.inventory.commons;

import api.inventory.model.Product;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductUtils {
    public List<Product> newProductList() {
        var mouse = Product
                .builder()
                .id(1L)
                .name("Mouse Logitech")
                .description("Mouse logitech de precisão")
                .price(200.0)
                .qtdStock(2)
                .build();

        var teclado = Product
                .builder()
                .id(2L)
                .name("Teclado Logitech")
                .description("Teclado logitech Gamer")
                .price(400.0)
                .qtdStock(7)
                .build();

        return new ArrayList<>(List.of(mouse, teclado));
    }

    public Product newProduct() {
        return Product
                .builder()
                .id(1L)
                .name("Mouse Logitech")
                .description("Mouse logitech de precisão")
                .price(200.0)
                .qtdStock(2)
                .build();
    }
}
