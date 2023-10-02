
package kltn.productservice.product.service.impl;

import kltn.productservice.common.entity.ProductDescriptionEntity;
import kltn.productservice.common.repository.ProductDescriptionRepository;
import kltn.productservice.product.dto.ProductDescriptionDto;
import kltn.productservice.product.converter.ProductDescriptionConverter;
import kltn.productservice.product.service.ProductDescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductDescriptionServiceImpl implements ProductDescriptionService {
    @Autowired
    private ProductDescriptionRepository productDescriptionRepository;

    @Autowired
    private ProductDescriptionConverter productDescriptionConverter;

    /**
     * *
     * Find Product Description ByProduct ID
     *
     * @param productId This is id of product.
     * @return List of Product Description with a List of Image and Video (Visual Description) on each Description
     */
    public List<ProductDescriptionDto> findProductDescriptionByProductId(Long productId) {
        List<ProductDescriptionEntity> pageListEntity
                = this.productDescriptionRepository.findByProduct(productId);
        if (pageListEntity.isEmpty()) {
            return null;
        }

        return pageListEntity.stream()
                .map(item -> this.productDescriptionConverter.toDto(item))
                .collect(Collectors.toList());
    }
}
