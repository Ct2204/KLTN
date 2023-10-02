package kltn.cartservice.common.entity;

import kltn.cartservice.common.vo.ProductDescriptionVisualType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "product_description_visual")
public class ProductDescriptionVisual extends BaseAuditEntity {

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_description_id")
    private ProductDescriptionEntity productDescription;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @Column(name = "type")
    private ProductDescriptionVisualType type;

    @Column(name = "url")
    private String url;
}
