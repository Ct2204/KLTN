
package kltn.userservice.user.dto;


import kltn.userservice.common.vo.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDto {

    private Long userId;

    private String profilePicture;

    private String fullName;

    private Gender gender;

    private Date birthday;

    private String email;

    private Long phone;
}
