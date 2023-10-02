package kltn.cartservice.common.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "sellers")
public class SellerEntity extends BaseAuditEntity {

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "seller_name")
    private String sellerName;

    @Column(name = "region")
    private String region;

    @Column(name = "district")
    private String district;

    @Column(name = "ward")
    private String ward;

    @Column(name = "street")
    private String street;

    @Column(name = "status")
    private Short status;

    @Column(name = "url")
    private String url;

    @OneToMany(mappedBy = "seller")
    private List<ProductEntity> products = new ArrayList<>();
}
