package api.inventory.controller;

import api.inventory.model.Produto;
import api.inventory.model.ProdutoData;
import api.inventory.repository.ProdutoRepository;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@WebMvcTest(controllers = ProdutoController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan(basePackages = "api.inventory")
class ProdutoControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private ProdutoData produtoData;
    @MockitoSpyBean
    private ProdutoRepository repository;
    private List<Produto> produtoList = new ArrayList<>();

    @Autowired
    private ResourceLoader resourceLoader;

    @BeforeEach
    void init() {
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

        produtoList.addAll(List.of(mouse, teclado));
    }

    @Test
    @DisplayName("GET v1/produtos - Retornando todos os produtos cadastrados")
    @Order(1)
    void findAll_ReturnsAllProdutos_WhenSucessFull() throws Exception {
        var response = readResourceFile("produto/get-produto-all-200.json");
        BDDMockito.when(produtoData.getProdutoList()).thenReturn(produtoList);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/produtos"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/produtos/1 - Retornando o produto pelo id")
    @Order(2)
    void findById_ReturnsProduto_WhenProdutoIdIsFound() throws Exception {
        var response = readResourceFile("produto/get-produto-find-by-id-1-200.json");
        BDDMockito.when(produtoData.getProdutoList()).thenReturn(produtoList);
        var id = 1L;
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/produtos/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/produtos/90 - Retornando o throw ResponseStatusException, caso o id não seja encontrado")
    @Order(3)
    void findById_ReturnsThrowResponseStatusException_WhenProdutoIdIsNotFound() throws Exception {
        BDDMockito.when(produtoData.getProdutoList()).thenReturn(produtoList);
        var id = 90L;
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/produtos/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("Produto não encontrado"));
    }

    @Test
    @DisplayName("GET v1/produtos/filterName?nome=Mouse Logitech - Retornando todos os produtos onde o nome seja igual ao parametro")
    @Order(4)
    void listAllName_ReturnsProdutos_WhenNameIsFound() throws Exception {
        var response = readResourceFile("produto/get-produto-filter-name-Mouse-Logitech-200.json");
        BDDMockito.when(produtoData.getProdutoList()).thenReturn(produtoList);

        var nome = "Mouse Logitech";
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/produtos/filterName").param("nome", nome))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/produtos/filterName?nome= - Retornando todos os produtos, caso o parametro seja nulo")
    @Order(5)
    void listAllName_ReturnsProdutos_WhenNameIsNull() throws Exception {
        var response = readResourceFile("produto/get-produto-all-200.json");

        BDDMockito.when(produtoData.getProdutoList()).thenReturn(produtoList);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/produtos/filterName").param("nome"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/produtos/filterName?name= - Retornando uma lista vazia, caso o parametro seja branco")
    @Order(6)
    void listAllName_ReturnsProdutos_WhenNameIsBlank() throws Exception {
        var response = readResourceFile("produto/get-produto-filter-name-empty-200.json");

        BDDMockito.when(produtoData.getProdutoList()).thenReturn(Collections.emptyList());
        var nome = " ";

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/produtos/filterName").param("nome", nome))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("POST v1/produtos - Salvando um produto")
    @Order(7)
    void save_CreatesProduto_WhenSucessFull() throws Exception {
        var request = readResourceFile("produto/post-produto-request-200.json");
        var response = readResourceFile("produto/post-produto-response-201.json");

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
    @DisplayName("POST v1/produtos - Salvando um produto com nome e preco nulo e retornando throw IlegalArgumentException")
    @Order(8)
    void save_ThrowIlegalArgumentException_WhenNameIsNull() throws Exception {
        var request = readResourceFile("produto/post-produto-request-name-preco-null-500.json");

        var produto = Produto
                .builder()
                .id(3L)
                .nome(null)
                .descricao(null)
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
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.status().reason("O nome e descrição do produto deve ser incluído no cadastro!"));
    }

    @Test
    @DisplayName("DELETE v1/produtos/1 - Deletando um produto pelo id")
    @Order(9)
    void deleteById_removesProduto_WhenSucessFull() throws Exception {
        var id = 1L;
        BDDMockito.when(produtoData.getProdutoList()).thenReturn(produtoList);

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/produtos/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("DELETE v1/produtos/90 - Deletando um produto com id inexistente e lançando throw ResponseStatusException")
    @Order(10)
    void deleteById_ThrowResponseStatusException_WhenIdIsNotFound() throws Exception {
        var id = 90L;
        BDDMockito.when(produtoData.getProdutoList()).thenReturn(produtoList);

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/produtos/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("Produto não encontrado"));
    }

    @Test
    @DisplayName("PUT v1/produtos - Atualizando produto")
    @Order(11)
    void update_UpdateProduto_WhenSucessFull() throws Exception {
        var request = readResourceFile("produto/put-produto-request-204.json");
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
    @DisplayName("PUT v1/produtos - Atualizando produto com id inexistente e lançando throw ResponseStatusException")
    @Order(12)
    void update_UpdateProduto_WhenIdIsNotFound() throws Exception {
        var request = readResourceFile("produto/put-produto-request-404.json");
        BDDMockito.when(produtoData.getProdutoList()).thenReturn(produtoList);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/v1/produtos")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("Produto não encontrado"));
    }

    private String readResourceFile(String fileName) throws IOException {
        var file = resourceLoader.getResource("classpath:%s".formatted(fileName)).getFile();
        return new String(Files.readAllBytes(file.toPath()));
    }
}