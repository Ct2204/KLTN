
package kltn.productservice.product.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
public class ProductOptionDto {

    private Long id;

    private String option;

    private Instant createdAt;

    private Instant updatedAt;

    private Long productId;

    private List<ProductOptionDetailDto> listProductOptionDetail;
}
