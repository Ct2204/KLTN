package kltn.userservice.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter

public class EmailDto {
    @Email(message = "Invalid email address!" )
    @Size(max = 30, message = "The maximum length for an email is 30 characters")
    private String email;


}
