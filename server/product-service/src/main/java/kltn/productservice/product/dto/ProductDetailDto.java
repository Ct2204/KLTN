
package kltn.productservice.product.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;


@Getter
@Setter
public class ProductDetailDto {
    private Long id;

    private Long productId;

    private String key;

    private String value;

    private Instant createdAt;

    private Instant updatedAt;

}
