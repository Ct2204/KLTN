package kltn.orderservice.order.dto;





import kltn.orderservice.common.vo.OrderStatusE;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {

    private Long id;

    private Short deliveryMethod;

    private OrderStatusE status;

    private Long paymentMethodId;

    private BigDecimal totalPrice;
}
