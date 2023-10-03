package kltn.orderservice.order.controller;



import kltn.orderservice.common.dto.ResponseDataDto;
import kltn.orderservice.common.dto.ResponseDto;
import kltn.orderservice.common.vo.OrderStatusE;
import kltn.orderservice.order.dto.CreateOrderRequestDto;
import kltn.orderservice.order.dto.OrderDetailDto;
import kltn.orderservice.order.dto.OrderDto;
import kltn.orderservice.order.service.OrderDetailService;
import kltn.orderservice.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin({"*"})
@RequestMapping({"/api/v1/order"})


public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailService orderDetailService;

    /**
     * *
     * This method handles get all order of user.
     *
     * @param id this is ID of the user
     * @return A list of order .
     */


    @GetMapping({"/by-user/{id}"})
    public ResponseEntity<ResponseDataDto> getOrderByUserId(@PathVariable("id") Long id) {
        List<OrderDto> items = this.orderService.getAllOrderByUserId(id);
        ResponseDataDto responseDataDto = new ResponseDataDto();
        responseDataDto.setStatus(HttpStatus.OK.series().name());
        responseDataDto.setCode(HttpStatus.OK.value());
        responseDataDto.setMessage(items == null ? "Not exist any Order of user " + id : "Get order Successfully!");
        responseDataDto.setData(items);
        return ResponseEntity.status(HttpStatus.OK).body(responseDataDto);
    }

    /**
     * *
     * This method handles get all order with status.
     *
     * @param status this is status of the order
     * @return A list of order .
     */


    @GetMapping({"/by-status/{status}"})
    public ResponseEntity<ResponseDataDto> getOrderByStatus(@PathVariable("status") OrderStatusE status) {
        List<OrderDto> items = this.orderService.getOrderByStatus(status);
        ResponseDataDto responseDataDto = new ResponseDataDto();
        responseDataDto.setStatus(HttpStatus.OK.series().name());
        responseDataDto.setCode(HttpStatus.OK.value());
        responseDataDto.setMessage(items == null ? "Not exist any Order with " + status : "Get order Successfully!");
        responseDataDto.setData(items);
        return ResponseEntity.status(HttpStatus.OK).body(responseDataDto);
    }

    /**
     * *
     * This method handles get order by OrderID.
     *
     * @param id this is ID of the order
     * @return An order
     */

    @GetMapping({"/by-order/{id}"})
    public ResponseEntity<ResponseDataDto> getOrderById(@PathVariable("id") Long id) {
        OrderDto items = this.orderService.getOrderById(id);
        ResponseDataDto responseDataDto = new ResponseDataDto();
        responseDataDto.setStatus(HttpStatus.OK.series().name());
        responseDataDto.setCode(HttpStatus.OK.value());
        responseDataDto.setMessage(items == null ? "Not exist any Order with " + id : "Get order Successfully!");
        responseDataDto.setData(items);
        return ResponseEntity.status(HttpStatus.OK).body(responseDataDto);
    }

    /**
     * *
     * This method handles update Status of order.
     *
     * @param id          this is ID of the order
     * @param orderStatus this is a status of order
     * @return A message
     */
    @PatchMapping({"/update-order/{id}"})
    public ResponseEntity<ResponseDto> updateStatusOrder(@PathVariable Long id, @RequestBody OrderStatusE orderStatus) {
        this.orderService.updateStatusOrder(id, orderStatus);
        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatus(HttpStatus.OK.series().name());
        responseDto.setCode(201);
        responseDto.setMessage("Change Status order successfully!");
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    /**
     * *
     * This method handles create order.
     *
     * @param createOrderRequestDto this is all information need to create order
     * @return A message
     */


    @PostMapping({"/create-order"})
    public ResponseEntity<ResponseDto> createOrder(@RequestBody CreateOrderRequestDto createOrderRequestDto) {

        this.orderService.saveOrder(createOrderRequestDto);
        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatus(HttpStatus.OK.series().name());
        responseDto.setCode(201);
        responseDto.setMessage(" order successfully!");
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    /**
     * *
     * This method handles create order.
     *
     * @param id this is ID of the orderDetail
     * @return A List Order Detail
     */
    @GetMapping({"/get-order-detail-by-order/{id}"})
    ResponseEntity<ResponseDataDto> getOrderDetailByOrderId(@PathVariable("id") Long id) {

        List<OrderDetailDto> orderDetail = this.orderDetailService.getOrderDetailByOrderId(id);
        ResponseDataDto responseDataDto = new ResponseDataDto();
        responseDataDto.setStatus(HttpStatus.OK.series().name());
        responseDataDto.setCode(HttpStatus.OK.value());
        responseDataDto.setMessage("Get order detail Successfully!");
        responseDataDto.setData(orderDetail);
        return ResponseEntity.status(HttpStatus.OK).body(responseDataDto);
    }
}
