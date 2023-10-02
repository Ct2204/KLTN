
package kltn.productservice.product.controller;

import kltn.productservice.product.dto.ProductSearchPageDto;
import kltn.productservice.product.dto.ResponseDataDto;
import kltn.productservice.product.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/product")

public class ProductSearchController {

    @Autowired
    private PageService pageService;

    /**
     * Api get result search product by text and response a paginated list product
     *   @param keyword-> this is the keyword which you have to enter to search the products you want to find
     *   @param pageNumber-> this is the current page
     *   @param size -> number of element in one page
     *   @return: returns a PageResponse containing the necessary information that the client needs including a list of products, total products...
     */
    @GetMapping("/search-product-by-text")
    public ResponseEntity<ResponseDataDto> GetPageResultSearchProductV2 (
            @RequestParam("keyword")String keyword,
            @RequestParam(value = "pageNumber",defaultValue = "1")int pageNumber,
            @RequestHeader(value = "size",defaultValue = "3") int size){

        ProductSearchPageDto pageResDTO =this.pageService.pageableListResultSearch(keyword, pageNumber, size);

        if(pageResDTO.getListProduct().size()==0){
            ResponseDataDto responseDto = new ResponseDataDto();
            responseDto.setCode(200);
            responseDto.setStatus("ok");
            responseDto.setMessage("No matching products found");

            return ResponseEntity.status(HttpStatus.OK).body(responseDto);
        }

        ResponseDataDto responseDto = new ResponseDataDto();
        responseDto.setCode(200);
        responseDto.setStatus("ok");
        responseDto.setMessage("Find list product by text successfully");
        responseDto.setData(pageResDTO);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
