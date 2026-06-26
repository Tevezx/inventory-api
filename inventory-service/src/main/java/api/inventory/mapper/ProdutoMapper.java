package api.inventory.mapper;

import api.inventory.dtos.ProdutoRequestDTO;
import api.inventory.dtos.ProdutoResponseDTO;
import api.inventory.model.Produto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProdutoMapper {
    @Mapping(target = "id", expression = "java(java.util.concurrent.ThreadLocalRandom.current().nextLong(1000L))")
    Produto toProdutoRequestDTO(ProdutoRequestDTO produtoRequestDTO);
    ProdutoResponseDTO toProdutoResponseDTO(Produto produto);

    List<ProdutoResponseDTO> toProdutoResponseDTOList(List<Produto> produtos);
}
