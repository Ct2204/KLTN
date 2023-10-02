
package kltn.productservice.product.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

public class ProductDescriptionDto {

    private Long productId;

    private String description;

    private List<ProductDescriptionVisualDto> listMedia;
}
