package api.inventory.service;

import api.inventory.exception.InternalErrorException;
import api.inventory.exception.NotFoundException;
import api.inventory.model.Produto;
import api.inventory.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
                .orElseThrow(() -> new NotFoundException("Produto não encontrado"));
    }

    public List<Produto> listAllName(String nome) {
        return nome == null || nome.isEmpty() ? repository.findAll() : repository.listAllName(nome);
    }

    public Produto save(Produto produto) {
        if (produto.getNome() == null || produto.getNome().isEmpty() || produto.getDescricao() == null || produto.getDescricao().isEmpty()) {
            throw new InternalErrorException("O nome e descrição do produto deve ser incluído no cadastro!");
        }

        if (produto.getPreco() == null || produto.getPreco() <= 0) {
            throw new InternalErrorException("O preço do produto deve ser maior ou igual a zero!");
        }

        return repository.save(produto);
    }

    public void deleteById(Long id) {
        Produto produto = findById(id);
        repository.deleteById(produto.getId());
    }

    public void update(Produto produto) {
        findById(produto.getId());
        repository.update(produto);
    }

}
