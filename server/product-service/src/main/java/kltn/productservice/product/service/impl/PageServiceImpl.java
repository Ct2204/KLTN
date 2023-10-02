

package kltn.productservice.product.service.impl;

import kltn.productservice.common.entity.ProductCategoryEntity;
import kltn.productservice.common.entity.ProductEntity;
import kltn.productservice.common.entity.ProductRatingEntity;
import kltn.productservice.common.entity.ProductVisualEntity;
import kltn.productservice.common.repository.ProductCategoryRepository;
import kltn.productservice.common.repository.ProductRepository;
import kltn.productservice.product.dto.ProductCategoryDto;
import kltn.productservice.product.dto.ProductSearchByCategoryPageResDto;
import kltn.productservice.product.dto.ProductSearchPageDto;
import kltn.productservice.product.dto.ProductSearchResDto;
import kltn.productservice.product.service.PageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PageServiceImpl implements PageService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    private final static Logger logger = LoggerFactory.getLogger(PageServiceImpl.class);

    /**
     * This method help return a list product of a result search by text and all information of that page
     * @param keyword: enter the keyword to find
     * @param pageNumber: name of current page
     * @param size: number of element in a page
     * @return: all the information about page is contained in PageResDTO object
     */
    @Override
    public ProductSearchPageDto pageableListResultSearch(String keyword, int pageNumber, int size) {

        Pageable pageable =PageRequest.of(pageNumber-1,size);

        // get a list of product search by text
        Page<ProductSearchResDto> page = this.productRepository.getPageResultSearch(keyword, pageable);
        ProductSearchPageDto pageResDTO = new ProductSearchPageDto(
                page.getTotalPages(),
                page.getTotalElements(),
                size,
                page.getNumberOfElements(),
                pageNumber,
                page.isFirst(),
                page.isLast(),
                page.getContent()

        );

        return pageResDTO;
    }

    /**
     * Get the products by category and pageable
     * @param parent_id     enter category id which you want to get products
     * @param pageNumber: name of current page
     * @param size: number of element in a page
     * @return all the information about page is contained in PageResDTO object
     */
    @Override
    public ProductSearchByCategoryPageResDto pageListProductWithCategory(short parent_id, int pageNumber, int size) {

        Pageable pageable = PageRequest.of(pageNumber-1,size);

        // get list product find by parent_id of category
        Page<ProductEntity> page = this.productRepository.findProductsByCategoryId(parent_id, pageable);

        // get a list of subcategories
        List<ProductCategoryEntity> listChildCate = this.productCategoryRepository.findByParentId((short) parent_id);

        // convert  productCategoryEntity to ProductCategoryDTO
        List<ProductCategoryDto> productCategoryDTOList = listChildCate.stream()
                .map((productCategoryEntity) -> {

                            logger.info("get product category" + productCategoryEntity.toString());
                            ProductCategoryDto productCategoryDTO = new ProductCategoryDto();

                            productCategoryDTO.setId(productCategoryEntity.getId());
                            productCategoryDTO.setTitle(productCategoryEntity.getTitle());
                            productCategoryDTO.setSlug(productCategoryEntity.getSlug());
                            productCategoryDTO.setDescription(productCategoryEntity.getDescriptions());
                            productCategoryDTO.setParent_id(productCategoryEntity.getParentId());
                            productCategoryDTO.setStatus(productCategoryEntity.getStatus());
                            productCategoryDTO.setCreateAt(productCategoryEntity.getCreatedAt());
                            productCategoryDTO.setUpdateAt(productCategoryEntity.getUpdatedAt());

                            return productCategoryDTO;
                        }
                ).collect(Collectors.toList());

        return page.getTotalElements()>0 ? new ProductSearchByCategoryPageResDto(
                page.getTotalPages(),
                page.getTotalElements(),
                size,
                page.getNumberOfElements(),
                pageNumber,
                page.isFirst(),
                page.isLast(),
                productCategoryDTOList,

                page.getContent().stream().map(
                        (item)->{
                            ProductSearchResDto productResDto = new ProductSearchResDto(
                                    item.getId(),

                                    //  get the first url in list images of a product
                                    item.getProductVisuals().stream()
                                            .findFirst().map(ProductVisualEntity::getUrl)
                                            .orElse(null) ,
                                    item.getTitle(),
                                    // get average rating star of all product review comments
                                    item.getProductRatings().stream().mapToDouble(
                                            ProductRatingEntity::getStarRating
                                    ).average().orElse(Double.NaN),
                                    item.getPrice(),
                                    item.getPriceSales()
                            );
                            return productResDto;
                        }

                ).collect(Collectors.toList())):null;
    }
}


