
package kltn.productservice.product.converter;

import kltn.productservice.common.entity.ProductDescriptionEntity;
import kltn.productservice.product.dto.ProductDescriptionDto;
import kltn.productservice.product.dto.ProductDescriptionVisualDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductDescriptionConverter {
    @Autowired
    private ProductDescriptionVisualConverter productDescriptionVisualConverter;

    /**
     * Convert ProductDescriptionEntity to ProductDescriptionDto
     * @param entity ProductDescriptionEntity
     * @return ProductDescriptionDto with List ProductDescriptionVisualDto
     */
    public ProductDescriptionDto toDto(ProductDescriptionEntity entity){
        ProductDescriptionDto dto = new ProductDescriptionDto();
        dto.setProductId(entity.getProduct().getId());
        dto.setDescription(entity.getDescriptions());
        List<ProductDescriptionVisualDto> listMedia
                = entity.getProductDescriptionVisuals()
                .stream()
                .map(e -> productDescriptionVisualConverter.toDto(e))
                .collect(Collectors.toList());
        dto.setListMedia(listMedia);
        return dto;
    }
}
