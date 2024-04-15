package seedu.address.model.appointment;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.appointment.exceptions.DuplicateAppointmentException;
import seedu.address.model.person.Person;

/**
 * A list of appointments that enforces uniqueness between its elements and does not allow nulls.
 * An appointment is considered unique by comparing using {@code Appointment#isSameAppointment(Appointment)}.
 * As such, adding and updating of appointments uses Appointment#isSameAppointment(Appointment) for equality
 * so as to ensure that the Appointment being added or updated is
 * unique in terms of identity in the UniqueAppointmentList.
 * Supports a minimal set of list operations.
 *
 * @see Appointment#isSameAppointment(Appointment)
 */
public class UniqueAppointmentList implements Iterable<Appointment> {
    private final ObservableList<Appointment> internalList = FXCollections.observableArrayList();
    private final ObservableList<Appointment> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    /**
     * Returns true if the list contains an equivalent appointment as the given argument.
     * @param toCheck Appointment to check for in the list.
     * @return boolean indicating if appointment is contained.
     */
    public boolean contains(Appointment toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::isSameAppointment);
    }

    /**
     * Returns a list of appointments if the appointment contains the person. Checked via person's NRIC
     * @param person target person.
     * @return list of appointments.
     */
    public List<Appointment> contains(Person person) {
        requireNonNull(person);
        return internalList.stream()
                .filter(appointment -> appointment.appointmentContainsPerson(person))
                .collect(Collectors.toList());
    }

    /**
     * Adds an appointment to the list.
     * The appointment must not already exist in the list.
     *
     * @param toAdd Appointment toAdd
     */
    public void add(Appointment toAdd) {
        requireNonNull(toAdd);
        internalList.add(toAdd);
    }

    /**
     * Replaces the appointment {@code target} in the list with {@code editedAppointment}.
     * {@code target} must exist in the list.
     * The appointment details of {@code editedAppointment} must not be the same as another
     * existing appointment in the list.
     *
     * @param target the Appointment to replace.
     * @param editedAppointment the Appointment to edit.
     */
    public void setAppointment(Appointment target, Appointment editedAppointment) {
        requireAllNonNull(target, editedAppointment);

        int index = internalList.indexOf(target);

        internalList.set(index, editedAppointment);
    }

    /**
     * Removes the equivalent appointment from the list.
     * The appointment must exist in the list.
     *
     * @param toRemove the Appointment to remove from the list.
     */
    public void remove(Appointment toRemove) {
        requireNonNull(toRemove);
        assert internalList.contains(toRemove)
                : "Internal list should contain toRemove as check is done prior to method call";
        internalList.remove(toRemove);
    }


    /**
     * Replaces the contents of this list with {@code appointments}.
     * {@code appointments} must not contain duplicate appointments.
     *
     * @param appointments the list of Appointments to replace the current list with.
     */
    public void setAppointments(List<Appointment> appointments) throws DuplicateAppointmentException {
        requireAllNonNull(appointments);
        if (!appointmentsAreUnique(appointments)) {
            throw new DuplicateAppointmentException();
        }

        internalList.setAll(appointments);
    }

    /**
     * Replaces the contents of this list with {@code appointments}.
     * {@code appointments} must not contain duplicate appointments.
     *
     * This method does not throw DuplicateAppointmentException because it is only called when resetting the data.
     * @param appointments
     */
    public void setAppointmentsExistingBook(List<Appointment> appointments) {
        requireAllNonNull(appointments);
        boolean isAllAppointmentsUnique = appointmentsAreUnique(appointments);
        assert isAllAppointmentsUnique == true : "when this method is called appointments should be unique";
        internalList.setAll(appointments);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     *
     * @return ObservableList the backing list
     */
    public ObservableList<Appointment> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    @Override
    public Iterator<Appointment> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UniqueAppointmentList)) {
            return false;
        }

        UniqueAppointmentList otherUniqueAppointmentList = (UniqueAppointmentList) other;
        return internalList.equals(otherUniqueAppointmentList.internalList);
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    @Override
    public String toString() {
        return internalList.toString();
    }

    /**
     * Returns true if {@code appointments} contains only unique appointments.
     *
     * @param appointments the list of appointments to check for uniqueness.
     * @return boolean value indicating if appointments are unique.
     */
    private boolean appointmentsAreUnique(List<Appointment> appointments) {

        for (int i = 0; i < appointments.size() - 1; i++) {
            for (int j = i + 1; j < appointments.size(); j++) {
                if (appointments.get(i).isSameAppointment(appointments.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }
}
