

package kltn.productservice.product.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductSearchResDto {

    private Long id;

    private String imageUrl;

    private String productTitle;

    private Double staring;

    private BigDecimal price;

    private BigDecimal price_sale;

    public ProductSearchResDto(Long id, String imageUrl, String productTitle, Double staring, BigDecimal price, BigDecimal price_sale) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.productTitle = productTitle;
        this.staring = staring;
        this.price = price;
        this.price_sale = price_sale;
    }
}
