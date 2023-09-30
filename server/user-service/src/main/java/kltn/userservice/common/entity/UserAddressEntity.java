
package kltn.userservice.common.entity;


import jakarta.persistence.*;
import kltn.userservice.common.vo.AddressStatus;
import lombok.Getter;
import lombok.Setter;	

@Getter
@Setter
@Entity
@Table(name = "user_address")
public class UserAddressEntity extends BaseAuditEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "full_name", length = 100)
    private String fullName;

    @Column(name = "company", length = 100)
    private String company;

    @Column(name = "phone")
    private Long phone;

    @Column(name = "region", length = 50)
    private String region;

    @Column(name = "district", length = 50)
    private String district;

    @Column(name = "ward", length = 50)
    private String ward;

    @Column(name = "street", length = 100)
    private String street;

    @Column(name = "status")
    private AddressStatus status;
}
