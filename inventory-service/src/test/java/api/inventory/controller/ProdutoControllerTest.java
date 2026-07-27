package api.inventory.controller;

import api.inventory.commons.FileUtils;
import api.inventory.commons.ProdutoUtils;
import api.inventory.model.Produto;
import api.inventory.model.ProdutoData;
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
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@WebMvcTest(controllers = ProdutoController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan(basePackages = "api.inventory")
class ProdutoControllerTest {
    private final static String URL = "/v1/produtos";
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private ProdutoData produtoData;
    @MockitoSpyBean
    private ProdutoRepository repository;
    private List<Produto> produtoList = new ArrayList<>();

    @Autowired
    private FileUtils fileUtils;
    @Autowired
    private ProdutoUtils produtoUtils;

    @BeforeEach
    void init() {
        produtoList = produtoUtils.newProdutoList();
    }

    @Test
    @DisplayName("GET v1/produtos - Retornando todos os produtos cadastrados")
    @Order(1)
    void findAll_ReturnsAllProdutos_WhenSucessFull() throws Exception {
        var response = fileUtils.readResourceFile("produto/get-produto-all-200.json");

        BDDMockito.when(produtoData.getProdutoList()).thenReturn(produtoList);

        mockMvc.perform(MockMvcRequestBuilders.get(URL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/produtos/1 - Retornando o produto pelo id")
    @Order(2)
    void findById_ReturnsProduto_WhenProdutoIdIsFound() throws Exception {
        var response = fileUtils.readResourceFile("produto/get-produto-find-by-id-1-200.json");

        BDDMockito.when(produtoData.getProdutoList()).thenReturn(produtoList);
        var id = 1L;

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/produtos/90 - Retornando o throw NotFoundException, caso o id não seja encontrado")
    @Order(3)
    void findById_ReturnsThrowNotFoundException_WhenProdutoIdIsNotFound() throws Exception {
        var response = fileUtils.readResourceFile("produto/get-produto-find-by-id-404.json");

        BDDMockito.when(produtoData.getProdutoList()).thenReturn(produtoList);
        var id = 90L;

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/produtos/filterName?nome=Mouse Logitech - Retornando todos os produtos onde o nome seja igual ao parametro")
    @Order(4)
    void listAllName_ReturnsProdutos_WhenNameIsFound() throws Exception {
        var response = fileUtils.readResourceFile("produto/get-produto-filter-name-Mouse-Logitech-200.json");

        BDDMockito.when(produtoData.getProdutoList()).thenReturn(produtoList);
        var nome = "Mouse Logitech";

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/filterName").param("nome", nome))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/produtos/filterName?nome= - Retornando todos os produtos, caso o parametro seja nulo")
    @Order(5)
    void listAllName_ReturnsProdutos_WhenNameIsNull() throws Exception {
        var response = fileUtils.readResourceFile("produto/get-produto-all-200.json");

        BDDMockito.when(produtoData.getProdutoList()).thenReturn(produtoList);

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/filterName").param("nome"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/produtos/filterName?name= - Retornando uma lista vazia, caso o parametro seja branco")
    @Order(6)
    void listAllName_ReturnsProdutos_WhenNameIsBlank() throws Exception {
        var response = fileUtils.readResourceFile("produto/get-produto-filter-name-empty-200.json");

        BDDMockito.when(produtoData.getProdutoList()).thenReturn(Collections.emptyList());
        var nome = " ";

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/filterName").param("nome", nome))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("POST v1/produtos - Salvando um produto")
    @Order(7)
    void save_CreatesProduto_WhenSucessFull() throws Exception {
        var request = fileUtils.readResourceFile("produto/post-produto-request-200.json");
        var response = fileUtils.readResourceFile("produto/post-produto-response-201.json");

        var produto = Produto
                .builder()
                .id(3L)
                .nome("HeadSet Logitech")
                .descricao("HeadSet logitech gamer")
                .preco(250.0)
                .qtdEstoque(3)
                .build();

        BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(produto);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/produtos")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("DELETE v1/produtos/1 - Deletando um produto pelo id")
    @Order(8)
    void deleteById_removesProduto_WhenSucessFull() throws Exception {
        var id = 1L;
        BDDMockito.when(produtoData.getProdutoList()).thenReturn(produtoList);

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("DELETE v1/produtos/90 - Deletando um produto com id inexistente e lançando throw NotFoundException")
    @Order(9)
    void deleteById_ThrowNotFoundException_WhenIdIsNotFound() throws Exception {
        var response = fileUtils.readResourceFile("produto/delete-produto-response-404.json");

        BDDMockito.when(produtoData.getProdutoList()).thenReturn(produtoList);
        var id = 90L;

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("PUT v1/produtos - Atualizando produto")
    @Order(10)
    void update_UpdateProduto_WhenSucessFull() throws Exception {
        var request = fileUtils.readResourceFile("produto/put-produto-request-204.json");

        BDDMockito.when(produtoData.getProdutoList()).thenReturn(produtoList);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/v1/produtos")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("PUT v1/produtos - Atualizando produto com id inexistente e lançando throw NotFoundException")
    @Order(11)
    void update_UpdateProduto_WhenIdIsNotFound() throws Exception {
        var request = fileUtils.readResourceFile("produto/put-produto-request-404.json");
        var response = fileUtils.readResourceFile("produto/put-produto-response-404.json");

        BDDMockito.when(produtoData.getProdutoList()).thenReturn(produtoList);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/v1/produtos")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @ParameterizedTest
    @MethodSource("postBadRequestSource")
    @DisplayName("POST v1/produtos - Salvando um produto com dados em branco")
    @Order(12)
    void save_SavesProduto_WhenEmptyFields(String fileName, List<String> errors) throws Exception {
        var request = fileUtils.readResourceFile("produto/%s".formatted(fileName));

        var mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/produtos")
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
        var request = fileUtils.readResourceFile("produto/%s".formatted(fileName));

        var mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put("/v1/produtos")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
    }

    private static Stream<Arguments> postBadRequestSource() {
        return Stream.of(
                Arguments.of("post-produto-request-user-blank-fields-400.json", allRequiredErrors()),
                Arguments.of("post-produto-request-user-negative-fields-400.json", priceAndQtdStockPositive())
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
        var nomeRequired = "Nome obrigatório";
        var descricaoRequired = "Descrição obrigatória";
        var precoRequired = "Preço obrigatório";
        var qtdEstoqueRequired = "Quantidade em estoque obrigatório";

        return List.of(nomeRequired, descricaoRequired, precoRequired, qtdEstoqueRequired);
    }

    private static List<String> priceAndQtdStockPositive() {
        var precoPositive = "Preço deve ser positivo";
        var qtdEstoquePositive = "Quantidade em estoque deve ser maior ou igual a zero";
        return List.of(precoPositive, qtdEstoquePositive);
    }

    private static List<String> idNotNull() {
        var idNotNull = "Id obrigatório";
        return List.of(idNotNull);
    }
}