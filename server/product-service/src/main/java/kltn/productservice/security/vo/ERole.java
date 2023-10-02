
package kltn.productservice.security.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ERole {
    USER("USER"),
    SELLER("SELLER"),
    ADMIN("ADMIN");

    @Getter
    private final String role;
}
