
package kltn.productservice.seller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductsDto {
    List<ProductDto> listDraftProduct;
    List<ProductDto> listSaleProduct;
}
