package seedu.address.model.appointment;

import static seedu.address.commons.util.AppUtil.checkArgument;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.appointment.exceptions.InvalidAppointmentException;
import seedu.address.model.person.Nric;
import seedu.address.model.person.Person;

/**
 * Appointment class that describes an appointment in MediCLI.
 */
public class Appointment {

    private static final Logger logger = LogsCenter.getLogger(Appointment.class);

    private static final String MESSAGE_CONSTRAINTS_INVALID_DATE =
            "Appointment should be made with a date-time today onwards";

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
     * @param isInitialised a boolean value indication whether this was initialised by the json file
     * @throws ParseException
     */
    public Appointment(Nric doctorNric, Nric patientNric, AppointmentDateTime appointmentDateTime,
                       Boolean isInitialised) throws ParseException {
        requireAllNonNull(doctorNric, patientNric, appointmentDateTime);
        logger.log(Level.INFO, "Going to create new appointment instance");

        if (!isInitialised) {
            try {
                checkArgument(isValidAppointmentDateTime(appointmentDateTime), MESSAGE_CONSTRAINTS_INVALID_DATE);
            } catch (IllegalArgumentException e) {
                logger.log(Level.INFO, "Appointment parameter check failed");
                throw new ParseException(e.getMessage());
            }
        }

        this.doctorNric = doctorNric;
        this.patientNric = patientNric;
        this.appointmentDateTime = appointmentDateTime;
    }

    /**
     * Checks if appointment is valid by comparing appointment date against current date.
     * A valid new appointment can only be in the future, not the past.
     *
     * @param appointmentDate Date to check validity of
     * @return boolean if appointment is valid or not
     */
    public boolean isValidAppointmentDateTime(AppointmentDateTime appointmentDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        AppointmentDateTime currentDateTime = new AppointmentDateTime(LocalDateTime.now().format(formatter));
        return appointmentDate.compareTo(currentDateTime) > -1;
    }

    /**
     * Gets Nric of doctor in charge.
     * @return Nric of doctor in charge.
     */
    public Nric getDoctorNric() {
        return doctorNric;
    }

    /**
     * Sets the doctor nric to input nric.
     *
     * @param nric the new doctor nric.
     * @throws InvalidAppointmentException if nric is null.
     */
    public void setDoctorNric(Nric nric) throws InvalidAppointmentException {
        // If multiplicity is violated, throw exception. Appointment cannot have null doctor nric.
        if (nric == null) {
            throw new InvalidAppointmentException();
        }
        this.doctorNric = nric;
    }

    /**
     * Gets nric of the patient of the appointment.
     * @return nric of patient of the appointment.
     */
    public Nric getPatientNric() {
        return patientNric;
    }

    /**
     * Sets the patient nric to input nric.
     *
     * @param nric the new patient nric.
     * @throws InvalidAppointmentException if nric is null.
     */
    public void setPatientNric(Nric nric) throws InvalidAppointmentException {
        // If multiplicity is violated, throw exception. Appointment cannot have null patient nric.
        if (nric == null) {
            throw new InvalidAppointmentException();
        }
        this.patientNric = nric;
    }

    /**
     * Gets the ID of the appointment.
     *
     * @return AppointmentId of the appointment.
     */
    public AppointmentId getAppointmentId() {
        return this.appointmentId;
    }

    /**
     * Gets date & time of the appointment.
     *
     * @return date & time of the appointment.
     */
    public AppointmentDateTime getAppointmentDateTime() {
        return appointmentDateTime;
    }

    /**
     * Checks if appointment is same as input one by comparing persons involved and date.
     *
     * @param appt input appointment to compare current appointment against.
     * @return boolean indicating if appointments are the same or not.
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
                .toString();
    }
}
