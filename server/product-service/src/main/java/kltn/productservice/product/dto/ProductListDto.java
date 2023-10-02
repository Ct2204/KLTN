
package kltn.productservice.product.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class ProductListDto extends PageableDto{

    private List<ProductDto> listProducts;

    public ProductListDto() {
    }

    public ProductListDto(int totalPages, long totalElements,
                          int perPage, int numberOfElements,
                          int currentPage, boolean first, boolean last,
                          List<ProductDto> listProducts) {
        super(totalPages, totalElements, perPage, numberOfElements, currentPage, first, last);
        this.listProducts = listProducts ;
    }
}
