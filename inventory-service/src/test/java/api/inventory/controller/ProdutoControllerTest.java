package api.inventory.controller;

import api.inventory.model.Produto;
import api.inventory.model.ProdutoData;
import api.inventory.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;

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

    private String readResourceFile(String fileName) throws IOException {
        var file = resourceLoader.getResource("classpath:%s".formatted(fileName)).getFile();
        return new String(Files.readAllBytes(file.toPath()));
    }
}