
package kltn.orderservice.order.service.impl;

import kltn.orderservice.order.dto.OrderDetailDto;
import kltn.orderservice.order.repository.OrderDetailRepository;
import kltn.orderservice.order.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    public OrderDetailServiceImpl() {
    }

    public List<OrderDetailDto> getOrderDetailByOrderId(Long id) {
        return this.orderDetailRepository.getOrderDetailByOrderId(id);
    }
}
