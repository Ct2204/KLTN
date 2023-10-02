package kltn.cartservice.common.vo;


import com.fasterxml.jackson.annotation.JsonCreator;

public enum Gender {

    Male, Female, Other;

    @JsonCreator
    public static Gender fromString(String value) {
        if (value != null) {
            for (Gender gender : Gender.values()) {
                if (value.equalsIgnoreCase(gender.name())) {
                    return gender;
                }
            }
        }
        throw new IllegalArgumentException("Invalid gender: " + value);
    }
}
