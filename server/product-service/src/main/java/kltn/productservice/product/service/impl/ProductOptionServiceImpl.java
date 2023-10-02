
package kltn.productservice.product.service.impl;

import kltn.productservice.common.entity.ProductOptionEntity;
import kltn.productservice.common.repository.ProductOptionRepository;
import kltn.productservice.product.dto.ProductOptionDto;
import kltn.productservice.product.converter.ProductOptionConverter;
import kltn.productservice.product.service.ProductOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductOptionServiceImpl implements ProductOptionService {
    @Autowired
    private ProductOptionRepository productOptionRepository;

    @Autowired
    private ProductOptionConverter productOptionConverter;

    /**
     * *
     * Find Product Options ByProduct id
     *
     * @param productId This is id of product.
     * @return List of Product Options with a List of Image and Video in each Product Option Detail on each Option
     */
    public List<ProductOptionDto> findProductOptionByProductId(Long productId) {
        List<ProductOptionEntity> pageListEntity
                = this.productOptionRepository.findProductOptionByProductId(productId);

        if (pageListEntity.isEmpty()) {
            return null;
        }

        return pageListEntity.stream()
                .map(item -> this.productOptionConverter.toDto(item))
                .collect(Collectors.toList());
    }
}
