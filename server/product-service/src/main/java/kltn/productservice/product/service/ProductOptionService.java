
package kltn.productservice.product.service;


import kltn.productservice.product.dto.ProductOptionDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductOptionService {
    public List<ProductOptionDto> findProductOptionByProductId(Long productId);
}
