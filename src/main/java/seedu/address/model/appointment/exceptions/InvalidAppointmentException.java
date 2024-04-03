package seedu.address.model.appointment.exceptions;

import seedu.address.logic.commands.exceptions.CommandException;

/**
 * Signals that the operation is unable to find the specified person.
 */
public class InvalidAppointmentException extends CommandException {

    public InvalidAppointmentException() {
        super("This appointment is invalid due to invalid inputs.");
    }
}
