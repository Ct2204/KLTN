
package kltn.productservice.seller.dto;

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
public class ProductItemDto {

    private Long id;
    private String title;
    private BigDecimal price;
    private Integer quantity;
    private List<Integer> optionValueIndex = new ArrayList<>();
}
