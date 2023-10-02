package kltn.cartservice.common.entity;

import kltn.cartservice.common.vo.ProductVisualType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "product_visual")
public class ProductVisualEntity extends BaseAuditEntity {

    @Column(name = "type")
    private ProductVisualType type;

    @Column(name="url")
    private String url;

    @Column(name="slug")
    private String slug;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_option_detail_id")
    private ProductOptionDetailEntity productOptionDetail;

}

