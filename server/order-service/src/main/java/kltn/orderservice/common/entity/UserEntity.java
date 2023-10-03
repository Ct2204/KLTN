
package kltn.orderservice.common.entity;

import kltn.orderservice.common.vo.UserStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "users")
public class UserEntity extends BaseAuditEntity {

    @OneToOne(mappedBy = "user")
    private SellerEntity seller;

    @Column(name = "email", length = 30, unique = true)
    private String email;

    @Column(name = "phone")
    private Long phone;

    @Column(name = "password")
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private RoleEntity role;

    @Column(name = "status")
    private UserStatusEnum status;

    @Column(name = "verification_code")
    private Integer verificationCode;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private Set<SocialAccountEntity> socialAccounts = new LinkedHashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private Set<CartItemEntity> cartItems = new LinkedHashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private Set<ProductRatingEntity> productRatings = new LinkedHashSet<>();


    @OneToOne(mappedBy = "users")
    private UserProfileEntity userProfile;

    @OneToMany(mappedBy = "user")
    private Set<UserRefreshTokenEntity> userRefreshToken;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private Set<OrderEntity> orders = new LinkedHashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private Set<NotificationEntity> notifications = new LinkedHashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private Set<UserAddressEntity> userAddresses = new LinkedHashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private Set<UserPaymentMethodEntity> userPaymentMethodEntities = new LinkedHashSet<>();
}

