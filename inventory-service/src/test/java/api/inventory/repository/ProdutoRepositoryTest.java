package api.inventory.repository;

import api.inventory.model.Produto;
import api.inventory.model.ProdutoData;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProdutoRepositoryTest {
    @InjectMocks
    private ProdutoRepository repository;
    @Mock
    private ProdutoData produtoData;
    private final List<Produto> produtoList = new ArrayList<>();

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
    @DisplayName("Retornando todos os produtos")
    @Order(1)
    void findAll_ReturnProduto_WhenProdutoIsFound() {
        BDDMockito.when(produtoData.getProdutoList()).thenReturn(produtoList);

        var produtos = repository.findAll();
        Assertions.assertThat(produtos).isNotNull().isNotEmpty().hasSize(produtoList.size());
    }

    @Test
    @DisplayName("Retornando o produto pelo id")
    @Order(2)
    void findById_ReturnsProduto_WhenProdutoIdIsFound() {
        BDDMockito.when(produtoData.getProdutoList()).thenReturn(produtoList);

        var produtoExpected = produtoList.getFirst();
        var produtoById = repository.findById(produtoExpected.getId());

        Assertions.assertThat(produtoById).contains(produtoExpected).isPresent();
    }

    @Test
    @DisplayName("Retornando uma lista vazia, caso o parametro seja null")
    @Order(3)
    void listAllName_ReturnsEmpty_WhenNameIsNull() {
        BDDMockito.when(produtoData.getProdutoList()).thenReturn(produtoList);

        var produto = repository.listAllName(null);
        Assertions.assertThat(produto).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("Retornando uma lista de nomes dos produtos encontrados")
    @Order(4)
    void listAllName_ReturnsProdutos_WhenNameIsFound() {
        BDDMockito.when(produtoData.getProdutoList()).thenReturn(produtoList);

        var produtoExpected = produtoList.getFirst();
        var produto = repository.listAllName(produtoExpected.getNome());
        Assertions.assertThat(produto).isNotNull().contains(produtoExpected);
    }

    @Test
    @DisplayName("Salvando o produto passado no bodyrequest")
    @Order(5)
    void save_CreatesProduto_WhenSucessFull() {
        BDDMockito.when(produtoData.getProdutoList()).thenReturn(produtoList);

        var produtoCreated = Produto
                .builder()
                .id(3L)
                .nome("Fone Logitech")
                .descricao("Fone logitech Gamer")
                .preco(150.0)
                .qtdEstoque(4)
                .build();

        repository.save(produtoCreated);
        Assertions.assertThat(produtoCreated).hasNoNullFieldsOrProperties();
    }

    @Test
    @DisplayName("Deletando um produto pelo id")
    @Order(6)
    void deleteById_RemovesProduto_WhenSucessFull() {
        BDDMockito.when(produtoData.getProdutoList()).thenReturn(produtoList);

        var produto = produtoList.getFirst();
        repository.deleteById(produto.getId());

        var findAllProdutos = repository.findAll();

        Assertions.assertThat(findAllProdutos).doesNotContain(produto);
    }

    @Test
    @DisplayName("Atualizando um produto pelo id")
    @Order(7)
    void update_UpdatesProduto_WhenSucessFull() {
        BDDMockito.when(produtoData.getProdutoList()).thenReturn(produtoList);

        var produtos = repository.findAll();
        var produto = produtoList.getFirst();
        produto.setDescricao("Mouse Razer");

        repository.update(produto);

        Assertions.assertThat(produtos).contains(produto);
        Assertions.assertThat(produto).hasNoNullFieldsOrProperties();
    }
}