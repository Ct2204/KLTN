package kltn.cartservice.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderRequestDto {

    private @NotNull(
            message = "cant null"
    ) List<Long> cartItemId;

    @NotNull(message = "cant null")
    private Long userId;
}
