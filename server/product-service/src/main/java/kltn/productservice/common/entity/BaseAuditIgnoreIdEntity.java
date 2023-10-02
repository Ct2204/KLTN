package kltn.productservice.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.Objects;

public class BaseAuditIgnoreIdEntity {

    private String createdBy;

    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    private String updatedBy;

    @PrePersist
    public void onPrePersist() {
        String currentUser = currentUser();
        this.createdAt = Instant.now();
        this.createdBy = currentUser;
        this.updatedAt = Instant.now();
        this.updatedBy = currentUser;
        this.createdAt = Instant.now();
    }

    @PreUpdate
    public void onPreUpdate() {
        this.updatedAt = Instant.now();
        this.updatedBy = currentUser();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BaseAuditIgnoreIdEntity that = (BaseAuditIgnoreIdEntity) o;
        return Objects.equals(createdBy, that.createdBy) &&
                Objects.equals(updatedBy, that.updatedBy) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), createdBy, updatedBy, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return "BaseAuditIgnoreIdEntity{" +
                "createdBy='" + createdBy + '\'' +
                ", updatedBy='" + updatedBy + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                "} " + super.toString();
    }

    /**
     * get current user information by token
     *
     * @return String name user
     */
    private String currentUser() {
//        return Optional.ofNullable(SecurityContextUtil.getCurrentEmployeeId().orElse("anonymous")).get();
        return "anonymous";
    }
}
