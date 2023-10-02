
package kltn.productservice.product.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
public class ProductRatingVisualDto {

    private Long id;

    private Long productRatingId;

    private Short type;

    private String url;

    private Instant createdAt;

    private Instant updatedAt;

}
