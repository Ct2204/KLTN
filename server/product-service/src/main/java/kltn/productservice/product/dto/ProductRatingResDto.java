
package kltn.productservice.product.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ProductRatingResDto {

    private Long id;

    private Long productId;

    private Long userId;

    private String fullName;

    private String avatarUrl;

    private Short starRating;

    private String comment;

    private Instant createdAt;

    private Instant updatedAt;

    private List<ProductRatingVisualDto> listMedia = new ArrayList<>();

}
