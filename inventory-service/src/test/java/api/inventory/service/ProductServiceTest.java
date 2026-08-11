package api.inventory.service;

import api.inventory.commons.ProductUtils;
import api.inventory.model.Product;
import api.inventory.repository.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductServiceTest {
    @InjectMocks
    private ProductService service;
    @Mock
    private ProductRepository repository;
    @InjectMocks
    private ProductUtils productUtils;
    private List<Product> productList = new ArrayList<>();

    @BeforeEach
    void init() {
        productList = productUtils.newProductList();
    }

    @Test
    @DisplayName("Returning every products")
    @Order(1)
    void findAll_ReturnsAllProducts_WhenSuccessFull() {
        BDDMockito.when(repository.findAll()).thenReturn(productList);
        var products = service.findAll();

        Assertions.assertThat(products).isNotNull().hasSameElementsAs(productList);
    }

    @Test
    @DisplayName("Returning product by id")
    @Order(2)
    void findById_ReturnsProduct_WhenProductIdIsFound() {
        var product = productList.getFirst();
        BDDMockito.when(repository.findById(product.getId())).thenReturn(Optional.of(product));

        Assertions.assertThatNoException().isThrownBy(() -> service.findById(product.getId()));
    }

    @Test
    @DisplayName("Returning throw ResponseStatusException, case the id not found")
    @Order(3)
    void findById_ReturnsThrowResponseStatusException_WhenProductIdIsNotFound() {
        var product = productList.getFirst();
        BDDMockito.when(repository.findById(product.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException().isThrownBy(() -> service.findById(product.getId())).isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("Returning every products where name equals parameter")
    @Order(4)
    void listAllName_ReturnsProducts_WhenNameIsFound() {
        var product = productList.getFirst();

        var productExpected = productList
                .stream()
                .filter(products -> products.getName().equalsIgnoreCase(product.getName()))
                .toList();

        BDDMockito.when(repository.findByName(product.getName())).thenReturn(productExpected);

        var productResponse = service.listAllName(product.getName());
        Assertions.assertThat(productExpected).hasSameElementsAs(productResponse).isNotNull();
    }

    @Test
    @DisplayName("Returning all products if the parameter is null")
    @Order(5)
    void listAllName_ReturnsProducts_WhenNameIsNull() {
        BDDMockito.when(repository.findAll()).thenReturn(productList);

        var productsResponse = service.listAllName(null);
        Assertions.assertThat(productsResponse).isNotNull().hasSameElementsAs(productList);
    }

    @Test
    @DisplayName("Returning one list empty if parameter is blank")
    @Order(6)
    void listAllName_ReturnsProducts_WhenNameIsBlank() {
        var products = productList.getFirst();
        BDDMockito.when(repository.findByName(products.getName())).thenReturn(Collections.emptyList());

        var productResponse = service.listAllName(products.getName());
        Assertions.assertThat(productResponse).isEmpty();
    }

    @Test
    @DisplayName("Saving one product")
    @Order(7)
    void save_CreatesProduct_WhenSuccessFull() {
        var product = Product
                .builder()
                .id(3L)
                .name("Fone Logitech")
                .description("Fone logitech gamer")
                .price(240.0)
                .qtdStock(4)
                .build();

        BDDMockito.when(repository.save(product)).thenReturn(product);
        var productSaved = service.save(product);

        Assertions.assertThat(productSaved).isEqualTo(product).hasNoNullFieldsOrProperties();
    }

    @Test
    @DisplayName("Saving one product name and description is null and returning throw IllegalArgumentException")
    @Order(8)
    void save_ThrowIllegalArgumentException_WhenNameIsNull() {
        var product = Product
                .builder()
                .id(3L)
                .name(null)
                .description(null)
                .price(250.0)
                .qtdStock(4)
                .build();

        Assertions.assertThatThrownBy(() -> service.save(product)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Delete product by id")
    @Order(9)
    void deleteById_removesProduct_WhenSuccessFull() {
        var product = productList.getFirst();
        BDDMockito.when(repository.findById(product.getId())).thenReturn(Optional.of(product));
        Assertions.assertThatNoException().isThrownBy(() -> service.deleteById(product.getId()));
    }

    @Test
    @DisplayName("Deleted one product by id not found and throw ResponseStatusException")
    @Order(10)
    void deleteById_ThrowResponseStatusException_WhenIdIsNotFound() {
        var product = productList.getFirst();
        BDDMockito.when(repository.findById(product.getId())).thenReturn(Optional.empty());
        Assertions.assertThatException().isThrownBy(() -> service.deleteById(product.getId())).isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("Updating product")
    @Order(11)
    void update_UpdateProduct_WhenSuccessFull() {
        var product = productList.getFirst();
        product.setName("HeadSet Logitech");
        BDDMockito.when(repository.findById(product.getId())).thenReturn(Optional.of(product));

        Assertions.assertThatNoException().isThrownBy(() -> service.update(product));
    }

    @Test
    @DisplayName("Updating product by id not found and throw ResponseStatusException")
    @Order(12)
    void update_UpdateProduct_WhenIdIsNotFound() {
        var product = productList.getFirst();
        BDDMockito.when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThatException().isThrownBy(() -> service.update(product)).isInstanceOf(ResponseStatusException.class);
    }
}