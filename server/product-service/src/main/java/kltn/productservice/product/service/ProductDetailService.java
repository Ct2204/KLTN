
package kltn.productservice.product.service;


import kltn.productservice.product.dto.ProductDetailDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductDetailService {

    public List<ProductDetailDto> getProductDetailByIdProduct(Long productId);
}
