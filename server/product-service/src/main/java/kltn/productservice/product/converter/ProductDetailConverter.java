
package kltn.productservice.product.converter;

import kltn.productservice.common.entity.ProductDetailEntity;
import kltn.productservice.product.dto.ProductDetailDto;
import org.springframework.stereotype.Component;

@Component
public class ProductDetailConverter {
    public ProductDetailDto toDto(ProductDetailEntity entity){
        ProductDetailDto dto = new ProductDetailDto();
        dto.setId(entity.getId());
        dto.setProductId(entity.getProduct().getId());
        dto.setKey(entity.getKey());
        dto.setValue(entity.getValue());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
}
