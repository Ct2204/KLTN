

package kltn.orderservice.order.service;



import kltn.orderservice.order.dto.OrderDetailDto;

import java.util.List;

public interface OrderDetailService {

    List<OrderDetailDto> getOrderDetailByOrderId(Long Id);
}
