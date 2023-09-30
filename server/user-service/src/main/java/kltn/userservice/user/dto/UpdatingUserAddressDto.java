
package kltn.userservice.user.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import kltn.userservice.common.vo.AddressStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdatingUserAddressDto {

    @NotNull(message = "User id shouldn't be null")
    private Long userId;

    @NotBlank(message = "The name field must not be left blank")
    private String fullName;

    private String company;

    @NotNull(message = "Phone number must not be left blank")
    @Pattern(regexp = "\\d{10,11}", message = "Invalid phone number!")
    private String phone;

    @NotBlank(message = "The region field must not be left blank")
    private String region;

    @NotBlank(message = "The district field must not be left blank")
    private String district;

    @NotBlank(message = "The ward field must not be left blank")
    private String ward;

    @NotBlank(message = "The street field must not be left blank")
    private String street;

    @NotNull(message = "The status field must not be left blank")
    private AddressStatus status;

    public Long getPhone() {
        return Long.parseLong(this.phone);
    }
}
