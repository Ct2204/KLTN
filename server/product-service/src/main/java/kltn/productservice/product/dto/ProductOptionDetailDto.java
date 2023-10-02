
package kltn.productservice.product.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
public class ProductOptionDetailDto {

    private Long id;

    private Long productOptionId;

    private String value;

    private Instant createdAt;

    private Instant updatedAt;

    private Long productId;

    private List<ProductVisualDto> listProductOptionDetailVisuals;
}
