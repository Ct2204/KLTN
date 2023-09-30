
package kltn.userservice.common.entity;

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
@Table(name = "roles")
public class RoleEntity extends BaseAuditEntity {

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "role")
    private Set<RolePermissionEntity> rolePermissions = new LinkedHashSet<>();

    @OneToMany(mappedBy = "role")
    private Set<AdminRoleEntity> adminRoles = new LinkedHashSet<>();

    @OneToMany(mappedBy = "role")
    private Set<UserEntity> users = new LinkedHashSet<>();
}
