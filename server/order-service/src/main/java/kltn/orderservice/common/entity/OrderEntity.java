
package kltn.orderservice.common.entity;

import kltn.orderservice.common.vo.OrderStatusE;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class OrderEntity extends BaseAuditEntity {

    @Column(name = "status")
    private OrderStatusE status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_method")
    private UserPaymentMethodEntity paymentMethod;

    @Column(name = "delivery_method")
    private Short deliveryMethod;

    @OneToMany(mappedBy = "order")
    private List<OrderDetailEntity> orderDetails = new ArrayList<>();
}
