
package kltn.productservice.product.service;


import kltn.productservice.product.dto.ProductSearchByCategoryPageResDto;
import kltn.productservice.product.dto.ProductSearchPageDto;

public interface PageService {

    ProductSearchPageDto pageableListResultSearch(String keyword, int pageNumber, int size);

    ProductSearchByCategoryPageResDto pageListProductWithCategory(short parent_id, int pageNumber, int size);
}
