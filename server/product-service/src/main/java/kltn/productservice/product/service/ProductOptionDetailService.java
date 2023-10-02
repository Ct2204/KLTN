
package kltn.productservice.product.service;

import kltn.productservice.product.dto.ProductOptionDetailDto;

public interface ProductOptionDetailService {
    public ProductOptionDetailDto findProductOptionDetailByIdAndByProductId(Long productId, Long productOptionDetailId);
}
