package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalAppointments.APPOINTMENT_1;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.appointment.exceptions.DuplicateAppointmentException;
import seedu.address.model.person.Nric;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.testutil.PatientBuilder;

public class AddressBookTest {

    private final AddressBook addressBook = new AddressBook();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), addressBook.getPersonList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.resetData(null));
    }

    @Test
    public void resetData_withValidReadOnlyAddressBook_replacesData() {
        AddressBook newData = getTypicalAddressBook();
        addressBook.resetData(newData);
        assertEquals(newData, addressBook);
    }

    @Test
    public void resetData_withDuplicatePersons_throwsDuplicatePersonException() {
        // Two persons with the same identity fields
        Person editedAlice = new PatientBuilder(ALICE).withPhone(VALID_PHONE_BOB).build();
        List<Person> newPersons = Arrays.asList(ALICE, editedAlice);
        AddressBookStub newData = new AddressBookStub(newPersons);

        assertThrows(DuplicatePersonException.class, () -> addressBook.resetData(newData));
    }

    @Test
    public void hasPerson_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.hasPerson(null));
    }

    @Test
    public void hasPerson_personNotInAddressBook_returnsFalse() {
        assertFalse(addressBook.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personInAddressBook_returnsTrue() {
        addressBook.addPerson(ALICE);
        assertTrue(addressBook.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personWithSameIdentityFieldsInAddressBook_returnsTrue() {
        addressBook.addPerson(ALICE);
        Person editedAlice = new PatientBuilder(ALICE).withPhone(VALID_PHONE_BOB).build();
        assertTrue(addressBook.hasPerson(editedAlice));
    }

    @Test
    public void getPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> addressBook.getPersonList().remove(0));
    }

    @Test
    public void toStringMethod() {
        String expected = AddressBook.class.getCanonicalName() + "{persons=" + addressBook.getPersonList() + "}";
        assertEquals(expected, addressBook.toString());
    }

    /**
     * A stub ReadOnlyAddressBook whose persons list can violate interface constraints.
     */
    private static class AddressBookStub implements ReadOnlyAddressBook {
        private final ObservableList<Person> persons = FXCollections.observableArrayList();
        private final ObservableList<Appointment> appointments = FXCollections.observableArrayList();

        AddressBookStub(Collection<Person> persons) {
            this.persons.setAll(persons);
        }

        @Override
        public ObservableList<Person> getPersonList() {
            return persons;
        }

        @Override
        public ObservableList<Appointment> getAppointmentList() {
            return appointments;
        }


    }

    @Test
    public void getPersonByNric_bookHasPerson_returnsPerson() {
        addressBook.addPerson(ALICE);
        assertEquals(ALICE, addressBook.getPersonByNric(ALICE.getNric()));
    }

    @Test
    public void getPersonByNric_bookDoesNotHavePerson_throwsPersonNotFoundException() {
        addressBook.addPerson(ALICE);
        assertThrows(PersonNotFoundException.class, () -> addressBook.getPersonByNric(new Nric("T1234567G")));
    }

    @Test
    public void getPersonByNric_nricInputIsNull_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.getPersonByNric(null));
    }

    @Test
    public void getPersonByNric_addressbookIsEmpty_throwsPersonNotFoundException() {
        assertThrows(PersonNotFoundException.class, () -> addressBook.getPersonByNric(ALICE.getNric()));
    }

    @Test
    public void hasPersonNric_validNric_returnsTrue() {
        addressBook.addPerson(ALICE);
        assertTrue(addressBook.hasPersonNric(ALICE.getNric().toString()));
    }

    @Test
    public void setAppointments_validSet_setsAppointments() throws DuplicateAppointmentException {
        List<Appointment> appts = new ArrayList<>();
        appts.add(APPOINTMENT_1);
        addressBook.setAppointments(appts);
        assertTrue(addressBook.hasAppointment(APPOINTMENT_1));
    }

    @Test
    public void equalsMethod() {
        // same object
        assertTrue(addressBook.equals(addressBook));
        // different class
        assertFalse(addressBook.equals(APPOINTMENT_1));
    }

}
