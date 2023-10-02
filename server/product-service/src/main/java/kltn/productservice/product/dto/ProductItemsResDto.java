
package kltn.productservice.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductItemsResDto {

    private List<ProductOptionsDto> options = new ArrayList<>();

    private List<ProductItemsDto> productItems;

}
