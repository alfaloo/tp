package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BROWN;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.ModelManager;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.appointment.AppointmentDateTime;
import seedu.address.model.appointment.exceptions.InvalidAppointmentException;

class AddAppointmentCommandTest {

    private ModelManager modelManager = new ModelManager();

    @Test
    void execute_validCommand_executesCommand() throws CommandException, ParseException {
        modelManager.addPerson(ALICE);
        modelManager.addPerson(BROWN);
        Appointment appt = new Appointment(BROWN.getNric(), ALICE.getNric(),
                new AppointmentDateTime("2024-09-01 11:02"));
        AddAppointmentCommand ad = new AddAppointmentCommand(appt);
        CommandResult commandResult = ad.execute(modelManager);
        assertTrue(modelManager.getFilteredAppointmentList().size() == 1);
    }

    @Test
    void execute_invalidCommand_missingPerson() throws CommandException, ParseException {
        Appointment appt = new Appointment(BROWN.getNric(), ALICE.getNric(),
                new AppointmentDateTime("2024-09-01 11:02"));
        AddAppointmentCommand ad = new AddAppointmentCommand(appt);
        assertThrows(CommandException.class, () -> ad.execute(modelManager));
    }

    @Test
    void execute_invalidCommand_throwsInvalidAppointmentException() throws ParseException {
        modelManager.addPerson(ALICE);
        modelManager.addPerson(BROWN);
        Appointment appt = new Appointment(ALICE.getNric(), BROWN.getNric(),
                new AppointmentDateTime("2024-09-01 11:02"));
        AddAppointmentCommand ad = new AddAppointmentCommand(appt);
        assertThrows(InvalidAppointmentException.class, () -> ad.execute(modelManager));
    }

    @Test
    void execute_invalidCommandAppointmentExists_throwsCommandException() throws ParseException {
        modelManager.addPerson(ALICE);
        modelManager.addPerson(BROWN);
        Appointment appt = new Appointment(BROWN.getNric(), ALICE.getNric(),
                new AppointmentDateTime("2024-09-01 11:02"));
        modelManager.addAppointment(appt);
        AddAppointmentCommand ad = new AddAppointmentCommand(appt);
        assertThrows(CommandException.class, () -> ad.execute(modelManager));
    }

    @Test
    void equals_sameCommandButDifferentObject_returnsTrue() throws ParseException {
        modelManager.addPerson(ALICE);
        modelManager.addPerson(BROWN);
        Appointment appt = new Appointment(BROWN.getNric(), ALICE.getNric(),
                new AppointmentDateTime("2024-09-01 11:02"));
        AddAppointmentCommand ad = new AddAppointmentCommand(appt);
        AddAppointmentCommand ad2 = new AddAppointmentCommand(appt);
        assertTrue(ad.equals(ad2));
    }

    @Test
    void equals_sameCommandSameObject_returnsTrue() throws ParseException {
        modelManager.addPerson(ALICE);
        modelManager.addPerson(BROWN);
        Appointment appt = new Appointment(BROWN.getNric(), ALICE.getNric(),
                new AppointmentDateTime("2024-09-01 11:02"));
        AddAppointmentCommand ad = new AddAppointmentCommand(appt);
        assertTrue(ad.equals(ad));
    }

    @Test
    void equals_differentClass_returnsFalse() throws ParseException {
        modelManager.addPerson(ALICE);
        modelManager.addPerson(BROWN);
        Appointment appt = new Appointment(BROWN.getNric(), ALICE.getNric(),
                new AppointmentDateTime("2024-09-01 11:02"));
        AddAppointmentCommand ad = new AddAppointmentCommand(appt);
        assertFalse(ad.equals("hi"));
    }

    @Test
    void toString_returnsValidString() throws ParseException {
        modelManager.addPerson(ALICE);
        modelManager.addPerson(BROWN);
        Appointment appt = new Appointment(BROWN.getNric(), ALICE.getNric(),
                new AppointmentDateTime("2024-09-01 11:02"));
        AddAppointmentCommand ad = new AddAppointmentCommand(appt);
        assertEquals(ad.toString(), "seedu.address.logic.commands.AddAppointmentCommand{toAdd="
                + appt.toString()
                + "}");
    }
}
