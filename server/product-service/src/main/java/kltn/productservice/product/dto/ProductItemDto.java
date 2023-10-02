
package kltn.productservice.product.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class ProductItemDto {

    private Long id;

    private Long productId;

    private BigDecimal price;

    private Integer quantity;

}
