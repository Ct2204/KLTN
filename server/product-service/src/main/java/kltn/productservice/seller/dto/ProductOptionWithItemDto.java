
package kltn.productservice.seller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductOptionWithItemDto {

    private Long productId;
    private Long sellerId;
    private List<ProductOptionDto> options = new ArrayList<>();
    private List<ProductItemDto> productItems;

}
