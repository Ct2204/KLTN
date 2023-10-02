
package kltn.productservice.product.converter;

import kltn.productservice.common.entity.ProductOptionEntity;
import kltn.productservice.product.dto.ProductOptionDetailDto;
import kltn.productservice.product.dto.ProductOptionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductOptionConverter {
    @Autowired
    private ProductOptionDetailConverter productOptionDetailConverter;

    /**
     * Convert ProductOptionEntity to ProductOptionDto
     * @param entity  ProductOptionEntity
     * @return ProductOptionDto with List ProductOptionDetailDto
     */
    public ProductOptionDto toDto(ProductOptionEntity entity){
        ProductOptionDto dto = new ProductOptionDto();
        dto.setId(entity.getId());
        dto.setProductId(entity.getProduct().getId());
        dto.setOption(entity.getOption());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        List<ProductOptionDetailDto> listProductOptionDetail
                = entity.getProductOptionDetails()
                .stream()
                .map(e -> productOptionDetailConverter.toDto(e))
                .collect(Collectors.toList());
        dto.setListProductOptionDetail(listProductOptionDetail);
        return dto;
    }
}
