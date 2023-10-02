package kltn.cartservice.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "product_rating_visual")
public class ProductRatingVisualEntity extends BaseAuditEntity {

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "rating_id")
    private ProductRatingEntity rating;

    @Column(name = "type")
    private Short type;

    @Column(name = "url")
    private String url;
}
