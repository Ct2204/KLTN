
package kltn.cartservice.carts.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This DTO is used to method find all cart's items of a user by user's id.
 */

@Data
@NoArgsConstructor
public class CartItemsPerUserResDto {

    @NotNull(message = "id mustn't be null!")
    @Min(value = 1, message = "The ID must be greater than or equal to 1!")
    private Long cartItemId;

    private Long productItemId;

    private String url;

    private String title;

    private String price;

    private Short quantity;

    private Short stockQuantity;

    public CartItemsPerUserResDto(@NotNull Long cartItemId, Long productItemId, String url, String title, String price, Short quantity, Short stockQuantity) {
        this.cartItemId = cartItemId;
        this.productItemId = productItemId;
        this.url = url;
        this.title = title;
        this.price = price;
        this.quantity = quantity;
        this.stockQuantity = stockQuantity;
    }


}
