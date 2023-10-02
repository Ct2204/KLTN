package kltn.cartservice.common.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product_item_detail")
public class ProductItemDetailEntity extends BaseAuditEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_item_id")
    private ProductItemEntity productItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_option_detail_id")
    private ProductOptionDetailEntity productOptionDetail;
}
