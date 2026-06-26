package api.inventory.controller;

import api.inventory.dtos.ProdutoPutRequestDTO;
import api.inventory.dtos.ProdutoRequestDTO;
import api.inventory.dtos.ProdutoResponseDTO;
import api.inventory.mapper.ProdutoMapper;
import api.inventory.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/produtos")
@RequiredArgsConstructor
@Log4j2
public class ProdutoController {
    private final ProdutoMapper mapper;
    private final ProdutoService service;

    @GetMapping()
    public ResponseEntity<List<ProdutoResponseDTO>> findAll() {
        log.debug("Procurando produtos...");

        var produtos = service.findAll();
        List<ProdutoResponseDTO> produtoResponseDTOS = mapper.toProdutoResponseDTOList(produtos);

        log.debug("Produtos encontrados: {}", produtoResponseDTOS.size());
        return ResponseEntity.ok().body(produtoResponseDTOS);
    }

    @GetMapping("{id}")
    public ResponseEntity<ProdutoResponseDTO> findById(@PathVariable Long id) {
        log.debug("Procurando produto com id: {}", id);

        var produto = service.findById(id);
        var produtoResponse = mapper.toProdutoResponseDTO(produto);

        log.debug("Produto encontrado: {}", produtoResponse);
        return ResponseEntity.ok().body(produtoResponse);
    }

    @GetMapping("filterName")
    public ResponseEntity<List<ProdutoResponseDTO>> listAllName(@RequestParam String nome) {
        log.debug("Procurando produtos com nome: {}", nome);

        var produto = service.listAllName(nome);
        var produtoResponse = mapper.toProdutoResponseDTOList(produto);

        log.debug("Produtos encontrados: {}", produtoResponse.size());
        return ResponseEntity.ok().body(produtoResponse);
    }

    @PostMapping()
    public ResponseEntity<Void> save(@RequestBody ProdutoRequestDTO produtoRequestDTO) {
        log.debug("Salvando produto...");

        var produto = mapper.toProdutoRequestDTO(produtoRequestDTO);
        service.save(produto);

        log.debug("Produto Salvo!");
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        log.debug("Deletando produto id: {}", id);
        service.deleteById(id);

        log.debug("Produto Deletado!");
        return ResponseEntity.noContent().build();
    }

    @PutMapping()
    public ResponseEntity<Void> update(@RequestBody ProdutoPutRequestDTO produtoPutRequestDTO) {
        log.debug("Atualizando produto: {}", produtoPutRequestDTO.getNome());

        var produtoRequest = mapper.toProdutoPutRequestDTO(produtoPutRequestDTO);
        service.update(produtoRequest);

        log.debug("Produto atualizado!");
        return ResponseEntity.noContent().build();
    }
}
