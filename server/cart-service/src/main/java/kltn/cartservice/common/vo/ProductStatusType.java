package kltn.cartservice.common.vo;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ProductStatusType {
    DRAFT,AVAILABLE,UNAVAILABLE,RECYCLE,DELETED;

    @JsonCreator
    public static ProductStatusType fromString(String value) {
        if (value != null) {
            for (ProductStatusType status : ProductStatusType.values()) {
                if (value.equalsIgnoreCase(status.name())) {
                    return status;
                }
            }
        }
        throw new IllegalArgumentException("Invalid product status: " + value +"!");
    }
}
