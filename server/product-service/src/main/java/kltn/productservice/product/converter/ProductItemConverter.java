
package kltn.productservice.product.converter;

import kltn.productservice.common.entity.ProductItemEntity;
import kltn.productservice.product.dto.ProductItemDetailDto;
import kltn.productservice.product.dto.ProductItemDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductItemConverter {
    @Autowired
    private ProductItemDetailConverter productItemDetailConverter;
    public ProductItemDto toDto(ProductItemEntity entity){
        ProductItemDto dto = new ProductItemDto();
        dto.setId(entity.getId());
        dto.setProductId(entity.getProduct().getId());
        dto.setPrice(entity.getPrice());
        dto.setQuantity(entity.getQuantity());
        List<ProductItemDetailDto> productItemDetails
                = entity.getProductItemDetails()
                .stream()
                .map(e -> productItemDetailConverter.toDto(e))
                .collect(Collectors.toList());
        //dto.setProductItemDetails(productItemDetails);
        return dto;
    }
}
