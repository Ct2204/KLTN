package kltn.userservice.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter

public class ConfirmLoginDto {
   @NotBlank(message = "The password field must not be left blank")
   @Size(max = 30,min = 8, message = "The password length should be between 8 and 30 characters!")
   private String password;

}
