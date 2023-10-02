
package kltn.productservice.product.controller;


import kltn.productservice.common.dto.ResponseDto;
import kltn.productservice.product.dto.*;
import kltn.productservice.product.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductDetailService productDetailService;

    @Autowired
    private ProductDescriptionService productDescriptionService;


    @Autowired
    private ProductOptionService productOptionService;

    @Autowired
    private ProductItemService productItemService;

    @Autowired
    private ProductOptionDetailService productOptionDetailService;

    /**
     * *
     * This method handles get all Product, List Product with paginate
     *
     * @param currentPage This is current page.
     * @return List Product on page and paginate information.
     */
    @GetMapping(path = "/list-product")
    @ResponseBody
    public ResponseEntity<ResponseDataDto> findAllProduct(
            @RequestParam(value = "perPage", defaultValue = "10") int perPage,
            @RequestParam(value = "currentPage", defaultValue = "1") int currentPage) {
        ProductListDto pageProductList = this.productService.findPageProduct(
                perPage, currentPage - 1);

        ResponseDataDto responseDataDto = new ResponseDataDto();
        responseDataDto.setStatus(HttpStatus.OK.series().name());
        responseDataDto.setCode(HttpStatus.OK.value());
        responseDataDto.setMessage("Find List Product Successful");
        responseDataDto.setData(pageProductList);

        if (pageProductList == null) {
            responseDataDto.setMessage("Do Not Have Any Products In This Page");
        }

        return ResponseEntity.status(HttpStatus.OK).body(responseDataDto);
    }

    /**
     * *
     * This method handles Get all Product Information and Product Visuals by Product Id
     *
     * @param productId This is an ID of exist product.
     * @return All Product Information and List Product Visuals by Product Id
     */
    @GetMapping(path = "/product-information/{productId}")
    public ResponseEntity<ResponseDataDto> findProductByProductId(@PathVariable("productId") Long productId) {
        ProductDto product = productService.findProductByProductId(productId);
        ResponseDataDto responseDataDto = new ResponseDataDto();
        responseDataDto.setStatus(HttpStatus.OK.series().name());
        responseDataDto.setCode(HttpStatus.OK.value());
        responseDataDto.setMessage(product == null ? "Product has not been added" :"Get Product Information Successful!");
        responseDataDto.setData(product);
        return ResponseEntity.status(HttpStatus.OK).body(responseDataDto);
    }

    /**
     * *
     * This method handles Get all Product Options Information by Product Id
     *
     * @param productId This is an ID of exist product.
     * @return All Product Options Information and List Product Option Detail, List Image and Video
     * in each Option by Product ID
     */
    @GetMapping(path = "/product-option/{productId}")
    public ResponseEntity<ResponseDataDto> findAllProductOptionByProductId(@PathVariable("productId") Long productId) {
        List<ProductOptionDto> options = productOptionService.findProductOptionByProductId(productId);
        ResponseDataDto responseDataDto = new ResponseDataDto();
        responseDataDto.setStatus(HttpStatus.OK.series().name());
        responseDataDto.setCode(HttpStatus.OK.value());
        responseDataDto.setMessage(options == null ? "Not exist any Product Options" :"Get Product Option Successful!");
        responseDataDto.setData(options);
        return ResponseEntity.status(HttpStatus.OK).body(responseDataDto);
    }

    /**
     * *
     * This method handles Get all Product Items Information by Product Id
     *
     * @param productId This is an ID of exist product.
     * @return All Product Items Information and List Product Item Detail by Product Id
     */
    @GetMapping(path = "/product-item/{productId}")
    public ResponseEntity<ResponseDataDto> findAllProductItemByProductId(@PathVariable("productId") Long productId) {
        List<ProductItemDto> items = productItemService.findProductItemsByProductId(productId);
        ResponseDataDto responseDataDto = new ResponseDataDto();
        responseDataDto.setStatus(HttpStatus.OK.series().name());
        responseDataDto.setCode(HttpStatus.OK.value());
        responseDataDto.setMessage(items == null ? "Not exist any Product Items" :"Get Product Item Successful!");
        responseDataDto.setData(items);
        return ResponseEntity.status(HttpStatus.OK).body(responseDataDto);
    }

    @GetMapping("/product-items/{id}")
    public ResponseEntity<ResponseDataDto> getProduct(@PathVariable("id") Long id){
        ProductItemsResDto items = productService.getProduct(id);
        ResponseDataDto responseDataDto = new ResponseDataDto();
        responseDataDto.setStatus(HttpStatus.OK.series().name());
        responseDataDto.setCode(HttpStatus.OK.value());
        responseDataDto.setMessage(items == null ? "Not exist any Product Items" :"Get Product Items Successful!");
        responseDataDto.setData(items);
        return ResponseEntity.status(HttpStatus.OK).body(responseDataDto);
    }

    /**
     * *
     * This method handles Get Exactly Product Option Detail by product Id and by productOptionDetail Id
     *
     * @param productId             This is an ID of exist product.
     * @param productOptionDetailId This is an ID of product option detail
     * @return Information Product Option Detail and its Visuals
     */
    @GetMapping(path = "/product-option-detail/{productId}/{productOptionDetailId}")
    public ResponseEntity<ResponseDataDto> findProductOptionByIdAndByProductId(@PathVariable Long productId, @PathVariable Long productOptionDetailId) {
        ProductOptionDetailDto findProductOptionDetail = productOptionDetailService.findProductOptionDetailByIdAndByProductId(productId, productOptionDetailId);
        ResponseDataDto responseDataDto = new ResponseDataDto();
        responseDataDto.setStatus(HttpStatus.OK.series().name());
        responseDataDto.setCode(HttpStatus.OK.value());
        responseDataDto.setMessage(findProductOptionDetail == null ? "Not exist Product Option Detail" :"Get Product Option Detail Successful!");
        responseDataDto.setData(findProductOptionDetail);
        return ResponseEntity.status(HttpStatus.OK).body(responseDataDto);
    }

    /**
     * *
     * This method handles Get Product Detail field on Page Product by product ID
     *
     * @param productId This is an ID of exist product.
     * @return All Product Detail information
     */
    @GetMapping("/detail/{productId}")
    public ResponseEntity<ResponseDataDto> getProductDetailByProductId(@PathVariable("productId") Long productId) {
        List<ProductDetailDto> details = productDetailService.getProductDetailByIdProduct(productId);
        ResponseDataDto responseDataDto = new ResponseDataDto();
        responseDataDto.setStatus(HttpStatus.OK.series().name());
        responseDataDto.setCode(HttpStatus.OK.value());
        responseDataDto.setMessage(details == null ? "Not exist Product Detail" :"Get Product Detail Successful!");
        responseDataDto.setData(details);
        return ResponseEntity.status(HttpStatus.OK).body(responseDataDto);
    }

    /**
     * *
     * This method handles Get Product Description field on Page Product by product Id
     *
     * @param productId This is an ID of exist product.
     * @return All Product Description information
     */
    @GetMapping(path = "/description/{productId}")
    public ResponseEntity<ResponseDataDto> findProductDescriptionByProductId(@PathVariable("productId") Long productId) {
        List<ProductDescriptionDto> descriptions = productDescriptionService.findProductDescriptionByProductId(productId);
        ResponseDataDto responseDataDto = new ResponseDataDto();
        responseDataDto.setStatus(HttpStatus.OK.series().name());
        responseDataDto.setCode(HttpStatus.OK.value());
        responseDataDto.setMessage(descriptions == null ? "Not exist Product Description" :"Get Product Description Successful!");
        responseDataDto.setData(descriptions);
        return ResponseEntity.status(HttpStatus.OK).body(responseDataDto);
    }

    //linh: api call by other service
    @GetMapping(path = "/productItem/{productItemId}")
    public ResponseEntity<ResponseDataDto> findOneProductItemByProductItemId(@PathVariable("productItemId") Long productItemId) {
        ProductItemDto productItemDto = this.productItemService.findOneByProductItemId(productItemId);
        ResponseDataDto responseDataDto = new ResponseDataDto();
        responseDataDto.setStatus(HttpStatus.OK.series().name());
        responseDataDto.setCode(HttpStatus.OK.value());
        responseDataDto.setMessage("Get Product Item Successful!");
        responseDataDto.setData(productItemDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDataDto);
    }

    @PostMapping(path = "/cartItem")
    public ResponseEntity<ResponseDataDto> getMapCartAndProductItem(@RequestBody List<CartItemDto> listCartItem) {
        Map<Long, ProductItemDto> mapCartAndProductItem = this.productItemService.getMapCartAndProductItem(listCartItem);
        ResponseDataDto responseDataDto = new ResponseDataDto();
        responseDataDto.setStatus(HttpStatus.OK.series().name());
        responseDataDto.setCode(HttpStatus.OK.value());
        responseDataDto.setMessage("Successful!");
        responseDataDto.setData(mapCartAndProductItem);
        return ResponseEntity.status(HttpStatus.OK).body(responseDataDto);
    }

    @PostMapping (path = "/productItem")
    public ResponseEntity<ResponseDto> updateQuantityOfProductItem(
            @RequestBody() Map<Long, Integer> mapProductItemIdAndQuantityCancel) {
        this.productItemService.updateListQuantityOfProductItem(mapProductItemIdAndQuantityCancel);
        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatus(HttpStatus.OK.series().name());
        responseDto.setCode(HttpStatus.OK.value());
        responseDto.setMessage("Successful!");
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }



}
