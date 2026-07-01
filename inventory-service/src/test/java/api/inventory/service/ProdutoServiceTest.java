package api.inventory.service;

import api.inventory.model.Produto;
import api.inventory.repository.ProdutoRepository;
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
class ProdutoServiceTest {
    @InjectMocks
    private ProdutoService service;

    @Mock
    private ProdutoRepository repository;
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
    @DisplayName("Retornando todos os produtos cadastrados")
    @Order(1)
    void findAll_ReturnsAllProdutos_WhenSucessFull() {
        BDDMockito.when(repository.findAll()).thenReturn(produtoList);

        var produtos = service.findAll();
        Assertions.assertThat(produtos).isNotNull().hasSameElementsAs(produtoList);
    }

    @Test
    @DisplayName("Retornando o produto pelo id")
    @Order(2)
    void findById_ReturnsProduto_WhenProdutoIdIsFound() {
        var produto = produtoList.getFirst();
        BDDMockito.when(repository.findById(produto.getId())).thenReturn(Optional.of(produto));

        Assertions.assertThatNoException().isThrownBy(() -> service.findById(produto.getId()));
    }

    @Test
    @DisplayName("Retornando o throw ResponseStatusException, caso o id não seja encontrado")
    @Order(3)
    void findById_ReturnsThrowResponseStatusException_WhenProdutoIdIsNotFound() {
        var produto = produtoList.getFirst();
        BDDMockito.when(repository.findById(produto.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException().isThrownBy(() -> service.findById(produto.getId())).isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("Retornando todos os produtos onde o nome seja igual ao parametro")
    @Order(4)
    void listAllName_ReturnsProdutos_WhenNameIsFound() {
        var produto = produtoList.getFirst();
        var produtoExpected = produtoList.stream().filter(produtos -> produtos.getNome().equalsIgnoreCase(produto.getNome())).toList();
        BDDMockito.when(repository.listAllName(produto.getNome())).thenReturn(produtoExpected);

        var produtoResponse = service.listAllName(produto.getNome());
        Assertions.assertThat(produtoExpected).hasSameElementsAs(produtoResponse).isNotNull();
    }

    @Test
    @DisplayName("Retornando todos os produtos, caso o parametro seja nulo")
    @Order(5)
    void listAllName_ReturnsProdutos_WhenNameIsNull() {
        BDDMockito.when(repository.findAll()).thenReturn(produtoList);

        var produtoResponse = service.listAllName(null);
        Assertions.assertThat(produtoResponse).isNotNull().hasSameElementsAs(produtoList);
    }

    @Test
    @DisplayName("Retornando uma lista vazia, caso o parametro seja branco")
    @Order(6)
    void listAllName_ReturnsProdutos_WhenNameIsBlank() {
        var produto = produtoList.getFirst();
        BDDMockito.when(repository.listAllName(produto.getNome())).thenReturn(Collections.emptyList());

        var produtoResponse = service.listAllName(produto.getNome());
        Assertions.assertThat(produtoResponse).isEmpty();
    }

    @Test
    @DisplayName("Salvando um produto")
    @Order(7)
    void save_CreatesProduto_WhenSucessFull() {
        var produto = Produto
                .builder()
                .id(3L)
                .nome("Fone Logitech")
                .descricao("Fone logitech gamer")
                .preco(240.0)
                .qtdEstoque(4)
                .build();

        BDDMockito.when(repository.save(produto)).thenReturn(produto);
        var produtoSaved = service.save(produto);

        Assertions.assertThat(produtoSaved).isEqualTo(produto).hasNoNullFieldsOrProperties();
    }

    @Test
    @DisplayName("Salvando um produto com nome e preco nulo e retornando throw IlegalArgumentException")
    @Order(8)
    void save_ThrowIlegalArgumentException_WhenNameIsNull() {
        var produto = Produto
                .builder()
                .id(3L)
                .nome(null)
                .descricao("Fone logitech gamer")
                .preco(null)
                .qtdEstoque(4)
                .build();

        Assertions.assertThatThrownBy(() -> service.save(produto)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Deletando um produto pelo id")
    @Order(9)
    void deleteById_removesProduto_WhenSucessFull() {
        var produto = produtoList.getFirst();
        BDDMockito.when(repository.findById(produto.getId())).thenReturn(Optional.of(produto));
        Assertions.assertThatNoException().isThrownBy(() -> service.deleteById(produto.getId()));
    }

    @Test
    @DisplayName("Deletando um produto com id inexistente e lançando throw ResponseStatusException")
    @Order(10)
    void deleteById_ThrowResponseStatusException_WhenIdIsNotFound() {
        var produto = produtoList.getFirst();
        BDDMockito.when(repository.findById(produto.getId())).thenReturn(Optional.empty());
        Assertions.assertThatException().isThrownBy(() -> service.deleteById(produto.getId())).isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("Atualizando produto")
    @Order(11)
    void update_UpdateProduto_WhenSucessFull() {
        var produto = produtoList.getFirst();
        produto.setNome("HeadSet Logitech");
        BDDMockito.when(repository.findById(produto.getId())).thenReturn(Optional.of(produto));

        Assertions.assertThatNoException().isThrownBy(() -> service.update(produto));
    }

    @Test
    @DisplayName("Atualizando produto com id inexistente e lançando throw ResponseStatusException")
    @Order(12)
    void update_UpdateProduto_WhenIdIsNotFound() {
        var produto = produtoList.getFirst();
        BDDMockito.when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThatException().isThrownBy(() -> service.update(produto)).isInstanceOf(ResponseStatusException.class);
    }
}