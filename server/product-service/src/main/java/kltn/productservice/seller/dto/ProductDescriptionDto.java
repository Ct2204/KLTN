
package kltn.productservice.seller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDescriptionDto {

    private Long productId;
    private Long sellerId;
    private String description;
    private List<ProductDescriptionVisualDto> images;
}
