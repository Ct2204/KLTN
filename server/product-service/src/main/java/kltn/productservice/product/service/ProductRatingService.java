
package kltn.productservice.product.service;

import kltn.productservice.product.dto.PageRatingResDto;
import kltn.productservice.product.dto.ProductRatingReqDto;
import kltn.productservice.product.dto.UserDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

@Service
public interface ProductRatingService {

    PageRatingResDto findAllProductRatingByProductId(Long productId,int perPage, int currentPage);

    void saveProductRating(Long userId, ProductRatingReqDto productRating);

    Long getUser(Long userId);
}
