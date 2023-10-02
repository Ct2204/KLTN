
package kltn.productservice.seller.service;

import kltn.productservice.seller.dto.ProductDescriptionDto;
import kltn.productservice.seller.dto.ProductDetailsDto;
import kltn.productservice.seller.dto.ProductDto;
import kltn.productservice.seller.dto.ProductOptionWithItemDto;
import kltn.productservice.seller.dto.ProductDetailsDto;
import kltn.productservice.seller.dto.ProductOptionWithItemDto;
import org.springframework.web.multipart.MultipartFile;

public interface SellerService {

    Long saveProduct(ProductDto productDto);

    void updateProduct(ProductDto productDto);

    void saveProductDetail(ProductDetailsDto productDetailsDto);

    void saveProductDescription(ProductDescriptionDto productDescriptionDto);

    void saveProductOptionWithItem(ProductOptionWithItemDto productOptionWithItemDto);

    String uploadProductFile(MultipartFile file);

    ProductOptionWithItemDto getProductOptionWithItem(Long id);

    ProductDescriptionDto getProductDescription(Long id);

    ProductDto getProduct(Long id);

    ProductDetailsDto getProductDetails(Long id);

}
