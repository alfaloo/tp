package seedu.address;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.format.FormatStyle;

public class test {
    public static String str(LocalDateTime t) {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(t);
    }

    public static void main(String args[]) {

        String pattern = "yyyy-MM-dd HH:mm";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

        try {
            String datetime = "2024-01-01           11:00";
            String date = datetime.substring(0, 11) + datetime.substring(11).strip();
            LocalDateTime time = LocalDateTime.parse(date, formatter);
            System.out.println(str(time));
        } catch (DateTimeParseException e) {
            System.out.println(e.getMessage());
        }
    }
}