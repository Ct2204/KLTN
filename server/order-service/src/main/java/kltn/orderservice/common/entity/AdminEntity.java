
package kltn.orderservice.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "admins")
public class AdminEntity extends BaseAuditEntity {

    @Column(name = "username", length = 50)
    private String username;

    @Column(name = "password", length = 30)
    private String password;

    @OneToMany(mappedBy = "admin")
    private Set<AdminRoleEntity> adminRoles = new LinkedHashSet<>();

    @OneToMany(mappedBy = "admin")
    private Set<AdminPermissionEntity> adminPermissions = new LinkedHashSet<>();
}
