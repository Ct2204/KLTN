
package kltn.cartservice.carts.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CartItemReqDto {

    private Long userId;

    @NotNull(message = "quantity mustn't be null!")
    @Min(value = 1, message = "quantity must be > than 1!")
    private Short quantity;

    private Long productItemId;
}
