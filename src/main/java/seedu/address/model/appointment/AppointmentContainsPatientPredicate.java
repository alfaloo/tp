package seedu.address.model.appointment;

import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;

import seedu.address.commons.util.StringUtil;
import seedu.address.commons.util.ToStringBuilder;

/**
 * Represents a Predicate used to test if an Appointment contains specified patient keywords.
 */
public class AppointmentContainsPatientPredicate implements Predicate<Appointment> {
    private static final Logger logger = Logger.getLogger(AppointmentContainsPatientPredicate.class.getName());
    private final List<String> keywords;

    public AppointmentContainsPatientPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Appointment appointment) {
        assert appointment != null : "Appointment cannot be null";
        logger.log(Level.INFO, "Testing appointment: " + appointment);
        return keywords.stream()
                .anyMatch(keyword -> {
                    boolean contains = StringUtil.containsWordIgnoreCase(appointment.getPatientNric().nric, keyword);
                    logger.log(Level.INFO, "Keyword: " + keyword + " contains in appointment: " + contains);
                    return contains;
                });
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AppointmentContainsPatientPredicate)) {
            return false;
        }

        AppointmentContainsPatientPredicate otherPredicate = (AppointmentContainsPatientPredicate) other;
        return keywords.equals(otherPredicate.keywords);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("keywords", keywords).toString();
    }
}
