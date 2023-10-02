
package kltn.productservice.product.converter;

import kltn.productservice.common.entity.ProductRatingVisualEntity;
import kltn.productservice.product.dto.ProductRatingVisualDto;
import org.springframework.stereotype.Component;

@Component
public class ProductRatingVisualConverter {

    /**
     * Convert ProductEntity to ProductRatingVisualDto
     * @param entity ProductRatingVisualEntity
     * @return ProductRatingVisualDto
     */
    public ProductRatingVisualDto toDto(ProductRatingVisualEntity entity){
        ProductRatingVisualDto dto = new ProductRatingVisualDto();
        dto.setId(entity.getId());
        dto.setType(entity.getType());
        dto.setUrl(entity.getUrl());
        dto.setProductRatingId(entity.getRating().getId());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
}
