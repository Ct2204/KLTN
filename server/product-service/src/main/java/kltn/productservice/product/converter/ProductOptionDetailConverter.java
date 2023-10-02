
package kltn.productservice.product.converter;

import kltn.productservice.common.entity.ProductOptionDetailEntity;
import kltn.productservice.product.dto.ProductOptionDetailDto;
import kltn.productservice.product.dto.ProductVisualDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductOptionDetailConverter {
    @Autowired
    private ProductVisualConverter productVisualConverter;

    /**
     * Convert ProductOptionDetailEntity to ProductOptionDetailDto
     *
     * @param entity ProductOptionDetailEntity
     * @return ProductOptionDetailDto with List ProductVisual prefer to productOptionDetailId
     */
    public ProductOptionDetailDto toDto(ProductOptionDetailEntity entity) {
        ProductOptionDetailDto dto = new ProductOptionDetailDto();
        dto.setId(entity.getId());
        dto.setProductOptionId(entity.getProductOption().getId());
        dto.setValue(entity.getValue());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        dto.setProductId(entity.getProduct().getId());
        List<ProductVisualDto> listProductOptionDetailVisuals
                = entity.getProductVisuals()
                .stream()
                .map(e -> productVisualConverter.toDto(e))
                .collect(Collectors.toList());
        dto.setListProductOptionDetailVisuals(listProductOptionDetailVisuals);
        return dto;
    }
}
