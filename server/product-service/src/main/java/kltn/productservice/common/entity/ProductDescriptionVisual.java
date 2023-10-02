package kltn.productservice.common.entity;


import jakarta.persistence.*;
import kltn.productservice.common.vo.ProductDescriptionVisualType;
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
