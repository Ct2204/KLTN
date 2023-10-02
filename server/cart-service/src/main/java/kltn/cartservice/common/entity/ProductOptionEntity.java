package kltn.cartservice.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "product_options")
public class ProductOptionEntity extends BaseAuditEntity {

    @Column(name = "option", length = 50)
    private String option;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @OneToMany(mappedBy = "productOption", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductOptionDetailEntity> productOptionDetails = new ArrayList<>();
}
