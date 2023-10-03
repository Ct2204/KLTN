
package kltn.orderservice.order.service;


import kltn.orderservice.common.entity.OrderEntity;
import kltn.orderservice.common.vo.OrderStatusE;
import kltn.orderservice.order.dto.CreateOrderRequestDto;
import kltn.orderservice.order.dto.OrderDto;

import java.util.List;

public interface OrderService {

    OrderDto getOrderById(Long id);

    List<OrderDto> getAllOrderByUserId(Long id);

    List<OrderDto> getOrderByStatus(OrderStatusE status);

    void updateStatusOrder(Long id, OrderStatusE orderStatus);

    void saveOrder(CreateOrderRequestDto createOrderRequestDto);

    public OrderDto mapperOrderEntityToDto(OrderEntity order);
}
