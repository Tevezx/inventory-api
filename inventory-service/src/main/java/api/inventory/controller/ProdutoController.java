package api.inventory.controller;

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
}
