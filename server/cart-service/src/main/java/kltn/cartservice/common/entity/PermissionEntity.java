
package kltn.cartservice.common.entity;

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
@Table(name = "permissions")
public class PermissionEntity extends BaseAuditEntity {

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "object", length = 50)
    private String object;

    @Column(name = "is_active")
    private Boolean isActive;

    @OneToMany(mappedBy = "permission")
    private Set<RolePermissionEntity> rolePermissions = new LinkedHashSet<>();

    @OneToMany(mappedBy = "permission")
    private Set<AdminPermissionEntity> adminPermissions = new LinkedHashSet<>();

    @OneToMany(mappedBy = "permission")
    private Set<MenuEntity> menus = new LinkedHashSet<>();
}
