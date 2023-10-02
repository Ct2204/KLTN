
package kltn.productservice.product.service;

import kltn.productservice.common.vo.ProductStatusType;
import kltn.productservice.product.dto.ProductDto;
import kltn.productservice.product.dto.ProductItemsResDto;
import kltn.productservice.product.dto.ProductListDto;

public interface ProductService {
    public ProductDto findProductByProductId(Long productId);

    ProductListDto findPageProduct(
            int perPage, int currentPage);

    //LINH

    ProductItemsResDto getProduct(Long id);

    void checkUpdateStatus(Long productItemId);
}
