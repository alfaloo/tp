package seedu.address.model.appointment.exceptions;

import seedu.address.logic.commands.exceptions.CommandException;

/**
 * Signals that the operation is unable to find the specified appointment.
 */
public class AppointmentNotFoundException extends CommandException {

    /**
     * Creates new instance of AppointmentNotFoundException.
     */
    public AppointmentNotFoundException() {
        super("Unable to locate appointment");
    }
}
