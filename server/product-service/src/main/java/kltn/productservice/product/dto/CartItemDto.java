package kltn.productservice.product.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemDto {

    private Long cartItemId;

    private Long productItemId;
}
