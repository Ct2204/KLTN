package kltn.userservice.security.model;


import jakarta.persistence.*;
import kltn.userservice.common.entity.BaseAuditEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "roles")
@RequiredArgsConstructor
public class Role extends BaseAuditEntity {

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ERole name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "roles_permissions",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private Set<Permission> permissions = new HashSet<>();

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority("ROLE_"+permission.getName().getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_"+this.name.getRole()));
        return authorities;
    }

    public List<String> getListRoles() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> permission.getName().getPermission())
                .collect(Collectors.toList());
        authorities.add(this.name.getRole());
        return authorities;
    }

//    public Role() {
//
//    }

    public Role(ERole name) {
        this.name = name;
    }


    public ERole getName() {
        return name;
    }

    public void setName(ERole name) {
        this.name = name;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }


}
