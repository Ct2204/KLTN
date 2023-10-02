
package kltn.productservice.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductItemsDto {

    private Long id;

    private Long productId;

    private BigDecimal price;

    private Integer quantity;

    private List<Integer> optionValueIndex = new ArrayList<>();
}