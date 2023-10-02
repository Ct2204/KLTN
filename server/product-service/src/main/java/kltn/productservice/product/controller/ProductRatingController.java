
package kltn.productservice.product.controller;

import kltn.productservice.common.dto.ResponseDto;
import kltn.productservice.product.dto.PageRatingResDto;
import kltn.productservice.product.dto.ProductRatingReqDto;
import kltn.productservice.product.dto.ResponseDataDto;
import kltn.productservice.product.service.ProductRatingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class ProductRatingController {

    @Autowired
    private ProductRatingService productRatingService;

    /**
     * *
     * This method handles get all ProductRating with paginate by product id
     *
     * @param productId   This is an ID of exist product.
     * @param perPage     this is the number of elements per page .
     * @param currentPage This is current page.
     * @return list rating of page and paginate information.
     */
    @GetMapping(path="/product/rating/{productId}")
    @ResponseBody
    public ResponseDataDto findAllProductRatingByProductId(
            @PathVariable Long productId,
            @RequestParam(value = "perPage", defaultValue = "10") int perPage,
            @RequestParam(value = "currentPage", defaultValue = "1") int currentPage){

        PageRatingResDto pageProductRatingRes = this.productRatingService.findAllProductRatingByProductId(
                productId, perPage, currentPage-1);

        ResponseDataDto responseDataDto = new ResponseDataDto();
        responseDataDto.setStatus(HttpStatus.OK.series().name());
        responseDataDto.setCode(HttpStatus.OK.value());
        responseDataDto.setData(pageProductRatingRes);
        responseDataDto.setMessage(pageProductRatingRes == null ? "Not exist any comments" :"Succeed");

        return responseDataDto;
    }

    /**
     *
     * This method handles insert new rating for a product
     * @param userId This is id of current user.
     * @param productRating This is all information to insert new rating.
     * @return "Created successes!"
     */
    @PostMapping("/rating")
    @ResponseBody
    public ResponseEntity<?> insertProductRating(@RequestHeader Long userId,
                                                 @ModelAttribute @Valid ProductRatingReqDto productRating){
        this.productRatingService.saveProductRating(userId, productRating);

        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatus(HttpStatus.CREATED.series().name());
        responseDto.setCode(HttpStatus.CREATED.value());
        responseDto.setMessage  ("Created succeed!");

        return ResponseEntity.status(201).body(responseDto);
    }




}
