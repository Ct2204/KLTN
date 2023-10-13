package kltn.userservice.user.dto;



import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import kltn.userservice.common.vo.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdatingUserProfileDto {

    @NotBlank(message = "The name field must not be left blank")
    private String fullName;

    @NotNull(message = "The gender field must not be left blank")
    private Gender gender;

    @NotBlank(message = "The birthday field must not be left blank")
    @Pattern(regexp = "^(\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|1\\d|2[0-9]|3[0-1]))$", message = "Date must be in the format yyyy-MM-dd")
    private String birthday;

    public Date getBirthdayInDate() {
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);

        String yearString = this.birthday.substring(0, 4);
        String monthString = this.birthday.substring(5, 7);
        String dayString = this.birthday.substring(8, 10);

        int year = Integer.parseInt(yearString);
        int month = Integer.parseInt(monthString);
        int day = Integer.parseInt(dayString);

        YearMonth yearMonth = YearMonth.of(year, month);
        int dayInMonth = yearMonth.lengthOfMonth();

        if(dayInMonth < day) {
            throw new IllegalArgumentException("Invalid birthday!");
        }

        try {
            return dateFormat.parse(this.birthday);

        } catch (ParseException e) {
            throw new IllegalArgumentException("Date must be in the format yyyy-MM-dd");
        }
    }
}
