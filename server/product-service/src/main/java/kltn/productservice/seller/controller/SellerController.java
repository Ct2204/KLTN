
package kltn.productservice.seller.controller;

import kltn.productservice.product.dto.ResponseDataDto;
import kltn.productservice.seller.dto.ProductDescriptionDto;
import kltn.productservice.seller.dto.ProductDetailsDto;
import kltn.productservice.seller.dto.ProductDto;
import kltn.productservice.seller.dto.ProductOptionWithItemDto;
import kltn.productservice.common.dto.ResponseDto;
import kltn.productservice.seller.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/seller")
public class SellerController {

    @Autowired
    SellerService sellerService;

    /**
     * *
     * This method handles get information of a product.
     *
     * @param id This is id of the product.
     * @return The information of the product.
     */
    @GetMapping("/product/{id}")
    public ResponseEntity<ResponseDataDto> getProduct(@PathVariable("id") Long id){
        ProductDto items = this.sellerService.getProduct(id);
        ResponseDataDto responseDataDto = new ResponseDataDto();
        responseDataDto.setStatus(HttpStatus.OK.series().name());
        responseDataDto.setCode(HttpStatus.OK.value());
        responseDataDto.setMessage(items == null ? "Not exist any Products" :"Get Product Successfully!");
        responseDataDto.setData(items);
        return ResponseEntity.status(HttpStatus.OK).body(responseDataDto);
    }

    /**
     * *
     * This method handles save a new product.
     *
     * @param productDto This is information of the product.
     * @return The success message.
     */
    @PostMapping("/product/add")
    public ResponseEntity<ResponseDataDto> saveProduct(@RequestBody ProductDto productDto){

        Long id = this.sellerService.saveProduct(productDto);
        ResponseDataDto responseDataDto = new ResponseDataDto();
        responseDataDto.setStatus(HttpStatus.OK.series().name());
        responseDataDto.setCode(201);
        responseDataDto.setMessage("Save Product Successfully!");
        Map<String, Long> dataHashmap = new HashMap<>();
        dataHashmap.put("productId", id);
        responseDataDto.setData(dataHashmap);
        return ResponseEntity.status(HttpStatus.OK).body(responseDataDto);
    }

    /**
     * *
     * This method handles upload a file of the product.
     *
     * @param file This is the picture or video from the product.
     *
     * @return The success message.
     */
    @PostMapping("/product/upload-file")
    public ResponseEntity<ResponseDataDto> uploadProfilePicture(@RequestBody MultipartFile file) {
        String path = this.sellerService.uploadProductFile(file);
        ResponseDataDto responseDataDto = new ResponseDataDto();
        responseDataDto.setStatus(HttpStatus.OK.series().name());
        responseDataDto.setCode(201);
        responseDataDto.setMessage("Upload file successfully!");
        Map<String, String> dataHashmap = new HashMap<>();
        dataHashmap.put("path", path);
        responseDataDto.setData(dataHashmap);
        return ResponseEntity.status(HttpStatus.OK).body(responseDataDto);
    }

    /**
     * *
     * This method handles update a product.
     *
     * @param productDto This is information of the product.
     * @return The success message.
     */
    @PutMapping("/product/update")
    public ResponseEntity<ResponseDto> updateProduct(@RequestBody ProductDto productDto){

        this.sellerService.updateProduct(productDto);
        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatus(HttpStatus.OK.series().name());
        responseDto.setCode(201);
        responseDto.setMessage("Update product successfully!");
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    /**
     * *
     * This method handles get a list options of a product.
     *
     * @param id This is id of the product.
     * @return The list options of the product.
     */
    @GetMapping("/product/option/{id}")
    public ResponseEntity<ResponseDataDto> getProductOptionWithItem(@PathVariable("id") Long id){
        ProductOptionWithItemDto item = sellerService.getProductOptionWithItem(id);

        ResponseDataDto responseDataDto = new ResponseDataDto();
        responseDataDto.setStatus(HttpStatus.OK.series().name());
        responseDataDto.setCode(HttpStatus.OK.value());
        responseDataDto.setMessage(item == null ? "Not exist any Products" :"Get Product Successfully!");
        responseDataDto.setData(item);
        return ResponseEntity.status(HttpStatus.OK).body(responseDataDto);
    }


    /**
     * *
     * This method handles save a list options and item of a product.
     *
     * @param productOptionWithItemDto This is a DTO contains list options and item.
     * @return The success message.
     */
    @PostMapping("/product/option/add")
    public ResponseEntity<ResponseDto>  saveProductOption(@RequestBody ProductOptionWithItemDto productOptionWithItemDto){
        this.sellerService.saveProductOptionWithItem(productOptionWithItemDto);
        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatus(HttpStatus.OK.series().name());
        responseDto.setCode(201);
        responseDto.setMessage("Save options successfully!");
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    /**
     * *
     * This method handles get description of a product.
     *
     * @param id This is id of the product.
     * @return The description of the product.
     */
    @GetMapping("/product/description/{id}")
    public ResponseEntity<ResponseDataDto> getProductDescription(@PathVariable("id") Long id){
        ProductDescriptionDto item = this.sellerService.getProductDescription(id);
        ResponseDataDto responseDataDto = new ResponseDataDto();
        responseDataDto.setStatus(HttpStatus.OK.series().name());
        responseDataDto.setCode(HttpStatus.OK.value());
        responseDataDto.setMessage(item == null ? "Not exist description" :"Get Product Description successfully!");
        responseDataDto.setData(item);
        return ResponseEntity.status(HttpStatus.OK).body(responseDataDto);
    }


    /**
     * *
     * This method handles save description of a product.
     *
     * @param productDescriptionDto This is information of the description.
     * @return The success message.
     */
    @PostMapping("/product/description/add")
    public ResponseEntity<ResponseDto> saveProductDescription(@RequestBody ProductDescriptionDto productDescriptionDto){
        this.sellerService.saveProductDescription(productDescriptionDto);
        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatus(HttpStatus.OK.series().name());
        responseDto.setCode(201);
        responseDto.setMessage("Save description successfully!");
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    /**
     * *
     * This method handles get details of a product.
     *
     * @param id This is id of the product.
     * @return The details of the product.
     */
    @GetMapping("/product/detail/{id}")
    public ResponseEntity<ResponseDataDto> getProductDetail(@PathVariable("id") Long id){
        ProductDetailsDto item = this.sellerService.getProductDetails(id);
        ResponseDataDto responseDataDto = new ResponseDataDto();
        responseDataDto.setStatus(HttpStatus.OK.series().name());
        responseDataDto.setCode(HttpStatus.OK.value());
        responseDataDto.setMessage(item == null ? "Not exist any details" :"Get Product Details successfully!");
        responseDataDto.setData(item);
        return ResponseEntity.status(HttpStatus.OK).body(responseDataDto);
    }



    /**
     * *
     * This method handles save details of a product.
     *
     * @param productDetailsDto This is information of the details.
     * @return The success message.
     */
    @PostMapping("/product/detail/add")
    public ResponseEntity<ResponseDto> saveProductDetail(@RequestBody ProductDetailsDto productDetailsDto){
        this.sellerService.saveProductDetail(productDetailsDto);
        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatus(HttpStatus.OK.series().name());
        responseDto.setCode(201);
        responseDto.setMessage("Save details successfully!");
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
