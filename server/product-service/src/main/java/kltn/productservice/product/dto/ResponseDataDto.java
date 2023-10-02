
package kltn.productservice.product.dto;


import kltn.productservice.common.dto.ResponseDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseDataDto extends ResponseDto {
    private Object data;
}
