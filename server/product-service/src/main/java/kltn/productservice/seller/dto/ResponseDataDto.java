

package kltn.productservice.seller.dto;


import kltn.productservice.common.dto.ResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDataDto extends ResponseDto {
    private Object data;
}
