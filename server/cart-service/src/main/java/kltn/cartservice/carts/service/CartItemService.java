
package kltn.cartservice.carts.service;

import kltn.cartservice.carts.dto.CartItemQuantityRequestDto;
import kltn.cartservice.carts.dto.CartItemReqDto;
import kltn.cartservice.carts.dto.CartItemsPerUserResDto;
import kltn.cartservice.common.dto.CreateOrderRequestDto;

import java.util.List;

public interface CartItemService {

    void createNewCartItem(CartItemReqDto cartItemReqDto);

    List<CartItemsPerUserResDto> getAllCartItemsByUserId(Long id);

    void updateCartItems(Long userId, CartItemQuantityRequestDto cartItemQuantityRequestDtoList);

    void deleteCartItem(Long userId, Long cartItemId);

    void deleteCartItemList(CreateOrderRequestDto orderRequestDto);
}
