
package kltn.productservice.product.dto;

import kltn.productservice.common.vo.ProductDescriptionVisualType;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class ProductDescriptionVisualDto {
    private Long id;

    private ProductDescriptionVisualType type;

    private String url;

    private Instant createdAt;

    private Instant updatedAt;

}
