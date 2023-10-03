
package kltn.orderservice.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "order_detail")
public class OrderDetailEntity extends BaseAuditEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_item_id")
    private ProductItemEntity productItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_option_detail_id")
    private ProductOptionDetailEntity productOptionDetail;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "status")
    private Short status;
}
