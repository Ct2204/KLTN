
package kltn.productservice.product.converter;

import kltn.productservice.common.entity.ProductItemDetailEntity;
import kltn.productservice.product.dto.ProductItemDetailDto;
import org.springframework.stereotype.Component;

@Component
public class ProductItemDetailConverter {
    public ProductItemDetailDto toDto(ProductItemDetailEntity entity){
        ProductItemDetailDto dto = new ProductItemDetailDto();
        dto.setId(entity.getId());
        dto.setProductItemId(entity.getProductItem().getId());
        dto.setProductOptionDetailId(entity.getProductOptionDetail().getId());
        return dto;
    }
}
