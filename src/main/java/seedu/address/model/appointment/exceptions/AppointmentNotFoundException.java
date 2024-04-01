package seedu.address.model.appointment.exceptions;

import seedu.address.logic.commands.exceptions.CommandException;

/**
 * Signals that the operation is unable to find the specified person.
 */
public class AppointmentNotFoundException extends CommandException {
    public AppointmentNotFoundException() {
        super("Unable to locate appointment");
    }
}
