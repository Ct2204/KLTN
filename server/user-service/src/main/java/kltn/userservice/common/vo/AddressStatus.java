package kltn.userservice.common.vo;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum AddressStatus {

    NOT_DEFAULT,DEFAULT, DELETED;

    @JsonCreator
    public static AddressStatus fromString(String value) {
        if (value != null) {
            for (AddressStatus address : AddressStatus.values()) {
                if (value.equalsIgnoreCase(address.name())) {
                    return address;
                }
            }
        }
        throw new IllegalArgumentException("Invalid address status: " + value +"!");
    }
}
