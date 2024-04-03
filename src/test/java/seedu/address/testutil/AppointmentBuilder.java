package seedu.address.testutil;

import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.appointment.AppointmentDate;
import seedu.address.model.person.Doctor;
import seedu.address.model.person.Nric;
import seedu.address.model.person.Patient;

/**
 * A utility class to help with building Person objects.
 */
public class AppointmentBuilder {

    public static final String DEFAULT_APPTDATE = "2024-08-30";

    private Nric doctorNric;
    private Nric patientNric;
    private AppointmentDate appointmentDate;


    /**
     * Creates a {@code PersonBuilder} with the default details.
     */
    public AppointmentBuilder() {
        Doctor d = new DoctorBuilder().build();
        Patient p = new PatientBuilder().build();

        this.doctorNric = d.getNric();
        this.patientNric = p.getNric();
        this.appointmentDate = new AppointmentDate(DEFAULT_APPTDATE);
    }

    /**
     * Initializes the AppointmentBuilder with the data of {@code personToCopy}.
     */
    public AppointmentBuilder(Appointment apptToCopy) {
        this.appointmentDate = apptToCopy.getAppointmentDate();
        this.doctorNric = apptToCopy.getDoctorNric();
        this.patientNric = apptToCopy.getPatientNric();
    }

    /**
     * Sets the {@code date} of the {@code Appointment} that we are building.
     */
    public AppointmentBuilder withDate(String dateString) {
        this.appointmentDate = new AppointmentDate(dateString);
        return this;
    }

    /**
     * Sets the {@code doctorNric} of the {@code Appointment} that we are building.
     */
    public AppointmentBuilder withDoctor(Doctor doctor) {
        this.doctorNric = doctor.getNric();
        return this;
    }

    /**
     * Sets the {@code patientNric} of the {@code Appointment} that we are building.
     */
    public AppointmentBuilder withPatient(Patient patient) {
        this.patientNric = patient.getNric();
        return this;
    }

    /**
     * Builds appointment with specified attributes.
     * If parameters are incorrect, or invalid, returns null.
     * @return Appointment with specified attributes
     */
    public Appointment build() {
        try {
            return new Appointment(doctorNric, patientNric, appointmentDate);
        } catch (ParseException e) {
            return null;
        }
    }

}
