package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/**
 * Represents a Person's date of birth in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidDoB(String)}
 */
public class DoB {

    public static final String MESSAGE_CONSTRAINTS =
            "Date of births should only contain numeric characters in the format yyyy-mm-dd. "
                    + "Acceptable date range is from 1900 Janurary 1st to today's date.";

    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */

    public final LocalDate dateOfBirth;

    /**
     * Constructs a {@code DoB}.
     *
     * @param dob A valid date of birth.
     */
    public DoB(String dob) {
        requireNonNull(dob);
        assert dob.length() == 10 : "Person dob string is of incorrect length";
        checkArgument(isValidDoB(dob), MESSAGE_CONSTRAINTS);
        dateOfBirth = LocalDate.parse(dob);
    }

    /**
     * Returns true if a given string is a valid name.
     */
    public static boolean isValidDoB(String test) {
        LocalDate first = LocalDate.parse("1900-01-01");
        LocalDate last = LocalDate.now();
        try {
            LocalDate date = LocalDate.parse(test);
            return !date.isBefore(first) && !date.isAfter(last);
        } catch (java.time.format.DateTimeParseException e) {
            return false;
        }
    }


    @Override
    public String toString() {
        return dateOfBirth.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DoB)) {
            return false;
        }

        DoB otherDoB = (DoB) other;
        return dateOfBirth.equals(otherDoB.dateOfBirth);
    }

    @Override
    public int hashCode() {
        return dateOfBirth.hashCode();
    }

}
