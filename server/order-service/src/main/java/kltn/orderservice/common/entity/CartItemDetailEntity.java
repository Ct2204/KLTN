

package kltn.orderservice.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "cart_item_detail")
public class CartItemDetailEntity extends BaseAuditEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_item_id")
    private CartItemEntity cartItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_option_detail_id")
    private ProductOptionDetailEntity productOptionDetail;
}
