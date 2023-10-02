
package kltn.productservice.product.converter;

import kltn.productservice.common.entity.ProductEntity;
import kltn.productservice.common.repository.ProductRatingRepository;
import kltn.productservice.product.dto.ProductDto;
import kltn.productservice.product.dto.ProductVisualDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductConverter {
    @Autowired
    private ProductVisualConverter productVisualConverter;

    @Autowired
    private ProductRatingRepository productRatingRepository;


    /**
     * Convert ProductEntity to ProductDto
     * @param entity ProductEntity
     * @return ProductDto with List ProductVisualDto
     */
    public ProductDto toDto(ProductEntity entity){
        ProductDto dto = new ProductDto();
        dto.setId(entity.getId());
        dto.setSlug(entity.getSlug());
        dto.setSku(entity.getSlug());
        dto.setCategoryId(entity.getCategory().getId());
        dto.setTitle(entity.getTitle());
        dto.setQuantityInStock(entity.getQuantityInStock());
        dto.setStatus(entity.getStatus());
        dto.setPrice(entity.getPrice());
        dto.setPercentDiscount(entity.getPercentDiscount());
        // Calculate priceSales from percentDiscount and do logic for percentDiscount in database
        BigDecimal price = entity.getPrice();
        BigDecimal percentDiscount = entity.getPercentDiscount();
        BigDecimal hundred = BigDecimal.valueOf(100);
        BigDecimal priceSales;
        if (percentDiscount != null) {
            priceSales = price.subtract(price.multiply(percentDiscount).divide(hundred, RoundingMode.HALF_UP));
        } else {
            priceSales = price;
        }
        dto.setPriceSales(priceSales);

        // Calculate average number of stars and round 1 number after decimal point
        long countRating = this.productRatingRepository.countAllRatingByProductId(entity.getId());
        if (countRating > 0) {
            float sumStar = this.productRatingRepository.sumStarOfProduct(entity.getId());
            float starRating = (float) Math.round(sumStar / countRating * 10) / 10;
            dto.setStar_rating(starRating);
        }

        List<ProductVisualDto> listMediaProduct
                = entity.getProductVisuals()
                .stream()
                .map(e -> productVisualConverter.toDto(e))
                .collect(Collectors.toList());
        dto.setListMediaProduct(listMediaProduct);
        return dto;
    }
}
