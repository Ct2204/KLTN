//package kltn.cartservice.common.rabbit;
//
//import kltn.cartservice.carts.service.CartItemService;
//import kltn.cartservice.common.dto.CreateOrderRequestDto;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.event.EventListener;
//import org.springframework.stereotype.Component;
//
//@Component
//public class MessageReceivedEventListener {
//
//    @Autowired
//    private CartItemService cartItemService;
//
//    @EventListener
//    public void handleEvent(MessageReceivedEvent event) {
//        CreateOrderRequestDto orderRequestDto = event.getMessage();
//        System.out.println("Received message: " + orderRequestDto);
//
//        this.cartItemService.deleteCartItemList(orderRequestDto);
//    }
//}
