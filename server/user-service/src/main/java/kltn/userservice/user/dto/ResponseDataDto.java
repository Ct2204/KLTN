

package kltn.userservice.user.dto;


import kltn.userservice.common.dto.ResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDataDto extends ResponseDto {
    private Object data;
}
