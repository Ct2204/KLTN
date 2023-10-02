
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
@Table(name = "notification_categories")
public class NotificationCategoryEntity extends BaseAuditEntity {

    @Column(name = "title", length = 50)
    private String title;

    @OneToMany(mappedBy = "category")
    private Set<NotificationEntity> notifications = new LinkedHashSet<>();
}
