package kltn.userservice.security.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum EPermission {

    USER_READ("USER_READ"),
    USER_CREATE("USER_CREATE"),
    USER_UPDATE("USER_UPDATE"),
    USER_DELETE("USER_DELETE"),


    SELLER_READ("SELLER_READ"),
    SELLER_UPDATE("SELLER_UPDATE"),
    SELLER_DELETE("SELLER_DELETE"),
    SELLER_CREATE("SELLER_CREATE"),

    ADMIN_READ("ADMIN_READ"),
    ADMIN_UPDATE("ADMIN_UPDATE"),
    ADMIN_DELETE("ADMIN_DELETE"),
    ADMIN_CREATE("ADMIN_CREATE");

    @Getter
    private final String permission;

}
