package seedu.address.model.appointment;

import static seedu.address.commons.util.AppUtil.checkArgument;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Nric;
import seedu.address.model.person.Person;

/**
 * Appointment class that describes an appointment
 */
public class Appointment {

    private static final String MESSAGE_CONSTRAINTS_INVALID_DATE =
            "Appointment should be made with a date today onwards";

    // The doctor in charge of the appointment
    private Nric doctorNric;

    // The patient benefiting from the appointment
    private Nric patientNric;

    // The date of the appointment
    private final AppointmentDateTime appointmentDateTime;

    // Message to outputs in case constraints are not met

    private final AppointmentId appointmentId;

    /**
     * Constructs a new appointment instance
     * @param doctorNric doctor in charge
     * @param patientNric patient of the appointment
     * @param appointmentDateTime date of the appointment
     */
    public Appointment(
            Nric doctorNric, Nric patientNric, AppointmentDateTime appointmentDateTime) throws ParseException {
        requireAllNonNull(doctorNric, patientNric, appointmentDateTime);

        try {
            checkArgument(isValidAppointment(appointmentDateTime), MESSAGE_CONSTRAINTS_INVALID_DATE);
        } catch (IllegalArgumentException e) {
            throw new ParseException(e.getMessage());
        }

        this.doctorNric = doctorNric;
        this.patientNric = patientNric;
        this.appointmentDateTime = appointmentDateTime;
        this.appointmentId = new AppointmentId();
    }

    /**
     * Constructs a new appointment instance
     * @param doctorNric doctor in charge
     * @param patientNric patient of the appointment
     * @param appointmentDateTime date and time of the appointment
     * @param appointmentId id of the appointment
     */
    public Appointment(Nric doctorNric, Nric patientNric, AppointmentDateTime appointmentDateTime,
                       AppointmentId appointmentId) {
        requireAllNonNull(doctorNric, patientNric, appointmentDateTime);
        checkArgument(isValidAppointment(appointmentDateTime), MESSAGE_CONSTRAINTS_INVALID_DATE);
        this.doctorNric = doctorNric;
        this.patientNric = patientNric;
        this.appointmentDateTime = appointmentDateTime;
        this.appointmentId = appointmentId;
    }

    /**
     * constructs a new appointment instance
     * @param doctorNric doctor in charge
     * @param patientNric patient of the appointment
     * @param appointmentDateTime dateTime of the appointment
     * @param isInitialised a boolean value indication whether this was initialised by the json file
     * @throws ParseException
     */
    public Appointment(
            Nric doctorNric, Nric patientNric,
            AppointmentDateTime appointmentDateTime, AppointmentId appointmentId, Boolean isInitialised) throws ParseException {
        requireAllNonNull(doctorNric, patientNric, appointmentDateTime);
        this.doctorNric = doctorNric;
        this.patientNric = patientNric;
        this.appointmentDateTime = appointmentDateTime;
        this.appointmentId = new AppointmentId();
    }
    /**
     * Checks if appointment is valid by comparing appointment date against current date.
     * A valid new appointment can only be in the future, not the past.
     *
     * @param appointmentDate Date to check validity of
     * @return boolean if appointment is valid or not
     */
    public boolean isValidAppointment(AppointmentDateTime appointmentDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        AppointmentDateTime currentDateTime = new AppointmentDateTime(LocalDateTime.now().format(formatter));
        return appointmentDate.compareTo(currentDateTime) > -1;
    }

    /**
     * Gets doctor in charge
     * @return Doctor in charge
     */
    public Nric getDoctorNric() {
        return doctorNric;
    }

    public void setDoctorNric(Nric nric) {
        this.doctorNric = nric;
    }

    /**
     * Gets patient of the appointment
     * @return patient of the appointment
     */
    public Nric getPatientNric() {
        return patientNric;
    }

    public void setPatientNric(Nric nric) {
        this.patientNric = nric;
    }

    public AppointmentId getAppointmentId() {
        return this.appointmentId;
    }

    /**
     * Gets date of the appointment
     * @return date of the appointment
     */
    public AppointmentDateTime getAppointmentDateTime() {
        return appointmentDateTime;
    }

    /**
     * Checks if appointment is same as input one by comparing persons involved and date.
     * @param appt input appointment to compare current appointment against
     * @return boolean indicating if appointments are the same or not
     */
    public boolean isSameAppointment(Appointment appt) {
        if (appt == this) {
            return true;
        }

        return appt != null
                && appt.getDoctorNric().equals(this.getDoctorNric())
                && appt.getPatientNric().equals(this.getPatientNric())
                && appt.getAppointmentDateTime().equals(this.getAppointmentDateTime());
    }

    /**
     * Checks if the given {@code Person} is associated with this appointment either as a doctor or a patient.
     *
     * @param person The {@code Person} to check if associated with this appointment.
     * @return {@code true} if the person's NRIC matches either the doctor's NRIC or the patient's NRIC,
     *         {@code false} otherwise.
     */
    public boolean appointmentContainsPerson(Person person) {
        return person.getNric().equals(this.doctorNric)
                || person.getNric().equals(this.patientNric);
    }

    @Override
    public boolean equals(Object appt) {
        if (appt == this) {
            return true;
        }

        if (!(appt instanceof Appointment)) {
            return false;
        }

        Appointment appointment = (Appointment) appt;

        return appt != null
                && appointment.getDoctorNric().equals(this.getDoctorNric())
                && appointment.getPatientNric().equals(this.getPatientNric())
                && appointment.getAppointmentDateTime().equals(this.getAppointmentDateTime());
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("Date", getAppointmentDateTime())
                .add("Doctor", getDoctorNric())
                .add("Patient", getPatientNric())
                .add("Id", getAppointmentId())
                .toString();
    }
}
