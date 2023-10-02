package kltn.productservice.common.vo;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ProductItemStatus {
    EXISTING,
    DELETED;

    @JsonCreator
    public static ProductItemStatus fromString(String value) {
        if (value != null) {
            for (ProductItemStatus status : ProductItemStatus.values()) {
                if (value.equalsIgnoreCase(status.name())) {
                    return status;
                }
            }
        }
        throw new IllegalArgumentException("Invalid product item status: " + value +"!");
    }
}
