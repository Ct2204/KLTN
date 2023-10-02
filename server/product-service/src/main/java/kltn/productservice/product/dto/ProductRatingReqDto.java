
package kltn.productservice.product.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ProductRatingReqDto {

    @NotNull(message = "Product id must not be null")
    private Long productId;

    @Min(value = 1, message = "Star must be >=1")
    @Max(value = 5, message = "Star must be <=5")
    @NotNull(message = "Start rating must not be null")
    private Short starRating;

    private String comment;

    private List<MultipartFile> listMedia= new ArrayList<>();
}
