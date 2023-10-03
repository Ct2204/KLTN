

package kltn.orderservice.common.entity;


import kltn.orderservice.common.vo.SocialProvider;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "social_account")
public class SocialAccountEntity extends BaseAuditEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "social_account_user_id", length = 100)
    private String socialAccountUserId;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "provider")
    @Enumerated(EnumType.STRING)
    private SocialProvider provider;
}
