

package kltn.orderservice.common.entity;



import jakarta.persistence.*;
import kltn.orderservice.common.vo.CartItemStatusEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "cart_items")
public class CartItemEntity extends BaseAuditEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_item_id")
    ProductItemEntity productItem;

    @Column(name = "quantity")
    private Short quantity;

    @Column(name = "status")
    private CartItemStatusEnum status;
}
