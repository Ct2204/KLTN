

package kltn.userservice.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterDto {

    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "Invalid email!")
    @Size(max = 30, message = "The maximum length for an email is 30 characters")
    private String email;

    @NotBlank(message = "Password shouldn't be blank")
    @Size(max = 30, message = "The limit password length is 60 characters")
    @Size(min = 8, message = "The min password length is 8 characters")
    private String password;

    @NotBlank(message = "The password field must not be left blank")
    @Size(max = 60, message = "The maximum length for an confirmPassword is 60 characters")
    private String confirmPassword;

    @NotBlank(message = "The full name field must not be left blank")
    @Size(max = 30, message = "The maximum length for an full name is 30 characters")
    private String fullName;
}