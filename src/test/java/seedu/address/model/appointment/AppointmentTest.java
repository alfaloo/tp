package seedu.address.model.appointment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Nric;
import seedu.address.model.person.Person;
import seedu.address.testutil.DoctorBuilder;
import seedu.address.testutil.PatientBuilder;

class AppointmentTest {

    @Test
    void getDoctorNric() throws ParseException {
        Nric doctorNric = new Nric("S1234567A");
        Appointment appointment = new Appointment(doctorNric, new Nric("T1234567A"),
                new AppointmentDateTime(LocalDateTime.now()), false);
        assertEquals(doctorNric, appointment.getDoctorNric());
    }

    @Test
    void getPatientNric() throws ParseException {
        Nric patientNric = new Nric("T1234567A");
        Appointment appointment = new Appointment(new Nric("S1234567A"), patientNric,
                new AppointmentDateTime(LocalDateTime.now()), false);
        assertEquals(patientNric, appointment.getPatientNric());
    }

    @Test
    void getAppointmentDate() throws ParseException {
        AppointmentDateTime appointmentDateTime = new AppointmentDateTime(LocalDateTime.now());
        Appointment appointment =
                new Appointment(new Nric("S1234567A"), new Nric("T1234567A"), appointmentDateTime, false);
        assertEquals(appointmentDateTime, appointment.getAppointmentDateTime());
    }

    @Test
    void isValidAppointmentDateTime_validDate_returnsTrue() throws ParseException {
        AppointmentDateTime futureDateTime = new AppointmentDateTime(LocalDateTime.now().plusDays(1));
        Appointment appointment =
                new Appointment(new Nric("S1234567A"), new Nric("T1234567A"), futureDateTime, false);

        assertTrue(appointment.isValidAppointmentDateTime(futureDateTime));
    }

    @Test
    void isValidAppointmentDateTime_pastDate_returnsFalse() {
        AppointmentDateTime pastDateTime = new AppointmentDateTime(LocalDateTime.now().minusDays(1));
        // Use assertThrows to check if IllegalArgumentException is thrown
        assertThrows(ParseException.class, () -> {
            new Appointment(new Nric("S1234567A"), new Nric("T1234567A"), pastDateTime, false);
        });
    }

    @Test
    void isSameAppointment() throws ParseException {
        Nric doctorNric = new Nric("S1234567A");
        Nric patientNric = new Nric("T1234567A");
        AppointmentDateTime appointmentDateTime = new AppointmentDateTime(LocalDateTime.now());
        Appointment appointment1 = new Appointment(doctorNric, patientNric, appointmentDateTime, false);
        Appointment appointment2 = new Appointment(doctorNric, patientNric, appointmentDateTime, false);
        assertTrue(appointment1.isSameAppointment(appointment2));
    }

    @Test
    void appointmentContainsPerson() throws ParseException {
        Nric doctorNric = new Nric("S1234567A");
        Nric patientNric = new Nric("T1234567A");
        AppointmentDateTime appointmentDateTime = new AppointmentDateTime(LocalDateTime.now());
        Appointment appointment = new Appointment(doctorNric, patientNric, appointmentDateTime, false);

        // False Doctor and Patient
        Person doctor = new DoctorBuilder().withNric("S7654321A").build();
        Person patient = new PatientBuilder().withNric("T7654321A").build();
        assertFalse(appointment.appointmentContainsPerson(doctor));
        assertFalse(appointment.appointmentContainsPerson(patient));

        // True Doctor and Patient
        doctor = new DoctorBuilder().withNric("S1234567A").build();
        patient = new PatientBuilder().withNric("T1234567A").build();
        assertTrue(appointment.appointmentContainsPerson(doctor));
        assertTrue(appointment.appointmentContainsPerson(patient));
    }

    @Test
    void testEquals() throws ParseException {
        Nric doctorNric = new Nric("S1234567A");
        Nric patientNric = new Nric("T1234567A");
        AppointmentDateTime appointmentDateTime = new AppointmentDateTime(LocalDateTime.now());
        Appointment appointment1 = new Appointment(doctorNric, patientNric, appointmentDateTime, false);
        Appointment appointment2 = new Appointment(doctorNric, patientNric, appointmentDateTime, false);
        assertEquals(appointment1, appointment2);
    }

    @Test
    void testToString() throws ParseException {
        Nric doctorNric = new Nric("S1234567A");
        Nric patientNric = new Nric("T1234567A");
        AppointmentDateTime appointmentDateTime = new AppointmentDateTime(LocalDateTime.now());
        Appointment appointment = new Appointment(doctorNric, patientNric, appointmentDateTime, false);
        String expectedString = "seedu.address.model.appointment.Appointment{Date=" + appointmentDateTime + ", Doctor="
                + doctorNric + ", Patient=" + patientNric + "}";
        assertEquals(expectedString, appointment.toString());
    }
}
