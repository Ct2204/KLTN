
package kltn.productservice.product.converter;

import kltn.productservice.common.entity.ProductVisualEntity;
import kltn.productservice.product.dto.ProductVisualDto;
import org.springframework.stereotype.Component;

@Component
public class ProductVisualConverter {
    public ProductVisualDto toDto(ProductVisualEntity entity){
        ProductVisualDto dto = new ProductVisualDto();
        dto.setId(entity.getId());
        dto.setSlug(entity.getSlug());
        dto.setType(entity.getType());
        dto.setUrl(entity.getUrl());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
}
