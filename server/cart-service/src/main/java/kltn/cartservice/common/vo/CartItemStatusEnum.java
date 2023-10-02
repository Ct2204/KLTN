
package kltn.cartservice.common.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CartItemStatusEnum {

    SELECTED(0),
    NOT_SELECTED(1);

    private final int value;
}
