
package kltn.cartservice.common.entity;


import kltn.cartservice.common.vo.CartItemStatusEnum;
import jakarta.persistence.*;
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
