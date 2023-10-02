package kltn.productservice.product.controller;


import kltn.productservice.product.dto.ResponseDataDto;
import kltn.productservice.product.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/product")

public class CategorySearchController {

    @Autowired
    private PageService pageService;

    /**
     * Api get result search product by category and response a paginated list product
     *   @param parent_id       -> this is the keyword which you have to enter to search the products you want to find
     *   @param pageNumber      -> this is the current page
     *   @param size            -> number of element in one page
     *   @return: a PageResponse containing the necessary information that the client needs including a list of products, total products...
     */
    @GetMapping("/category-search")
    public ResponseEntity<ResponseDataDto> categorySearchResult(
            @RequestParam("parent_id")short parent_id,
            @RequestParam(value = "pageNumber",defaultValue = "1") int pageNumber,
            @RequestHeader(value = "size",defaultValue = "3") int size
    ){

        ResponseDataDto responseDto = new ResponseDataDto();
        responseDto.setCode(200);
        responseDto.setStatus("ok");
        responseDto.setMessage("Get list product find by text successfully");
        responseDto.setData(this.pageService.pageListProductWithCategory(parent_id,pageNumber,size));
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
