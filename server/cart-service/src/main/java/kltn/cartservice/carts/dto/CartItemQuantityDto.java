package kltn.cartservice.carts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemQuantityDto {
    private Long cartItemId;
    private Short quantity;
}
