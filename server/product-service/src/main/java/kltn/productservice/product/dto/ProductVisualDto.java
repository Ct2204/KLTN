
package kltn.productservice.product.dto;


import kltn.productservice.common.vo.ProductVisualType;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class ProductVisualDto {
    private Long id;

    private ProductVisualType type;

    private String url;

    private String slug;

    private Instant createdAt;

    private Instant updatedAt;

}
