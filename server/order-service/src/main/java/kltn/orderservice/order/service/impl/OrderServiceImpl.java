
package kltn.orderservice.order.service.impl;


import kltn.orderservice.common.entity.*;
import kltn.orderservice.common.exception.DuplicateResourceException;
import kltn.orderservice.common.exception.ResourceNotFoundException;
import kltn.orderservice.common.repository.CartItemRepository;
import kltn.orderservice.common.repository.ProductItemRepository;
import kltn.orderservice.common.repository.ProductRepository;
import kltn.orderservice.common.repository.UserRepository;
import kltn.orderservice.common.vo.CartItemStatusEnum;
import kltn.orderservice.common.vo.OrderStatusE;
import kltn.orderservice.common.vo.ProductStatusType;
import kltn.orderservice.order.dto.CreateOrderRequestDto;
import kltn.orderservice.order.dto.OrderDto;
import kltn.orderservice.order.repository.OrderDetailRepository;
import kltn.orderservice.order.repository.OrderRepository;
import kltn.orderservice.order.service.OrderService;
import jakarta.transaction.Transactional;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductItemRepository productItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private DirectExchange exchange;


    /**
     * Get an order  by id.
     *
     * @param id This is the ID of the order.
     * @return A order.
     */

    public OrderDto getOrderById(Long id) {
        OrderEntity order = this.orderRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("order with id " + id + " not found!"));
        return this.mapperOrderEntityToDto(order);
    }

    /**
     * Get all order by userID .
     *
     * @param id This is the ID of  user.
     * @return The list order.
     */

    public List<OrderDto> getAllOrderByUserId(Long id) {
        if (this.userRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("user with id " + id + " not found!");
        }
        List<OrderEntity> list = this.orderRepository.getAllOrderByUserId(id);
        return list.stream().map(this::mapperOrderEntityToDto).collect(Collectors.toList());
    }

    /**
     * Get all order by userID .
     *
     * @param status This is the status of  order.
     * @return The list order.
     */

    public List<OrderDto> getOrderByStatus(OrderStatusE status) {
        List<OrderEntity> list = this.orderRepository.getAllOrderByStatus(status);
        return list.stream().map(this::mapperOrderEntityToDto).collect(Collectors.toList());
    }

    /**
     * Update Status Order .
     *
     * @param id          This is the ID of  order.
     * @param orderStatus this a status of order
     */

    public void updateStatusOrder(Long id, OrderStatusE orderStatus) {

        OrderEntity order = (OrderEntity) this.orderRepository.findById(id).orElseThrow(() -> {
            return new ResourceNotFoundException("order with id " + id + "not found");
        });
        if (order.getStatus() == OrderStatusE.PENDING) {
            order.setStatus(OrderStatusE.CANCELED);
            List<OrderDetailEntity> listOrderDetail = order.getOrderDetails();
            for (OrderDetailEntity orderDetail : listOrderDetail) {
                int backQuantity = orderDetail.getProductItem().getQuantity() + orderDetail.getQuantity();
                ProductItemEntity productItem = orderDetail.getProductItem();
                productItem.setQuantity(backQuantity);
                this.productItemRepository.save(productItem);
            }
        } else {
            order.setStatus(orderStatus);
        }

        this.orderRepository.save(order);
    }

    /**
     * Get all order by userID .
     *
     * @param orderInput This is all information need to create order
     * @throws DuplicateResourceException if the cartitem and user not map.
     */

    @Transactional
    public void saveOrder(CreateOrderRequestDto orderInput) {
        List<CartItemEntity> listCartItem = this.cartItemRepository.GetCartItemSelected(orderInput.getUserId(), orderInput.getCartItemId());
        System.out.println(listCartItem);
        if (listCartItem.isEmpty()) {
            throw new DuplicateResourceException("CartItem And User not map");
        } else {
            OrderEntity order = new OrderEntity();
            order.setStatus(OrderStatusE.PENDING);
            order.setUser((listCartItem.get(0).getUser()));
            order.setCreatedAt(Instant.now());
            order = this.orderRepository.save(order);
            for (CartItemEntity item : listCartItem) {
                if (item.getQuantity() <= item.getProductItem().getQuantity() && item.getStatus() == CartItemStatusEnum.SELECTED) {
                    OrderDetailEntity orderDetail = new OrderDetailEntity();
                    orderDetail.setOrder(order);
                    orderDetail.setQuantity(item.getQuantity().intValue());
                    orderDetail.setProductItem(item.getProductItem());
                    orderDetail = this.orderDetailRepository.save(orderDetail);
                    ProductItemEntity productItem = item.getProductItem();
                    int newQuantity = productItem.getQuantity() - orderDetail.getQuantity();
                    productItem.setQuantity(newQuantity);
                    this.productItemRepository.save(productItem);
                    Long productQuantity = this.productItemRepository.getQuantityProduct(productItem.getProduct().getId());
                    if (productQuantity == 0) {
                        ProductEntity product = productItem.getProduct();
                        product.setStatus(ProductStatusType.UNAVAILABLE);
                        this.productRepository.save(product);
                    }

                } else {

                    throw new ResourceNotFoundException("CartItem with " + item.getId() + " invalid please check !");
                }
            }
        }
        // Send message to cart microservice
        rabbitTemplate.convertAndSend(exchange.getName(), "routing.A", orderInput);
    }

    public OrderDto mapperOrderEntityToDto(OrderEntity order) {
        BigDecimal totalPrice = BigDecimal.valueOf(0);
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        order.setPaymentMethod(order.getPaymentMethod());
        for (OrderDetailEntity orderDetail : order.getOrderDetails()) {
            BigDecimal total1 = new BigDecimal(orderDetail.getQuantity()).multiply(orderDetail.getProductItem().getPrice());
            totalPrice = totalPrice.add(total1);
        }
        orderDto.setTotalPrice(totalPrice);
        orderDto.setStatus(order.getStatus());
        return orderDto;
    }
}
