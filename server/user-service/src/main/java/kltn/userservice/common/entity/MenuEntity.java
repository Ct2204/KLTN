package kltn.userservice.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "menu")
public class MenuEntity extends BaseAuditEntity {

    @Column(name = "parent_id")
    private Short parentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permission_id")
    private PermissionEntity permission;

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "slug", length = 100)
    private String slug;

    @Column(name = "icon", length = 100)
    private String icon;

    @Column(name = "description")
    private String description;
}
