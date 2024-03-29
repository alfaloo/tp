//package seedu.address.testutil;
//
//import java.util.HashSet;
//import java.util.Set;
//
//import seedu.address.model.person.Type;
//import seedu.address.model.person.Nric;
//import seedu.address.model.person.Name;
//import seedu.address.model.person.DoB;
//import seedu.address.model.person.Phone;
//import seedu.address.model.person.Person;
//import seedu.address.model.util.SampleDataUtil;
//
///**
// * A utility class to help with building Person objects.
// */
//public class PatientBuilder {
//
//    public static final Type DEFAULT_TYPE = "Patient";
//    public static final String DEFAULT_NAME = "Amy Bee";
//    public static final String DEFAULT_PHONE = "85355255";
//    public static final String DEFAULT_NRIC = "T0294883F";
//    public static final String DEFAULT_DOB = "2002-03-03";
//
//    private final Type type;
//    private final Nric nric;
//    private final Name name;
//    private final DoB dob;
//    private final Phone phone;
//
//
//    /**
//     * Creates a {@code PersonBuilder} with the default details.
//     */
//    public PersonBuilder() {
//        type = new Type.PATIENT;
//        nric = new Nric(DEFAULT_NRIC);
//        name = new Name(DEFAULT_NAME);
//        dob = new DoB(DEFAULT_DOB)
//        phone = new Phone(DEFAULT_PHONE);
//    }
//
//    /**
//     * Initializes the PersonBuilder with the data of {@code personToCopy}.
//     */
//    public PersonBuilder(Person personToCopy) {
//        type = personToCopy.getType();
//        nric = personToCopy.getNric();
//        name = personToCopy.getName();
//        dob = personToCopy.getDoB();
//        phone = personToCopy.getPhone();
//    }
//
//    /**
//     * Sets the {@code Name} of the {@code Person} that we are building.
//     */
//    public PatientBuilder withName(String name) {
//        this.name = new Name(name);
//        return this;
//    }
//
//    /**
//     * Sets the {@code Phone} of the {@code Person} that we are building.
//     */
//    public PersonBuilder withPhone(String phone) {
//        this.phone = new Phone(phone);
//        return this;
//    }
//
//    public Person build() {
//        return new Patient(type, nric, name, dob, phone);
//    }
//
//}
