
package kltn.productservice.product.service.impl;

import kltn.productservice.common.entity.ProductDetailEntity;
import kltn.productservice.common.exception.ResourceNotFoundException;
import kltn.productservice.common.repository.ProductDetailRepository;
import kltn.productservice.product.dto.ProductDetailDto;
import kltn.productservice.product.converter.ProductDetailConverter;
import kltn.productservice.product.service.ProductDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductDetailServiceImpl implements ProductDetailService {
    @Autowired
    private ProductDetailRepository productDetailRepository;

    @Autowired
    private ProductDetailConverter productDetailConverter;

    /**
     * *
     * Find Product Detail ByProduct id
     *
     * @param productId This is id of product.
     * @return List of Product Detail for a product
     * @throws ResourceNotFoundException
     */
    public List<ProductDetailDto> getProductDetailByIdProduct(Long productId) {

        List<ProductDetailEntity> pageListEntity
                = this.productDetailRepository.findProductDetailByProduct(productId);

        if (pageListEntity.isEmpty()) {
            return null;
        }

        return pageListEntity.stream()
                .map(item -> this.productDetailConverter.toDto(item))
                .collect(Collectors.toList());
    }
}
