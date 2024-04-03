package seedu.address.model.appointment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

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
                new AppointmentDate(LocalDate.now()));
        assertEquals(doctorNric, appointment.getDoctorNric());
    }

    @Test
    void getPatientNric() throws ParseException {
        Nric patientNric = new Nric("T1234567A");
        Appointment appointment = new Appointment(new Nric("S1234567A"), patientNric,
                new AppointmentDate(LocalDate.now()));
        assertEquals(patientNric, appointment.getPatientNric());
    }

    @Test
    void getAppointmentId() {
        AppointmentId appointmentId = new AppointmentId();
        Appointment appointment = new Appointment(new Nric("S1234567A"), new Nric("T1234567A"),
                new AppointmentDate(LocalDate.now()), appointmentId);
        assertEquals(appointmentId, appointment.getAppointmentId());
    }

    @Test
    void getAppointmentDate() throws ParseException {
        AppointmentDate appointmentDate = new AppointmentDate(LocalDate.now());
        Appointment appointment = new Appointment(new Nric("S1234567A"), new Nric("T1234567A"), appointmentDate);
        assertEquals(appointmentDate, appointment.getAppointmentDate());
    }

    @Test
    void isValidAppointment_validDate_returnsTrue() throws ParseException {
        AppointmentDate futureDate = new AppointmentDate(LocalDate.now().plusDays(1));
        Appointment appointment = new Appointment(new Nric("S1234567A"), new Nric("T1234567A"), futureDate);

        assertTrue(appointment.isValidAppointment(futureDate));
    }

    @Test
    void isValidAppointment_pastDate_returnsFalse() {
        AppointmentDate pastDate = new AppointmentDate(LocalDate.now().minusDays(1));
        // Use assertThrows to check if IllegalArgumentException is thrown
        assertThrows(ParseException.class, () -> {
            new Appointment(new Nric("S1234567A"), new Nric("T1234567A"), pastDate);
        });
    }

    @Test
    void isSameAppointment() throws ParseException {
        Nric doctorNric = new Nric("S1234567A");
        Nric patientNric = new Nric("T1234567A");
        AppointmentDate appointmentDate = new AppointmentDate(LocalDate.now());
        Appointment appointment1 = new Appointment(doctorNric, patientNric, appointmentDate);
        Appointment appointment2 = new Appointment(doctorNric, patientNric, appointmentDate);
        assertTrue(appointment1.isSameAppointment(appointment2));
    }

    @Test
    void appointmentContainsPerson() throws ParseException {
        Nric doctorNric = new Nric("S1234567A");
        Nric patientNric = new Nric("T1234567A");
        AppointmentDate appointmentDate = new AppointmentDate(LocalDate.now());
        Appointment appointment = new Appointment(doctorNric, patientNric, appointmentDate);

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
        AppointmentDate appointmentDate = new AppointmentDate(LocalDate.now());
        Appointment appointment1 = new Appointment(doctorNric, patientNric, appointmentDate);
        Appointment appointment2 = new Appointment(doctorNric, patientNric, appointmentDate);
        assertEquals(appointment1, appointment2);
    }

    @Test
    void testToString() {
        Nric doctorNric = new Nric("S1234567A");
        Nric patientNric = new Nric("T1234567A");
        AppointmentDate appointmentDate = new AppointmentDate(LocalDate.now());
        AppointmentId appointmentId = new AppointmentId();
        Appointment appointment = new Appointment(doctorNric, patientNric, appointmentDate, appointmentId);
        String expectedString = "seedu.address.model.appointment.Appointment{Date=" + appointmentDate + ", Doctor="
                + doctorNric + ", Patient=" + patientNric + ", Id=" + appointmentId + "}";
        assertEquals(expectedString, appointment.toString());
    }
}
