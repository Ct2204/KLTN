
package kltn.productservice.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ProductSearchPageDto extends PageableDto{
    private List<?> listProduct;

    public ProductSearchPageDto(int totalPages, long totalElements, int perPage, int numberOfElements, int currentPage, boolean first, boolean last, List<?> listProduct) {
        super(totalPages, totalElements, perPage, numberOfElements, currentPage, first, last);
        this.listProduct = listProduct;
    }
}
