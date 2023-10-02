
package kltn.cartservice.carts.controller;


import kltn.cartservice.carts.dto.CartItemQuantityRequestDto;
import kltn.cartservice.carts.dto.CartItemReqDto;
import kltn.cartservice.carts.service.impl.CartItemServiceImpl;
import kltn.cartservice.common.dto.ResponseDataDto;
import kltn.cartservice.common.dto.ResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/cart-items")
public class    CartItemController {

    @Autowired
    private CartItemServiceImpl cartItemServiceImpl;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDto> createNewCartItem(@Validated @RequestBody CartItemReqDto cartItemReqDto) {
        cartItemServiceImpl.createNewCartItem(cartItemReqDto);
        ResponseDto response = new ResponseDto();
        response.setCode(HttpStatus.OK.value());
        response.setStatus(HttpStatus.OK.name());
        response.setMessage("Create new cart item successfully!");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value = "/{userId}")
    public ResponseEntity<ResponseDataDto> getAllItemsByUserId(@PathVariable("userId") Long userId) {
        ResponseDataDto responseData = new ResponseDataDto();
        responseData.setStatus(HttpStatus.OK.name());
        responseData.setCode(HttpStatus.OK.value());
        responseData.setMessage("Get all user's cart item successfully!");
        responseData.setData(cartItemServiceImpl.getAllCartItemsByUserId(userId));
        return ResponseEntity.status(HttpStatus.OK).body(responseData);
    }

    @PutMapping(value = "/update-all/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDto> updateCartItemList(
            @PathVariable("userId") Long userId,
            @Validated @RequestBody CartItemQuantityRequestDto cartItemQuantityRequestDtoList) {
        cartItemServiceImpl.updateCartItems(userId,     cartItemQuantityRequestDtoList);
        ResponseDto response = new ResponseDto();
        response.setStatus(HttpStatus.OK.name());
        response.setCode(HttpStatus.OK.value());
        response.setMessage("Update all user's cart item successfully!");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(value = "/{userId}/{cartItemId}")
    public ResponseEntity<ResponseDto> deleteCartItem(@PathVariable("userId") Long userId,
                               @PathVariable("cartItemId") Long cartItemId) {
        cartItemServiceImpl.deleteCartItem(userId, cartItemId);
        ResponseDto response = new ResponseDto();
        response.setStatus(HttpStatus.OK.name());
        response.setCode(HttpStatus.OK.value());
        response.setMessage("Delete cart item successfully!");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
