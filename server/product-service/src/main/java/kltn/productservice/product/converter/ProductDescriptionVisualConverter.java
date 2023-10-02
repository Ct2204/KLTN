
package kltn.productservice.product.converter;

import kltn.productservice.common.entity.ProductDescriptionVisual;
import kltn.productservice.product.dto.ProductDescriptionVisualDto;
import org.springframework.stereotype.Component;

@Component
public class ProductDescriptionVisualConverter {

    /**
     * Convert ProductDescriptionVisualEntity to ProductDescriptionVisualDto
     * @param entity ProductDescriptionVisualEntity
     * @return ProductDescriptionVisualDto
     */
    public ProductDescriptionVisualDto toDto(ProductDescriptionVisual entity){
        ProductDescriptionVisualDto dto = new ProductDescriptionVisualDto();
        dto.setId(entity.getId());
        dto.setType(entity.getType());
        dto.setUrl(entity.getUrl());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
}
