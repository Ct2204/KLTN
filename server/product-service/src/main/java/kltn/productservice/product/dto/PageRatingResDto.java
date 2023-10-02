
package kltn.productservice.product.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PageRatingResDto extends PageableDto{

    private Long productId;

    private Float averageOfStars;

    private List<ProductRatingResDto> listRating;

    public PageRatingResDto(int totalPages, long totalElements, int perPage,
                            int numberOfElements, int currentPage,
                            boolean first, boolean last, Long productId, Float averageOfStars,
                            List<ProductRatingResDto> listRating) {
        super(totalPages, totalElements, perPage, numberOfElements, currentPage, first, last);
        this.productId = productId;
        this.averageOfStars = averageOfStars;
        this.listRating = listRating;
    }

}
