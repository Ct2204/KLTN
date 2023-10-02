
package kltn.productservice.common.entity;


import jakarta.persistence.*;
import kltn.productservice.common.vo.ProductVisualType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "product_description_visual")
public class ProductDescriptionVisualEntity extends BaseAuditEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_description_id")
    private ProductDescriptionEntity productDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @Column(name = "type")
    private ProductVisualType type;

    @Column(name = "url")
    private String url;
}
