package kltn.userservice.user.dto;


import kltn.userservice.common.vo.AddressStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserAddressDto {

    private Long id;

    private String fullName;

    private String company;

    private Long phone;

    private String region;

    private String district;

    private String ward;

    private String street;

    private AddressStatus status;
}
