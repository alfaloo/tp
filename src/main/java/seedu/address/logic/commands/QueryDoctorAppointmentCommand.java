package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.logging.Level;
import java.util.logging.Logger;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.appointment.AppointmentContainsDoctorPredicate;

/**
 * Represents a command for querying appointments for a specific doctor.
 * The command searches for appointments of doctors whose NRICs contain any of the specified keywords
 * (case-insensitive) and displays them as a list with index numbers.
 */
public class QueryDoctorAppointmentCommand extends Command {

    public static final String COMMAND_WORD = "apptfordoctor";
    private static final Logger logger = Logger.getLogger(QueryDoctorAppointmentCommand.class.getName());

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all appointments of doctors whose "
            + "nrics contain any of the specified keywords (case-insensitive) and displays them as a "
            + "list with index numbers.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...(Keywords have to be the "
            + "exact NRICs of the doctor(s) in question)\n"
            + "Example: " + COMMAND_WORD + " alice bob T1234567A S7654321A";

    private final AppointmentContainsDoctorPredicate predicate;

    /**
     * Constructs a QueryDoctorAppointmentCommand with the given predicate.
     *
     * @param predicate The predicate to be used for querying doctor appointments.
     * @throws NullPointerException if the predicate is null.
     */
    public QueryDoctorAppointmentCommand(AppointmentContainsDoctorPredicate predicate) {
        requireNonNull(predicate, "Predicate cannot be null in QueryDoctorAppointmentCommand constructor.");
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model, "Model cannot be null in execute method of QueryDoctorAppointmentCommand.");

        logger.log(Level.INFO, "Executing QueryDoctorAppointmentCommand");

        model.updateFilteredAppointmentList(predicate);
        int numberOfAppointments = model.getFilteredAppointmentList().size();
        logger.log(Level.INFO, "Number of appointments found: " + numberOfAppointments);

        return new CommandResult(
                String.format(Messages.MESSAGE_APPOINTMENTS_LISTED_OVERVIEW, numberOfAppointments));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof QueryDoctorAppointmentCommand)) {
            return false;
        }

        QueryDoctorAppointmentCommand otherQueryDoctorAppointmentCommand = (QueryDoctorAppointmentCommand) other;
        return predicate.equals(otherQueryDoctorAppointmentCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
