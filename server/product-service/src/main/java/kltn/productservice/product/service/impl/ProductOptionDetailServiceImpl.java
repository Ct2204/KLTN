
package kltn.productservice.product.service.impl;

import kltn.productservice.common.entity.ProductOptionDetailEntity;
import kltn.productservice.common.repository.ProductOptionDetailRepository;
import kltn.productservice.product.dto.ProductOptionDetailDto;
import kltn.productservice.product.converter.ProductOptionDetailConverter;
import kltn.productservice.product.service.ProductOptionDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductOptionDetailServiceImpl implements ProductOptionDetailService {
    @Autowired
    private ProductOptionDetailConverter productOptionDetailConverter;

    @Autowired
    private ProductOptionDetailRepository productOptionDetailRepository;

    /**
     * *
     * Find exactly Product Option Detail By productId and productOptionDetailId
     *
     * @param productId             This is id of product.
     * @param productOptionDetailId This is id of product option detail.
     * @return Product Option Detail with a List of Image and Video in each Product Option Detail
     */
    public ProductOptionDetailDto findProductOptionDetailByIdAndByProductId(Long productId, Long productOptionDetailId) {
        ProductOptionDetailEntity productOptionDetail
                = this.productOptionDetailRepository.findProductOptionDetailByProductId(productId, productOptionDetailId);

        if(productOptionDetail == null) {
            return null;
        }

        return this.productOptionDetailConverter.toDto(productOptionDetail);
    }
}
