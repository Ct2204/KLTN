
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
public class ProductDetailsDto {

    private Long productId;
    private Long sellerId;
    private List<ProductDetailDto> productDetails;
}
