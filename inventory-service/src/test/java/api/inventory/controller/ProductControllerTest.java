package api.inventory.controller;

import api.inventory.commons.FileUtils;
import api.inventory.commons.ProductUtils;
import api.inventory.model.Product;
import api.inventory.repository.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@WebMvcTest(controllers = ProductController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan(basePackages = "api.inventory")
class ProductControllerTest {
    private final static String URL = "/v1/products";
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private ProductRepository repository;
    private List<Product> productList = new ArrayList<>();
    @Autowired
    private FileUtils fileUtils;
    @Autowired
    private ProductUtils productUtils;

    @BeforeEach
    void init() {
        productList = productUtils.newProductList();
    }

    @Test
    @DisplayName("GET v1/products - Returning every products cadastred")
    @Order(1)
    void findAll_ReturnsAllProducts_WhenSuccessFull() throws Exception {
        var response = fileUtils.readResourceFile("product/get-product-all-200.json");

        BDDMockito.when(repository.findAll()).thenReturn(productList);

        mockMvc.perform(MockMvcRequestBuilders.get(URL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/products/1 - Returning product by id")
    @Order(2)
    void findById_ReturnsProduct_WhenProductIdIsFound() throws Exception {
        var id = productList.getFirst().getId();
        var response = fileUtils.readResourceFile("product/get-product-find-by-id-1-200.json");
        var productById = productList.stream().filter(product -> product.getId().equals(id)).findFirst();

        BDDMockito.when(repository.findById(id)).thenReturn(productById);

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/products/90 - Returning throw NotFoundException, case id not found")
    @Order(3)
    void findById_ReturnsThrowNotFoundException_WhenProductIdIsNotFound() throws Exception {
        var response = fileUtils.readResourceFile("product/get-product-find-by-id-404.json");
        var id = 90L;

        BDDMockito.when(repository.findAll()).thenReturn(productList);

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/products/filterName?name=Mouse Logitech - Returning every products where name equals parameter")
    @Order(4)
    void listAllName_ReturnsProducts_WhenNameIsFound() throws Exception {
        var response = fileUtils.readResourceFile("product/get-product-filter-name-Mouse-Logitech-200.json");
        var name = "Mouse Logitech";
        var productByName = productList.stream().filter(product -> product.getName().equalsIgnoreCase(name)).toList();

        BDDMockito.when(repository.findByName(name)).thenReturn(productByName);

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/filterName").param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/products/filterName?name= - Returning every products, case parameter null")
    @Order(5)
    void listAllName_ReturnsProducts_WhenNameIsNull() throws Exception {
        var response = fileUtils.readResourceFile("product/get-product-all-200.json");

        BDDMockito.when(repository.findAll()).thenReturn(productList);

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/filterName").param("name"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/products/filterName?name= - Returning empty list, case parameter null")
    @Order(6)
    void listAllName_ReturnsProducts_WhenNameIsBlank() throws Exception {
        var response = fileUtils.readResourceFile("product/get-product-filter-name-empty-200.json");

        BDDMockito.when(repository.findAll()).thenReturn(Collections.emptyList());
        var nome = " ";

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/filterName").param("nome", nome))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("POST v1/products - Saving one product")
    @Order(7)
    void save_CreatesProduct_WhenSuccessFull() throws Exception {
        var request = fileUtils.readResourceFile("product/post-product-request-200.json");
        var response = fileUtils.readResourceFile("product/post-product-response-201.json");

        var product = Product
                .builder()
                .id(1L)
                .name("HeadSet Logitech")
                .description("HeadSet logitech gamer")
                .price(250.0)
                .qtdStock(3)
                .build();

        BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("DELETE v1/products/1 - Deleting one product by id")
    @Order(8)
    void deleteById_removesProduct_WhenSuccessFull() throws Exception {
        var id = 1L;
        var productById = productList.stream().filter(product -> product.getId().equals(id)).findFirst();
        BDDMockito.when(repository.findById(id)).thenReturn(productById);

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("DELETE v1/products/90 - Deleting one product by id not exists and throw NotFoundException")
    @Order(9)
    void deleteById_ThrowNotFoundException_WhenIdIsNotFound() throws Exception {
        var response = fileUtils.readResourceFile("product/delete-product-response-404.json");

        BDDMockito.when(repository.findAll()).thenReturn(productList);
        var id = 90L;

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("PUT v1/products - Updating product")
    @Order(10)
    void update_UpdateProduct_WhenSuccessFull() throws Exception {
        var request = fileUtils.readResourceFile("product/put-product-request-204.json");
        var id = 2L;
        var productExpected = productList.stream().filter(product -> product.getId().equals(id)).findFirst();

        BDDMockito.when(repository.findById(id)).thenReturn(productExpected);

        mockMvc.perform(MockMvcRequestBuilders
                        .put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("PUT v1/products - Updating product by id not exists and throw NotFoundException")
    @Order(11)
    void update_UpdateProduct_WhenIdIsNotFound() throws Exception {
        var request = fileUtils.readResourceFile("product/put-product-request-404.json");
        var response = fileUtils.readResourceFile("product/put-product-response-404.json");

        BDDMockito.when(repository.findAll()).thenReturn(productList);

        mockMvc.perform(MockMvcRequestBuilders
                        .put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @ParameterizedTest
    @MethodSource("postBadRequestSource")
    @DisplayName("POST v1/products - Saving one product where data is blank")
    @Order(12)
    void save_SavesProduct_WhenEmptyFields(String fileName, List<String> errors) throws Exception {
        var request = fileUtils.readResourceFile("product/%s".formatted(fileName));

        var mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        Exception resolvedException = mvcResult.getResolvedException();
        Assertions.assertThat(resolvedException).isNotNull();

        Assertions.assertThat(resolvedException.getMessage()).contains(errors);
    }

    @ParameterizedTest
    @MethodSource("putBadRequestSource")
    @DisplayName("PUT v1/produtos - Atualizando produto com id nulo e preços inválidos")
    @Order(13)
    void update_UpdateProduto_WhenIdIsNullAndEmptyFields(String fileName, List<String> errors) throws Exception {
        var request = fileUtils.readResourceFile("product/%s".formatted(fileName));

        var mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        Exception resolvedException = mvcResult.getResolvedException();
        Assertions.assertThat(resolvedException).isNotNull();

        Assertions.assertThat(resolvedException.getMessage()).contains(errors);
    }

    private static Stream<Arguments> postBadRequestSource() {
        return Stream.of(
                Arguments.of("post-product-request-user-blank-fields-400.json", allRequiredErrors()),
                Arguments.of("post-product-request-user-negative-fields-400.json", priceAndQtdStockPositive())
        );
    }

    private static Stream<Arguments> putBadRequestSource() {
        return Stream.of(
                Arguments.of("put-request-user-blank-fields-400.json", allRequiredErrors()),
                Arguments.of("put-request-user-null-id-400.json", idNotNull()),
                Arguments.of("put-request-user-price-stock-negative-400.json", priceAndQtdStockPositive())
        );
    }

    private static List<String> allRequiredErrors() {
        var nameRequired = "Name required";
        var descriptionRequired = "Description required";
        var priceRequired = "Price required";
        var qtdStockRequired = "Quantity in stock required";

        return List.of(nameRequired, descriptionRequired, priceRequired, qtdStockRequired);
    }

    private static List<String> priceAndQtdStockPositive() {
        var pricePositive = "The price should be positive";
        var qtdStockPositive = "Quantity in stock must be greater than or equal to zero";
        return List.of(pricePositive, qtdStockPositive);
    }

    private static List<String> idNotNull() {
        var idNotNull = "Id required";
        return List.of(idNotNull);
    }
}