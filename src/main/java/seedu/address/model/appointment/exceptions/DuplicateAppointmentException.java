package seedu.address.model.appointment.exceptions;

import seedu.address.logic.commands.exceptions.CommandException;

/**
 * Signals that the operation will result in duplicate Appointments.
 * (Appointments are considered duplicates if they have the same identity).
 */
public class DuplicateAppointmentException extends CommandException {

    /**
     * Creates new instance of DuplicateAppointmentException.
     */
    public DuplicateAppointmentException() {
        super("Operation would result in duplicate appointments");
    }
}
