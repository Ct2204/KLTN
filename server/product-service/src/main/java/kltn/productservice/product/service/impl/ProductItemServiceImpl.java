
package kltn.productservice.product.service.impl;

import kltn.productservice.common.entity.ProductItemEntity;
import kltn.productservice.common.exception.ResourceNotFoundException;
import kltn.productservice.common.repository.ProductItemRepository;
import kltn.productservice.product.dto.CartItemDto;
import kltn.productservice.product.dto.ProductItemDto;
import kltn.productservice.product.converter.ProductItemConverter;
import kltn.productservice.product.service.ProductItemService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductItemServiceImpl implements ProductItemService {
    @Autowired
    private ProductItemRepository productItemRepository;

    @Autowired
    private ProductItemConverter productItemConverter;

    /**
     * *
     * This method handles get all Product Items by product id
     *
     * @param productId This is an ID of exist product.
     * @return List of Product Item with a List of Product Item Detail for each Item
     */
    public List<ProductItemDto> findProductItemsByProductId(Long productId) {

        List<ProductItemEntity> pageListEntity
                = this.productItemRepository.findAllItemsByProductId(productId);

        if (pageListEntity.isEmpty()) {
            return null;
        }

        return pageListEntity.stream()
                .map(item -> this.productItemConverter.toDto(item))
                .collect(Collectors.toList());
    }


    //linh
    @Override
    public ProductItemDto findOneByProductItemId(Long productItemId) {
        ProductItemEntity productItemEntity = this.productItemRepository.findById(productItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Product item not found!"));

        return this.productItemConverter.toDto(productItemEntity);

    }

    @Override
    @Transactional
    public void updateListQuantityOfProductItem(Map<Long, Integer> mapProductItemIdAndQuantityCancel) {
        for (Map.Entry<Long, Integer> entry : mapProductItemIdAndQuantityCancel.entrySet()) {
            Long productItemId = entry.getKey();
            Integer quantityCancel = entry.getValue();

            ProductItemEntity productItem = this.productItemRepository.findById(productItemId)
                    .orElseThrow(()-> new ResourceNotFoundException("Not exit this product item"));
            productItem.setQuantity(productItem.getQuantity()+quantityCancel);
            this.productItemRepository.save(productItem);
        }
    }

    @Override
    public Map<Long, ProductItemDto> getMapCartAndProductItem(List<CartItemDto> listCartItem) {
        Map<Long, ProductItemDto> mapCartAndProductItem= new HashMap<Long, ProductItemDto>();
        for(CartItemDto cartItem: listCartItem){
            ProductItemEntity entity = this.productItemRepository.findById(cartItem.getProductItemId())
                    .orElseThrow(()->new ResourceNotFoundException("Product item not found"));
            ProductItemDto dto = new ProductItemDto();
            dto.setId(entity.getId());
            dto.setProductId(entity.getProduct().getId());
            dto.setQuantity(entity.getQuantity());
            dto.setPrice(entity.getPrice());
            mapCartAndProductItem.put(cartItem.getCartItemId(), dto);
        }
        return mapCartAndProductItem;
    }


    @KafkaListener(id = "orderGroup",topics = "order-cancel-topic")
    public void processOrderCancel(Map<Long, Integer> mapProductItemIdAndQuantityCancel) {
        System.out.println(mapProductItemIdAndQuantityCancel);
       this.updateListQuantityOfProductItem(mapProductItemIdAndQuantityCancel);
    }

    @KafkaListener(id = "orderCancelGroup",topics = "order-topic")
    public void processOrder(Map<Long, Integer> mapProductItemIdAndQuantityOder) {
        this.updateListQuantityOfProductItem(mapProductItemIdAndQuantityOder);
    }


}
