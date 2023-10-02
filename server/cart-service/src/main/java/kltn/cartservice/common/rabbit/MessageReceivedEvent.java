//package kltn.cartservice.common.rabbit;
//
//import kltn.cartservice.common.dto.CreateOrderRequestDto;
//import org.springframework.context.ApplicationEvent;
//
//public class MessageReceivedEvent extends ApplicationEvent {
//
//    private final CreateOrderRequestDto message;
//
//    public MessageReceivedEvent(Object source, CreateOrderRequestDto orderRequestDto) {
//        super(source);
//        this.message = orderRequestDto;
//    }
//
//    public CreateOrderRequestDto getMessage() {
//        return message;
//    }
//}
