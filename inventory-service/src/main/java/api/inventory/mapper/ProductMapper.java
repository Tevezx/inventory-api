package api.inventory.mapper;

import api.inventory.dtos.ProductPutRequestDTO;
import api.inventory.dtos.ProductRequestDTO;
import api.inventory.dtos.ProductResponseDTO;
import api.inventory.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper {
    Product toProductRequestDTO(ProductRequestDTO productRequestDTO);
    ProductResponseDTO toProductResponseDTO(Product product);

    Product toProductPutRequestDTO(ProductPutRequestDTO productPutRequestDTO);

    List<ProductResponseDTO> toProductResponseDTOList(List<Product> products);
}
