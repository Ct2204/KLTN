package kltn.userservice.security.model;


import jakarta.persistence.*;
import kltn.userservice.common.entity.BaseAuditEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "permissions", uniqueConstraints = {
        @UniqueConstraint(columnNames = "name")
})
@Getter
@Setter
public class Permission extends BaseAuditEntity {


    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private EPermission name;

    @Column(name = "description")
    private String description;

    @Column(name = "object", length = 50)
    private String object;

    @Column(name = "is_active")
    private Boolean isActive;


}
