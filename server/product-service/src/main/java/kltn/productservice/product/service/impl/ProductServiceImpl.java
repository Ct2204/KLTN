
package kltn.productservice.product.service.impl;

import kltn.productservice.common.entity.*;
import kltn.productservice.common.exception.ResourceNotFoundException;
import kltn.productservice.common.repository.ProductItemRepository;
import kltn.productservice.common.repository.ProductRatingRepository;
import kltn.productservice.common.repository.ProductRepository;
import kltn.productservice.common.vo.ProductStatusType;
import kltn.productservice.product.dto.*;
import kltn.productservice.product.converter.ProductConverter;
import kltn.productservice.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductItemRepository productItemRepository;

    @Autowired
    private ProductConverter productConverter;

    @Autowired
    private ProductRatingRepository productRatingRepository;

    /**
     * *
     * Find All Product Information of a Product By Product id
     *
     * @param productId This is id of product.
     * @return All Information of product and all product Visual
     */
    public ProductDto findProductByProductId(Long productId) {

        Optional<ProductEntity> product
                = this.productRepository.findById(productId);

        if(product.isEmpty()){
            return null;
        }
        return this.productConverter.toDto(product.get());
    }

    /**
     * *
     * Find All Product on page and Pagination
     *
     * @return List of Product with List Image and Video into each Product and StarRating for each Product
     */
    public ProductListDto findPageProduct(
            int perPage, int currentPage) {

        Pageable paging = PageRequest.of(currentPage, perPage);

        Page<ProductEntity> pageListEntity
                = this.productRepository.findListProduct(paging);

        return pageListEntity.getTotalElements() > 0
                ? new ProductListDto(
                pageListEntity.getTotalPages(),
                pageListEntity.getTotalElements(),
                pageListEntity.getSize(),
                pageListEntity.getNumberOfElements(),
                pageListEntity.getNumber() + 1,
                pageListEntity.isFirst(),
                pageListEntity.isLast(),
                pageListEntity.getContent().stream()
                        .map((item) -> {
                            ProductDto dto= this.productConverter.toDto(item);
                            return dto;
                        })
                        .collect(Collectors.toList()))
                : null;
    }


    public ProductItemsResDto getProduct(Long id) {
        ProductEntity product = this.productRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " +id+" not found!"));

        ProductItemsResDto productDto = new ProductItemsResDto();
        List<ProductOptionEntity> listOption = product.getProductOptions();
        List<ProductItemEntity> listProductItem = product.getProductItems();
        Map<Long,Integer> map = new HashMap<>();
        int n = listOption.size();
        List<ProductOptionsDto> listOptionDto = new ArrayList<>();
        List<List<ProductOptionDetailImageDto>> images = new ArrayList<>();
        for( int i = 0; i < n; i++){
            // 3 or 2 Option
            //List of option detail
            ProductOptionsDto option = new ProductOptionsDto();
            option.setOption(listOption.get(i).getOption());
            List<String> listValue = new ArrayList<>();
            List<ProductOptionDetailEntity> listOptionDetail = listOption.get(i).getProductOptionDetails();
            List<ProductOptionDetailImageDto> imageVisuals = new ArrayList<>();
            for(int j = 0 ; j < listOptionDetail.size(); j++){
                //Key id option detail, value : index of optiondetail
                map.put(listOptionDetail.get(j).getId(),j);
                listValue.add(listOptionDetail.get(j).getValue());
                if(listOptionDetail.get(j).getProductVisuals().size() > 0){
                    imageVisuals = listOptionDetail
                            .get(j)
                            .getProductVisuals()
                            .stream()
                            .map(v -> toDto(v))
                            .collect(Collectors.toList());
                    images.add(imageVisuals);
                }
                if(images.size() > 0){
                    option.setImages(images);
                }
                option.setValues(listValue);
            }

            listOptionDto.add(option);
        }
        productDto.setOptions(listOptionDto);


        List<ProductItemsDto> listItemDto = new ArrayList<>();
        listProductItem.forEach(p -> System.out.println(p.getId()));
        listProductItem = listProductItem.stream().sorted(Comparator.comparing(ProductItemEntity::getId)).collect(Collectors.toList());
        listProductItem.forEach(p -> System.out.println(p.getId()));
        for(ProductItemEntity item : listProductItem){
            ProductItemsDto dto = new ProductItemsDto();
            dto.setId(item.getId());
            dto.setProductId(item.getProduct().getId());
            dto.setPrice(item.getPrice());
            dto.setQuantity(item.getQuantity());
            List<Integer> list = new ArrayList<>();
            List<ProductItemDetailEntity> listItemDetail = new ArrayList<>();
            listItemDetail = item.getProductItemDetails().stream().sorted(Comparator.comparing(ProductItemDetailEntity::getId)).collect(Collectors.toList());
            for(ProductItemDetailEntity itemDetail : listItemDetail){
                list.add(map.get(itemDetail.getProductOptionDetail().getId()));
            }
            dto.setOptionValueIndex(list);
            listItemDto.add(dto);
        }
        productDto.setProductItems(listItemDto);
        return productDto;
    }

    public ProductOptionDetailImageDto toDto(ProductVisualEntity entity){
        ProductOptionDetailImageDto dto = new ProductOptionDetailImageDto();
        dto.setType(entity.getType());
        dto.setUrl(entity.getUrl());
        return dto;
    }

    //linh
    @Override
    public void checkUpdateStatus(Long productItemId) {
        ProductItemEntity productItem = this.productItemRepository.findById(productItemId)
                .orElseThrow(()->new ResourceNotFoundException("Product item not found"));
        Long productQuantity = this.productItemRepository
                .getQuantityProduct(productItem.getProduct().getId());
        if (productQuantity == 0) {
            ProductEntity product = productItem.getProduct();
            product.setStatus(ProductStatusType.UNAVAILABLE);
            this.productRepository.save(product);
        }
    }

    @KafkaListener(id = "checkUpdateStatusGroup",topics = "check-status-product-topic")
    public void processOrder(List<Long> listProductItemId) {
        for(Long id: listProductItemId){
            this.checkUpdateStatus(id);
        }
    }
}
