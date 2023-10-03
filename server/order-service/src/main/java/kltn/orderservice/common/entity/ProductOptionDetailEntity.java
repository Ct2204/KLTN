package kltn.orderservice.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "product_option_detail")
public class ProductOptionDetailEntity extends BaseAuditEntity {

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_option_id")
    private ProductOptionEntity productOption;

    @Column(name = "value", length = 50)
    private String value;

    @OneToMany(mappedBy = "productOptionDetail")
    private List<ProductVisualEntity> productVisuals = new ArrayList<>();
}