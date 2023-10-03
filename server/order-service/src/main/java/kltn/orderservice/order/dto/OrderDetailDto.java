package kltn.orderservice.order.dto;





import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderDetailDto {

    private Long id;

    private Long orderId;

    private Long productItemId;

    private Integer quantity;

    private Short status;

    public OrderDetailDto(Long id, Long orderId, Long productItemId, Integer quantity, Short status) {
        this.id = id;
        this.orderId = orderId;
        this.productItemId = productItemId;
        this.quantity = quantity;
        this.status = status;
    }
}
