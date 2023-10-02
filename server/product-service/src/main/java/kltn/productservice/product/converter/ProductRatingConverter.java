
package kltn.productservice.product.converter;

import kltn.productservice.common.entity.ProductRatingEntity;
import kltn.productservice.product.dto.ProductRatingReqDto;
import kltn.productservice.product.dto.ProductRatingResDto;
import kltn.productservice.product.dto.ProductRatingVisualDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductRatingConverter {

    @Autowired
    private ProductRatingVisualConverter productRatingVisualConverter;

    /**
     * Convert ProductRatingDto to ProductRatingEntity
     * @param dto ProductRatingReqDto
     * @return ProductRatingEntity
     */
    public ProductRatingEntity toEntity(ProductRatingReqDto dto){
        ProductRatingEntity entity = new ProductRatingEntity();
        entity.setStarRating(dto.getStarRating());
        entity.setComment(dto.getComment());
        return entity;
    }

    /**
     * Convert ProductRatingEntity to ProductRatingDto
     * @param entity  ProductRatingEntity
     * @return ProductRatingResDto
     */
    public ProductRatingResDto toDto(ProductRatingEntity entity){
        ProductRatingResDto dto = new ProductRatingResDto();
        dto.setId(entity.getId());
        dto.setComment(entity.getComment());
        dto.setStarRating(entity.getStarRating());
        dto.setProductId(entity.getProduct().getId());
        dto.setUserId(entity.getUserId());

//        if(entity.getUser().getUserProfile()!=null){
//            dto.setFullName(entity.getUser().getUserProfile().getFullName());
//            dto.setAvatarUrl(entity.getUser().getUserProfile().getProfilePicture());
//        }
        List<ProductRatingVisualDto> listMedia
                = entity.getProductRatingVisuals()
                .stream()
                .map(e -> this.productRatingVisualConverter.toDto(e))
                .collect(Collectors.toList());

        dto.setListMedia(listMedia);
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
}
