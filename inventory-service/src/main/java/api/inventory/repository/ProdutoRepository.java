package api.inventory.repository;

import api.inventory.model.Produto;
import api.inventory.model.ProdutoData;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class ProdutoRepository {
    private final ProdutoData produtoData;

    public List<Produto> findAll() {
        return produtoData.getProdutoList();
    }

    public Optional<Produto> findById(Long id) {
        return produtoData.getProdutoList()
                .stream()
                .filter(produto -> produto.getId().equals(id))
                .findFirst();
    }

    public List<Produto> listAllName(String nome) {
        return produtoData.getProdutoList()
                .stream()
                .filter(produto -> produto.getNome().equalsIgnoreCase(nome))
                .toList();
    }

    public void save(Produto produto) {
        produtoData.getProdutoList().add(produto);
    }

    public void deleteById(Long id) {
        produtoData.getProdutoList().removeIf(produto -> produto.getId().equals(id));
    }

    public void update(Produto produto) {
        deleteById(produto.getId());
        save(produto);
    }
}
