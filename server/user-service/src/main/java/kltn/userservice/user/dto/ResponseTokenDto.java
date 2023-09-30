
package kltn.userservice.user.dto;


import kltn.userservice.common.dto.ResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseTokenDto extends ResponseDto {
    private String token;
}
