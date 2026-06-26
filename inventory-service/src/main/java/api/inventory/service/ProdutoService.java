package api.inventory.service;

import api.inventory.model.Produto;
import api.inventory.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProdutoService {
    private final ProdutoRepository repository;

    @Autowired
    public ProdutoService(ProdutoRepository repository) {
        this.repository = repository;
    }

    public List<Produto> findAll() {
        return repository.findAll();
    }

    public Produto findById(Long id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));
    }

    public List<Produto> listAllName(String nome) {
        return nome == null ? repository.findAll() : repository.listAllName(nome);
    }

    public void save(Produto produto) {
        repository.save(produto);
    }

    public void deleteById(Long id) {
        Produto produto = findById(id);
        repository.deleteById(produto.getId());
    }

    public void update(Produto produto) {
        Produto produtoUpdated = findById(produto.getId());
        repository.update(produtoUpdated);
    }

}
