

package kltn.userservice.security.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import kltn.userservice.common.entity.*;
import kltn.userservice.common.vo.UserStatusEnum;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email")
        })
public class User extends BaseAuditEntity {

    @OneToOne(mappedBy = "user")
    private SellerEntity seller;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(max = 120)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @Column(name = "phone")
    private Long phone;

    @Column(name = "status")
    private UserStatusEnum status;

    @Column(name = "verification_code")
    private Integer verificationCode;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private Set<SocialAccountEntity> socialAccounts = new LinkedHashSet<>();


    @OneToOne(mappedBy = "users")
    private UserProfileEntity userProfile;

    @OneToMany(mappedBy = "user")
    private Set<UserRefreshTokenEntity> userRefreshToken;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private Set<UserAddressEntity> userAddresses = new LinkedHashSet<>();


    public User() {
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
