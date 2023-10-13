package kltn.userservice.security.model;

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
