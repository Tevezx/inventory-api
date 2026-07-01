package api.inventory.controller;

import api.inventory.model.Produto;
import api.inventory.model.ProdutoData;
import api.inventory.repository.ProdutoRepository;
import org.junit.jupiter.api.*;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
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
    @DisplayName("GET v1/produtos retornando todos os produtos cadastrados")
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
    @DisplayName("Retornando o produto pelo id")
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

    private String readResourceFile(String fileName) throws IOException {
        var file = resourceLoader.getResource("classpath:%s".formatted(fileName)).getFile();
        return new String(Files.readAllBytes(file.toPath()));
    }
}