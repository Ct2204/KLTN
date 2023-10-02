
package kltn.productservice.product.dto;


import kltn.productservice.common.vo.ProductStatusType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;


@Getter
@Setter
public class ProductDto {
    // Product
    private Long id;

    private String title;

    private String slug;

    private String sku;

    private Long CategoryId;

    private Integer quantityInStock;

    private BigDecimal price;

    private BigDecimal percentDiscount;

    private BigDecimal priceSales;

    private ProductStatusType status;

    private float star_rating;

    private List<ProductVisualDto> listMediaProduct;

}
