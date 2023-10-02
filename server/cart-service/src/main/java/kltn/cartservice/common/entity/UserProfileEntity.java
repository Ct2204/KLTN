

package kltn.cartservice.common.entity;


import kltn.cartservice.common.vo.Gender;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "user_profile")
public class UserProfileEntity extends BaseAuditEntity {

    @MapsId
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity users;

    @Column(name = "profile_picture")
    private String profilePicture;

    @Column(name = "full_name", length = 100)
    private String fullName;

    @Column(name = "birthday")
    private Date birthday;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;
}
