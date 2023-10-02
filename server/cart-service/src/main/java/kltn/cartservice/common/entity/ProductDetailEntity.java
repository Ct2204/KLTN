package kltn.cartservice.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="product_detail")
public class ProductDetailEntity extends BaseAuditEntity {

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @Column(name = "key", length = 30)
    private String key;

    @Column(name = "value", length = 100)
    private String value;
}