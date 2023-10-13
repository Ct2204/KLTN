package kltn.userservice.user.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class VerificationCodeDto {
    @Size(max = 6, message = "The maximum length for a verificationCode is 6 characters")
    private String verificationCode;
}
