package seedu.address.model.appointment;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Class encapsulating an appointment's date and corresponding methods
 */
public class AppointmentDateTime {

    // Message to output in case constraints are not met
    public static final String MESSAGE_CONSTRAINTS =
            "Appointment date should be in the format of yyyy-MM-dd HH:mm.";

    // Variable storing appointment date in a local datetime instance
    public final LocalDateTime appointmentDateTime;

    /**
     * Constructs new AppointmentDate object using an input date string in yyyy-MM-dd HH:mm format
     * @param dateStr input string to be stored
     */
    public AppointmentDateTime(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        requireNonNull(dateStr);
        checkArgument(isValidDate(dateStr), MESSAGE_CONSTRAINTS);
        this.appointmentDateTime = LocalDateTime.parse(dateStr, formatter);
    }

    /**
     * Overloaded constructor that constructs a new instance using a LocalDateTime rather than datetime string
     * @param dateTime LocalDateTime instance to construct AppointmentDate around
     */
    public AppointmentDateTime(LocalDateTime dateTime) {
        requireNonNull(dateTime);
        this.appointmentDateTime = dateTime;
    }

    /**
     * Checks if a provided input date string is in a valid format
     * @param dateStr input date string
     * @return boolean indicating if format is valid or not
     */
    public static boolean isValidDate(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        try {
            LocalDateTime temp = LocalDateTime.parse(dateStr, formatter);
            //LocalDate today = LocalDate.now();
            //return temp.isAfter(today);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }

    /**
     * Returns string version of appointment date for printing
     * @return String stringed appointment date
     */
    @Override
    public String toString() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(this.appointmentDateTime);
    }

    /**
     * Checks if input object is practically equal to this AppointmentDate object
     * @param obj input object
     * @return boolean indicating if compared objects are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof AppointmentDateTime)) {
            return false;
        }

        AppointmentDateTime ad = (AppointmentDateTime) obj;
        return appointmentDateTime.equals(ad.appointmentDateTime);
    }

    /**
     * Returns hashcode of appointment date
     * @return int hashcode
     */
    @Override
    public int hashCode() {
        return appointmentDateTime.hashCode();
    }

    /**
     * Compares two AppointmentDate instances together
     * @param compareValue value to compare with current instance
     * @return integer reflecting whether compareValue is greater, less, or equal
     */
    public int compareTo(AppointmentDateTime compareValue) {
        return this.appointmentDateTime.compareTo(compareValue.appointmentDateTime);
    }

}
