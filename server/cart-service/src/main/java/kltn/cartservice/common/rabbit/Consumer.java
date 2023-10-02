//package kltn.cartservice.common.rabbit;
//
//import kltn.cartservice.common.dto.CreateOrderRequestDto;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationEventPublisher;
//import org.springframework.stereotype.Component;
//
//@Component
//@Slf4j
//public class Consumer {
//
//    @Autowired
//    private ApplicationEventPublisher applicationEventPublisher;
//
//    @RabbitListener(queues = "queue.A")
//    private void receive(CreateOrderRequestDto orderRequest) {
//        log.info("Message receive from queueA: " + orderRequest);
//        applicationEventPublisher.publishEvent(new MessageReceivedEvent(this, orderRequest));
//    }
//}
