
package kltn.productservice.product.service;


import kltn.productservice.product.dto.CartItemDto;
import kltn.productservice.product.dto.ProductItemDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface ProductItemService {
    public List<ProductItemDto> findProductItemsByProductId(Long productId);


    //linh
    ProductItemDto findOneByProductItemId(Long productItemId);

    void updateListQuantityOfProductItem(Map<Long, Integer> mapProductItemIdAndQuantityCancel);

    Map<Long, ProductItemDto> getMapCartAndProductItem(List<CartItemDto> listCartItem);

}
