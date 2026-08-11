package api.inventory.service;

import api.inventory.exception.InternalErrorException;
import api.inventory.exception.NotFoundException;
import api.inventory.model.Product;
import api.inventory.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository repository;

    @Autowired
    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public List<Product> findAll() {
        return repository.findAll();
    }

    public Product findById(Long id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));
    }

    public List<Product> listAllName(String nome) {
        return nome == null || nome.isEmpty() ? repository.findAll() : repository.findByName(nome);
    }

    public Product save(Product product) {
        if (product.getName() == null || product.getName().isEmpty() || product.getDescription() == null || product.getDescription().isEmpty()) {
            throw new IllegalArgumentException("The product name and description must be included in the registration!");
        }

        if (product.getPrice() == null || product.getPrice() <= 0) {
            throw new IllegalArgumentException("The product price must be greater than or equal to zero!");
        }

        return repository.save(product);
    }

    public void deleteById(Long id) {
        Product product = findById(id);
        repository.deleteById(product.getId());
    }

    public void update(Product product) {
        findById(product.getId());
        repository.save(product);
    }

}
