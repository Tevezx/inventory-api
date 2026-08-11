package api.inventory.controller;

import api.inventory.dtos.ProductPutRequestDTO;
import api.inventory.dtos.ProductRequestDTO;
import api.inventory.dtos.ProductResponseDTO;
import api.inventory.mapper.ProductMapper;
import api.inventory.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/products")
@RequiredArgsConstructor
@Log4j2
public class ProductController {
    private final ProductMapper mapper;
    private final ProductService service;

    @GetMapping()
    public ResponseEntity<List<ProductResponseDTO>> findAll() {
        log.debug("Finding products...");

        var product = service.findAll();
        List<ProductResponseDTO> productResponseDTOS = mapper.toProductResponseDTOList(product);

        log.debug("Products found: {}", productResponseDTOS.size());
        return ResponseEntity.ok().body(productResponseDTOS);
    }

    @GetMapping("{id}")
    public ResponseEntity<ProductResponseDTO> findById(@PathVariable Long id) {
        log.debug("Find product by id: {}", id);

        var product = service.findById(id);
        var productResponse = mapper.toProductResponseDTO(product);

        log.debug("Product found: {}", productResponse);
        return ResponseEntity.ok().body(productResponse);
    }

    @GetMapping("filterName")
    public ResponseEntity<List<ProductResponseDTO>> listAllName(@RequestParam(required = false) String name) {
        log.debug("Find product by name: {}", name);

        var product = service.listAllName(name);
        var productResponse = mapper.toProductResponseDTOList(product);

        log.debug("Products founds: {}", productResponse.size());
        return ResponseEntity.ok().body(productResponse);
    }

    @PostMapping()
    public ResponseEntity<ProductResponseDTO> save(@RequestBody @Valid ProductRequestDTO productRequestDTO) {
        log.debug("Saving product");

        var product = mapper.toProductRequestDTO(productRequestDTO);
        var productSaved = service.save(product);

        var productResponse = mapper.toProductResponseDTO(productSaved);

        log.debug("Product Saved");
        return ResponseEntity.status(HttpStatus.CREATED).body(productResponse);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        log.debug("Delete product by id: {}", id);
        service.deleteById(id);

        log.debug("Product Deleted");
        return ResponseEntity.noContent().build();
    }

    @PutMapping()
    public ResponseEntity<Void> update(@RequestBody @Valid ProductPutRequestDTO productPutRequestDTO) {
        log.debug("Updating product: {}", productPutRequestDTO.getName());

        var productRequest = mapper.toProductPutRequestDTO(productPutRequestDTO);
        service.update(productRequest);

        log.debug("Product updated");
        return ResponseEntity.noContent().build();
    }
}
