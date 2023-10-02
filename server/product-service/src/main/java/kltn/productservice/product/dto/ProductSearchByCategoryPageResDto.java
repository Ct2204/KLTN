
package kltn.productservice.product.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductSearchByCategoryPageResDto extends ProductSearchPageDto {
    private List<ProductCategoryDto> listChildCate;

    public ProductSearchByCategoryPageResDto(int totalPages, long totalElements, int perPage, int numberOfElements, int currentPage, boolean first, boolean last, List<ProductCategoryDto> listChildCate, List<?> listProduct) {
        super(totalPages, totalElements, perPage, numberOfElements, currentPage, first, last, listProduct);
        this.listChildCate = listChildCate;
    }
}
