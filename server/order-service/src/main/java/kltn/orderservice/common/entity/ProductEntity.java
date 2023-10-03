package kltn.orderservice.common.entity;

import kltn.orderservice.common.vo.ProductStatusType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "products")
public class ProductEntity extends BaseAuditEntity {

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "seller_id")
    private SellerEntity seller;

    @Column(name = "title")
    private String title;

    @Column(name = "slug")
    private String slug;

    @Column(name = "sku", length = 100)
    private String sku;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id")
    private ProductCategoryEntity category;

    @Column(name = "quantity_in_stock")
    private Integer quantityInStock;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "percent_discount")
    private BigDecimal percentDiscount;

    @Column(name = "price_sales")
    private BigDecimal priceSales;

    @Column(name = "status")
    private ProductStatusType status;

    @OneToMany(mappedBy = "product")
    private List<ProductDescriptionEntity> productDescriptions = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    private List<ProductDescriptionVisual> productDescriptionVisuals = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    private Set<ProductDetailEntity> details = new LinkedHashSet<>();

    @OneToMany(mappedBy = "product")
    private Set<ProductDescriptionVisual> descriptionVisuals = new LinkedHashSet<>();

    @OneToMany(mappedBy = "product")
    private Set<ProductVisualEntity> productVisuals = new LinkedHashSet<>();

    @OneToMany(mappedBy = "product")
    private Set<ProductRatingEntity> productRatings = new LinkedHashSet<>();

    @OneToMany(mappedBy = "product")
    private Set<ProductRatingVisualEntity> productRatingVisuals = new LinkedHashSet<>();

    @OneToMany(mappedBy = "product")
    private List<ProductOptionEntity> productOptions = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    private List<ProductOptionDetailEntity> productOptionDetails = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    private List<ProductItemEntity> productItems = new ArrayList<>();
}