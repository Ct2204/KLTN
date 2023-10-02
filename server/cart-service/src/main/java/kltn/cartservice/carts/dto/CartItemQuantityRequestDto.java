package kltn.cartservice.carts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemQuantityRequestDto {
    private List<CartItemQuantityDto> cartItemList;
}
