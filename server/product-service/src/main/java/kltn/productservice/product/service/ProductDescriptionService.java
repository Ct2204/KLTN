
package kltn.productservice.product.service;


import kltn.productservice.product.dto.ProductDescriptionDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface  ProductDescriptionService {
    public List<ProductDescriptionDto> findProductDescriptionByProductId(Long productId);
}
