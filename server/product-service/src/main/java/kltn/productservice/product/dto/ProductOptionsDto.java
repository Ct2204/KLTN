
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
public class ProductOptionsDto {

    private String option;

    private List<List<ProductOptionDetailImageDto>> images = new ArrayList<>();

    private List<String> values = new ArrayList<>();
}