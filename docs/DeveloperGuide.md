---
layout: page
title: Developer Guide
---
* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

* {list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well}

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

<div markdown="span" class="alert alert-primary">

:bulb: **Tip:** The `.puml` files used to create diagrams in this document are in the `docs/diagrams` folder. Refer to the [_PlantUML Tutorial_ at se-edu/guides](https://se-education.org/guides/tutorials/plantUml.html) to learn how to create and edit diagrams.
</div>

### Architecture

<img src="images/ArchitectureDiagram.png" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of MediCLI and the various components.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `addpatient`.

<img src="images/ArchitectureSequenceDiagram.png" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<img src="images/ComponentManagers.png" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

![Structure of the UI Component](images/UiClassDiagram.png)

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `AppointmentListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/AY2324S2-CS2103T-T15-1/tp/blob/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/AY2324S2-CS2103T-T15-1/tp/blob/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` (`Doctor` or `Patient`) and `Appointment` objects residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/AY2324S2-CS2103T-T15-1/tp/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<img src="images/LogicClassDiagram.png" width="450"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

![Interactions Inside the Logic Component for the `delete 1` Command](images/DeleteSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</div>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `AddDoctorCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `AddDoctorCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to add a doctor).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<img src="images/ParserClasses.png" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddPatientCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddPatientCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddPatientCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<img src="images/ModelClassDiagram.png" width="450" />

The `Model` component,

* stores the address book data i.e., all `Person` derivative objects (which are contained in a `UniquePersonList` object) and
* all `Appointment` objects (which are contained in a `UniqueAppointmentList` object) stores the currently 'selected' `Person` objects (e.g., results of a search query, either a `Patient` or `Doctor` instance) and `Appointment` object (e.g results of an query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` and `ObservableList<Appointment>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<img src="images/StorageClassDiagram.png" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model` such as `Appointment` and `Patient`)

### Common classes

Classes used by multiple components are in the `seedu.addressbook.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### Add a `Patient` or `Doctor`
<i><b>Note</b>: Add `patient` and `doctor` has been grouped together as they are very similar in implementation.
This reduces repetition of information and increases clarity.</i>

Adds a new `Patient` or `Doctor` entry by indicating their `NRIC`, `Name`, `DoB`, and `Phone`.
This command is implemented through the `AddPatientCommand` for patient and `AddDoctorCommand` for doctor class which both extend the `Command` class.

The activity diagram below demonstrates the error handling process in more detail.

<img src="images/AddPersonActivityDiagram.png" width="800" />

This is the sequence of command execution for `execute` in `AddPatientCommand`, however `AddDoctorCommand` and `AddAppointmentCommand` follow similar design patterns within the execute command.
Add command execution sequence:
* Step 1. The `execute` method of the `AddPatientCommand` is called.
* Step 2. The method calls the `hasPerson` method of `model` to check if there are any duplicate patients and throws an exception if there is.
* Step 3. The `addPerson` method of `model` is then called and the control is passed back to the `execute` method.
* Step 4. A new `CommandResult` object with the success message is then created and returned by `execute`.

#### Design considerations:

**Aspect: How editing a Person works:**

* **Alternative 1 (current choice):** Removes the `originalPerson` and adds the `editedPerson`.
    * Pros: Retains the sorted order of Persons by `Name` in the person list.
    * Cons: May have performance issues in terms of time complexity since it requires 2 operations (`deletePerson()` and `addPerson`).

* **Alternative 2:** Directly update the fields in the `originalPerson`
    * Pros: Better performance, since this only requires searching through the person list once.
    * Cons: The order of person list will be lost, since `Name` of a `Person` may be edited.


### Edit `doctor` or `patient`

Edits a `doctor` or `patient` entry by indicating their `Index`.
This command is implemented through the `EditCommand` class which extends the `Command` class.

The activity diagram below demonstrates the error handling process in more detail.

<img src="images/EditPersonActivityDiagram.png" width="800" />

This is the sequence of command execution for `execute` in `EditCommand`, however `EditAppointmentCommand` follow a similar design pattern within the `execute` command.
* Step 1. The `execute` method of the `EditCommand` is called.
* Step 2. The method calls the `getFilteredPersonList` method of `model`.
* Step 3. The ``

Why is this implemented this way?
1. Making both `Doctor` and `Patient` class extend the `Person` class makes it easier to execute edit operations.
2. `Doctor` and `Patient` all exhibit similar qualities, and thus can inherit from the `Person` superclass.
3. Eliminates the need for separate edit commands for doctor and patient.
4. Since appointments are constructed with unique `Person` `Nric` fields, it does not make sense to have an appointment that does not have valid or outdated doctor or patient entries.
5. As such, the solution that is inbuilt to editing a `Person`, comes with the added functionality on the backend to augment all related `Appointment` entries as well.
6. This results in an updated `Appointments` panel, and saves the user from the hassle of needing to manually edit outdated `Appointment` entries one by one.

### Delete `doctor` or `patient`

Deletes a `doctor` or `patient` entry by indicating their `Index`.
This command is implemented through the `DeleteCommand` class which extends the `Command` class.

* Step 1. User enters an `delete` command.
* Step 2. The `AddressBookParser` will call `parseCommand` on the user's input string and return an instance of `deleteCommandParser`.
* Step 3. The `parse` command in `deleteCommandParser` calls `ParserUtil` to create instances of objects for each of the fields.
    * If there are any missing fields, a `CommandException` is thrown.
    * If input arguments does not match contraints for the fields, a `IllegalArgumentException` is thrown.
    * If the provided `index` is invalid, a `CommandException` is thrown.

The activity diagram below demonstrates this error handling process in more detail.

<img src="images/DeletePersonActivityDiagram.png" width="800" />

* Step 4. The `parse` command in `deleteCommandParser` return an instance of `deleteCommand`.
* Step 5. The `LogicManager` calls the `execute` method in `deleteCommand`.
* Step 6. The `execute` method in `deleteCommand` executes and calls `deletePerson` in model to remove doctor or patient from the system.
* Step 7. The `execute` method in `deleteCommand` also iterates through the `ObservableList<Appointments>` and retrieves all appointments that have the person to be deleted, and calls the `deleteAppointmentCommand` as well.
* Step 8. Success message gets printed onto the results display to notify user.

The sequence diagram below closely describes the interaction between the various components during the execution of the `DeleteCommand`.

<img src="images/DeletePersonSequenceDiagram.png" width="800" />


Why is this implemented this way?
1. Making both `Doctor` and `Patient` class extend the `Person` class makes it easier to execute delete operations.
2. `Doctor` and `Patient` all exhibit similar qualities, and thus can inherit from the `Person` superclass.
3. Eliminates the need for separate delete commands for doctor and patient.
4. Since appointments are constructed with unique `Person` `Nric` fields, it does not make sense to have an appointment that does not have valid doctor or patient entries.
5. As such, the solution that is inbuilt to deleting a `Person`, comes with the added functionality on the backend to delete all related `Appointment` entries as well.
6. This results in a cleaner `Appointments` panel, and saves the user from the hassle of needing to delete unwanted `Appointment` entries one by one.

### Understanding Appointments: A new entity type
MediCLI offers support for appointments, represented by the `Appointment` class on top of existing support for Doctors and Patients. 
At the crux of it, the `Appointment` class aims to reflect the essence of a medical appointment in real life, which involves a doctor and a patient and takes place at a specific time.

The class diagram below displays the structure of the `Appointment` class.
<img src="images/AppointmentClassDiagram.png" width="800" />

As visible, the `Appointment` class contains references to the following classes:
- **`Nric`**: a doctor's & a patient's NRIC number
- **`AppointmentDateTime`**: Date and time of the appointment
- **`AppointmentId`**: An ID automatically assigned to the appointment

The appointment class must have reference to exactly 2 `Nric` classes and 1 `AppointmentDateTime` and `AppointmentId`

Below is an object diagram demonstrating a possible appointment object.
<img src="images/AppointmentObjectDiagram.png" width="800" />

In the object diagram you see that two instances of the Nric class have been instantiated, one as `doctorNric`, and one as `patientNric`. This of course is along with the `appointmentDate` and `appointmentId` objects.

An instance of the `Appointment` class can only be created if the date & time of the appointment is >= the current date and time. This is enforced through the `isValidAppointmentDateTime` method in the `Appointment` class.

#### Context and thought process behind implementation:
Implementing Appointments naturally involved many design decisions, and here we have attempted to outline the thought process behind our current implementation:
* One key focus of the appointment implementation was to keep it as similar to the implementation of patients and doctors.
* The idea is that at the end of the day, the appointment is simply another type of entry being tracked.
* Nevertheless, it is natural that both in the UI and backend, we would want to differentiate the appointment entries from the patient/doctor entries to ensure that the system is more flexible and easy to expand on in the future.
* Hence, while similar in terms of the functionality, a lot of the infrastructure to handle appointments was built parallel to the one for persons.
* For instance, there is a separate `UniqueAppointmentList` class for storing and manipulating appointments that functions very similar to the equivalent list for persons.

#### Implementation and justification:
* Based on the thought process, the approach was to ensure MediCLI had the same way of handling `Appointments` and `Patients`/`Doctors`.
* The overall structure including how `Appointments` are stored, managed etc. is largely similar to support debugging and improve readability and comprehension.
* In other words, if you understand how MediCLI manages `Patients`/`Doctors`, you will also understand how it manages `Appointments`.
* Some differences are however inevitable and have been listed below:
  * `Appointment` objects include `doctorNric`, `patientNric` as attributes. A `Doctor` and `Patient` with the corresponding NRIC number must already exist before the `Appointment` was created.
  * Each `Appointment` object is also assigned a `AppointmentId`, and the ID itself is generated in by the `generateNewId` method of the `Idutil` class. While the `AppointmentID` does not serve a functional purpose at the moment and has not been fully fleshed-out, the current infrastructure serves as a framework on top of which further features that involve `AppointmentId` can be built.
    * Each appointmentId is structured as `aXXXXXXXX` where each `X` is a number. 
  * `Appointments` are stored in a separate list in the backend, called the `UniqueAppointmentList`, to allow for different operations and flexibility down the line.
  * In terms of the UI, `Appointments` appear in a separate column to ensure that the user is able to clearly distinguish between them.


#### Alternatives considered
* One key alternative we looked at was storing `Appointment` objects with `Patient` and `Doctor` objects as part of the same list i.e. `UniquePersonList`.
* This would mean changing the `Person` class to a different one such as `Entry` and have all three of `Patient` , `Doctor` and `Appointment` extend from the `Entry` class.
* We decided against this because we thought that it was not the most OOP friendly solution and would not allow for flexibility down the line
  * Eg: what if we wanted to add a feature that showed all `Appointments` for a set of `Patients` between a set of dates? Having them in the same list would be unintuitive and make the filtration and display quite cumbersome.
* Furthermore, it might get confusing for the user if everything was dumped into the same list for them to sieve through. Perhaps the user was only concerned with looking up `Patients` in which case the `Appointments` would simply be added clutter.
* The increased level of integration would also be problems for implementation and testing as existing functionality would have to be adapated exposing the system to more risks and potential for bugs. 
  * Eg: the classes would have to change from `Person` to `Entry` in a number of different places.


### Add a `Appointment`

Adds a new `Appointment` entry by indicating the `patientNric`, `doctorNric`, and an `appointmentDateTime`.
The values stored in each of these attributes are self-explanatory. A key thing to note is that a `Patients` /`Doctor` with the NRIC number must already exist in the records, and the date & time must be in the future.

This command is implemented through the `AddAppointmentCommand` class which extend the `Command` class.

* Step 1. User enters the keyword and attributes necessary for adding an appointment as indicated above.
* Step 2. The `AddressBookParser` will call `parseCommand` on the user's input string and return an instance of `addAppointmentCommandParser`.
* Step 3. The `parse` command in `addPatientCommandParser` calls `ParserUtil` to create instances of objects for each of the fields.
    * If there are any missing fields, a `CommandException` is thrown.
    * If input arguments does not match constraints for the fields, a `IllegalArgumentException` is thrown.
* Step 4. The `parse` command in `addAppointmentCommandParser` return an instance of `addAppointmentCommand`.
* Step 5. The `LogicManager` calls the `execute` method in `addAppointmentCommand`.
* Step 6. The `execute` method in `addAppointmentCommand` performs the following checks:
  * If a `Doctor`/`Patient` with the Nric in question not exist or the date & time is not >= current date & time, a `InvalidAppointmentException` is thrown
  * If an appointment between the `Doctor` and `Patient` (with corresponding NRICs) on the specified date & time already exists, then a `DuplicateAppointmentException` is thrown.
* Step 7: If both the checks above pass, the `execute` method executes and calls `addAppointment` in model to add the new appointment into the system.
* Step 8: Success message gets printed onto the results display to notify user.

The activity diagram below demonstrates this error handling process in more detail.
<img src="images/AddAppointmentActivityDiagram.png" width="800" />

### Edit `Appointment`
Edits an `Appointment` entry by indicating their `Index`.
This command is implemented through the `EditAppointmentCommand` class which extends the `Command` class.

* Step 1. User enters an `editappt` command.
* Step 2. The `AddressBookParser` will call `parseCommand` on the user's input string and return an instance of `editAppointmentCommandParser`.
* Step 3. The `parse` command in `editAppointmentCommandParser` calls `ParserUtil` to create instances of objects for each of the fields.
  * If there are any missing fields, a `CommandException` is thrown.
  * If input arguments does not match contraints for the fields, a `IllegalArgumentException` is thrown.
  * If the provided `index` is invalid, a `CommandException` is thrown.

The activity diagram below demonstrates this error handling process in more detail.

<img src="images/EditAppointmentActivityDiagram.png" width="800" />

* Step 4. The `parse` command in `editAppointmentCommandParser` returns an instance of `editAppointmentCommand`.
* Step 5. The `LogicManager` calls the `execute` method in `editAppointmentCommand`.
* Step 6. The `execute` method in `editAppointmentCommand` executes and calls `setAppointment` in model to set an updated appointment into the system.
* Step 7. Success message gets printed onto the results display to notify user.

The sequence diagram below closely describes the interaction between the various components during the execution of the `EditAppointmentCommand`.

<img src="images/EditAppointmentSequenceDiagram.png" width="800" />

Why is this implemented this way?
1. The `Appointment` class has very similar functionalities to that of the `Person` class, in which both classes deal with edit operations.
2. Furthermore on the UI, the `Appointment` column runs parallel to the `Person` column, as such, the behaviours (UX) of operating on the `Person` panel should have a similar feel and experience when dealing with `Appointment` objects.
3. This parallelism is also reflected in the backend code, and hence is very similar to how editing a `Person` is implemented - this is mostly seen through the naming conventions of the classes related to `EditPerson`, such as `EditAppointment`
4. This results in a more familiar experience for both users and developers alike as there is familiarity and some level of consistency when dealing with `Person` and `Appointment` classes.

Alternative implementation for consideration
1. Since both classes exhibit similarities in both code structure and behaviour, we might consider creating a generic class distinguished between `Person` and `Appointment` via enums to handle edits.
2. This will centralise the behaviours, and reduce the amount of code needed to perform the edit function.
3. A further extension is to do so with all other overlapping functionalities, such as `add` or `delete`, however we leave that possibility for future discussion and refinement.



### Delete `Appointment`
Deletes an `Appointment` entry by indicating their `Index`.
This command is implemented through the `DeleteAppointmentCommand` class which extend the `Command` class.

* Step 1. User enters an `deleteappt` command.
* Step 2. The `AddressBookParser` will call `parseCommand` on the user's input string and return an instance of `deleteAppointmentCommandParser`.
* Step 3. The `parse` command in `deleteAppointmentCommandParser` calls `ParserUtil` to create instances of objects for each of the fields.
    * If there are any missing fields, a `CommandException` is thrown.
    * If input arguments does not match contraints for the fields, a `IllegalArgumentException` is thrown.
    * If the provided `index` is invalid, a `CommandException` is thrown.

The activity diagram below demonstrates this error handling process in more detail.

<img src="images/DeleteAppointmentActivityDiagram.png" width="800" />

* Step 4. The `parse` command in `deleteAppointmentCommandParser` return an instance of `deleteAppointmentCommand`.
* Step 5. The `LogicManager` calls the `execute` method in `deleteAppointmentCommand`.
* Step 6. The `execute` method in `deleteAppointmentCommand` executes and calls `deleteAppointment` in model to remove appointment from the system.
* Step 7. Success message gets printed onto the results display to notify user.

The sequence diagram below closely describes the interaction between the various components during the execution of the `DeleteAppointmentCommand`.

<img src="images/DeleteAppointmentSequenceDiagram.png" width="800" />

Why is this implemented this way?
1. The `Appointment` class has very similar functionalities to that of the `Person` class, in which both classes deal with deletion operations.
2. Furthermore on the UI, the `Appointment` column runs parallel to the `Person` column, as such, the behaviours (UX) of operating on the `Person` panel should have a similar feel and experience when dealing with `Appointment` objects.
3. This parallelism is also reflected in the backend code, and hence is very similar to how deleting a `Person` is implemented - this is mostly seen through the naming conventions of the classes related to `DeletePerson`, such as `DeleteAppointment`
4. This results in a more familiar experience for both users and developers alike as there is familiarity and some level of consistency when dealing with `Person` and `Appointment` classes.

Alternative implementation for consideration
1. Since both classes exhibit similarities in both code structure and behaviour, we might consider creating a generic class distinguished between `Person` and `Appointment` via enums to handle deletions.
2. This will centralise the behaviours, and reduce the amount of code needed to perform the delete function.
3. A further extension is to do so with all other overlapping functionalities, such as `add` or `edit`, however we leave that possibility for future discussion and refinement.

### Find `Person`
Queries a `Person` entry, either `Doctor` or `Patient` by indicating their name or a substring of their name.
This command is implemented through the `FindCommand` class which extends the `Command` class.

The `find` command is able to take in multiple parameters (at least one), each of these parameters are read as strings, and act as separate queries. This is a feature of the `find` command, to have the capability of parsing both parameters as a short-hand feature.
Example, the command `find hans doe` is equivalent of returning the logical 'or' result of `find hans` and `find doe`.

* Step 1. User enters a `find` command.
* Step 2. The `AddressBookParser` will call `parseCommand` on the user's input string and return an instance of `findCommandParser`.
* Step 3. The `parse` command in `FindCommandParser` checks if at least one parameter is present.
    * If the field is missing, a `ParseException` is thrown, and the UI displays an error message.

The activity diagram below demonstrates this error handling process in more detail.

<img src="images/FindActivityDiagram.png" width="800" />

* Step 4. The `parse` command in `FindCommandParser` returns an instance of `FindCommand`.
* Step 5. The `LogicManager` calls the `execute` method in `FindCommand`.
* Step 6. The `execute` method in `FindCommand` executes and calls `updateFilteredPersonList` in model with the `NameContainsKeywordsPredicate` to get a filtered list of person entries of the entered keyword(s), both `patient` and `doctor` entries can be displayed.
* Step 7. A Success message gets printed onto the results display to notify user and the list of matching results is produced.

The sequence diagram below closely describes the interaction between the various components during the execution of the `FindCommand`.

<img src="images/FindPersonSequenceDiagram.png" width="800" />

Alternative implementations considered
1. The following sections describes the behaviour of querying `doctor` and `patient` entries by separate commands by all of the entry's fields, both following a very similar logic to how the `find` command is implemented. We might consider using flags to be more precise with our searches, (e.g a -doctor or -patient flag to indicate we wish to search for only `doctor` and `patient` entries respectively) so as to avoid the need to create additional commands. However, we felt that this approach overloaded the `find` method too much, and overcomplicated the `find` command's usage.
2. Even if the `find` command was to be overloaded with flags, we foresaw that the creation of distinct commands to fit the flags parsed by the `find` command was unavoidable. As such, it was prudent to start with the implementation of the distinct commands first (as described in the following sections, each tied to a specific command), and leave the overloading of the `find` command as a later increment.


### Query `doctor`
Queries a `doctor` entry by indicating the exact string, or substring of any of a `doctor`'s fields - name, nric, phone number and date of birth (as displayed on the UI. e.g 30 January 2023).
This command is implemented through the `QueryDoctorCommand` class which extends the `Command` class.

Similar to the `find` command, the `doctor` query command is able to take in multiple parameters (at least one), each of these parameters are read as strings, and act as separate queries, with the result returned being the logical 'or' result of applying a `doctor` query to each parameter separately.

* Step 1. User enters a `doctor` command.
* Step 2. The `AddressBookParser` will call `parseCommand` on the user's input string and return an instance of `QueryDoctorCommandParser`.
* Step 3. The `parse` command in `QueryDoctorCommandParser` checks if at least one parameter is present.
  * If the field is missing, a `ParseException` is thrown, and the UI displays an error message.

The activity diagram below demonstrates this error handling process in more detail.

<img src="images/QueryDoctorActivityDiagram.png" width="800" />

* Step 4. The `parse` command in `QueryDoctorCommandParser` returns an instance of `QueryDoctorCommand`.
* Step 5. The `LogicManager` calls the `execute` method in `QueryDoctorCommand`.
* Step 6. The `execute` method in `QueryDoctorCommand` executes and calls `updateFilteredPersonList` in model with the `DoctorContainsKeywordsPredicate` to get a filtered list of only `Doctor` entries of the entered keywords(s).
* Step 7. Success message gets printed onto the results display to notify user and the list of matching results is produced.


Why is this implemented this way?
1. The backend execution of the `doctor` command and the `find` command are very similar, however the choice to separate the two query commands is justified due to the expansion of fields in the `doctor` query, conceptually making them distinct commands.
2. Furthermore, there is an additional check to check if an entry is of type `doctor` that is not present in the `find` command.


### Query `patient`
Queries a `patient` entry by indicating the exact string, or substring of any of a `patient`'s fields - name, nric, phone number and date of birth (as displayed on the UI. e.g 30 January 2023).
This command is implemented through the `QueryPatientCommand` class which extends the `Command` class.

Similar to the `find` command, the `patient` query command is able to take in multiple parameters (at least one), each of these parameters are read as strings, and act as separate queries, with the result returned being the logical 'or' result of applying a `patient` query to each parameter separately.

* Step 1. User enters a `patient` command.
* Step 2. The `AddressBookParser` will call `parseCommand` on the user's input string and return an instance of `QueryPatientCommandParser`.
* Step 3. The `parse` command in `QueryPatientCommandParser` checks if at least one parameter is present.
  * If the field is missing, a `ParseException` is thrown, and the UI displays an error message.

The activity diagram below demonstrates this error handling process in more detail.

<img src="images/QueryPatientActivityDiagram.png" width="800" />

* Step 4. The `parse` command in `QueryPatientCommandParser` returns an instance of `QueryPatientCommand`.
* Step 5. The `LogicManager` calls the `execute` method in `QueryPatientCommand`.
* Step 6. The `execute` method in `QueryPatientCommand` executes and calls `updateFilteredPersonList` in model with the `PatientContainsKeywordsPredicate` to get a filtered list of only `Patient` entries of the entered keywords(s).
* Step 7. Success message gets printed onto the results display to notify user and the list of matching results is produced.


Why is this implemented this way?
1. The backend execution of the `patient` command and the `find` command are very similar, however the choice to separate the two query commands is justified due to the expansion of fields in the `patient` query, conceptually making them distinct commands.
2. Furthermore, there is an additional check to check if an entry is of type `patient` that is not present in the `find` command.


### Query `apptfordoctor`
Queries an `appointment` entry that has the associated `doctor`'s `Nric`, by indicating the exact `Nric` of the doctor as the search parameter.
This command is implemented through the `apptfordoctor` class which extends the `Command` class.

The `apptfordoctor` command takes in multiple parameters (at least one), and each of these parameters are read as strings (not case-sensitive, i.e S1234567A is equivalent to s1234567a), and returns the logical 'or' result of applying the `apptfordoctor` command to each parameter separately. <u>**Note that no errors will not be thrown if the inputted `Nric`(s) are not of the appropriate form** </u>(i.e Begins with one of S, T, G, F, or M, followed by 7 numerical digits, then ended by an alphabetical letter), but rather the expected return result is that no queries will be found.

* Step 1. User enters an `QueryDoctorAppointment` command.
* Step 2. The `AddressBookParser` will call `parseCommand` on the user's input string and return an instance of `QueryDoctorAppointmentCommandParser`.
* Step 3. The `parse` command in `QueryDoctorAppointmentCommandParser` checks if at least one parameter is present.
  * If the field is missing, a `ParseException` is thrown, and the UI displays an error message.

The activity diagram below demonstrates this error handling process in more detail.

<img src="images/QueryDoctorAppointmentActivityDiagram.png" width="800" />

* Step 4. The `parse` command in `QueryDoctorAppointmentCommandParser` returns an instance of `QueryDoctorAppointmentCommand`.
* Step 5. The `LogicManager` calls the `execute` method in `QueryDoctorAppointmentCommand`.
* Step 6. The `execute` method in `QueryDoctorAppointmentCommand` executes and calls `updateFilteredAppointmentList` in model with the `AppointmentContainsDoctorPredicate` to get a filtered list of appointment entries with the entered keyword(s), only those `appointment`(s) that have the associated `doctor`'s `Nric` entries are be displayed.
* Step 7. Success message gets printed onto the results display to notify user and the list of matching results is produced.


Why is this implemented this way?
1. This command closely resembles the `find` command, but can be seen as a stricter version as the results queried do not include substring searches. Therefore, it is justified to separate this command from the `find` command as two distinct commands with distinct `commandParsers`.
2. The rationale behind excluding substring searches for `appointment`(s) is that if a hospital clerk is searching for a specific `doctor`'s scheduled `appointment`(s), the hospital clerk already has the `doctor`'s unique `Nric` and hence including substring querying is irrelevant.


### Query `apptforpatient`

Queries an `appointment` entry that has the associated `patient`'s `Nric`, by indicating the exact `Nric` of the patient as the search parameter.
This command is implemented through the `apptforpatient` class which extends the `Command` class.

The `apptforpatient` command takes in multiple parameters (at least one), and each of these parameters are read as strings (not case-sensitive, i.e S1234567A is equivalent to s1234567a), and returns the logical 'or' result of applying the `apptforpatient` command to each parameter separately. <u>**Note that no errors will not be thrown if the inputted `Nric`(s) are not of the appropriate form** </u>(i.e Begins with one of S, T, G, F, or M, followed by 7 numerical digits, then ended by an alphabetical letter), but rather the expected return result is that no queries will be found.

* Step 1. User enters an `QueryPatientAppointment` command.
* Step 2. The `AddressBookParser` will call `parseCommand` on the user's input string and return an instance of `QueryPatientAppointmentCommandParser`.
* Step 3. The `parse` command in `QueryPatientAppointmentCommandParser` checks if at least one parameter is present.
  * If the field is missing, a `ParseException` is thrown, and the UI displays an error message.

The activity diagram below demonstrates this error handling process in more detail.

<img src="images/QueryPatientAppointmentActivityDiagram.png" width="800" />

* Step 4. The `parse` command in `QueryPatientAppointmentCommandParser` returns an instance of `QueryPatientAppointmentCommand`.
* Step 5. The `LogicManager` calls the `execute` method in `QueryPatientAppointmentCommand`.
* Step 6. The `execute` method in `QueryPatientAppointmentCommand` executes and calls `updateFilteredAppointmentList` in model with the `AppointmentContainsPatientPredicate` to get a filtered list of appointment entries with the entered keyword(s), only those `appointment`(s) that have the associated `patient`'s `Nric` entries are be displayed.
* Step 7. Success message gets printed onto the results display to notify user and the list of matching results is produced.


Why is this implemented this way?
1. This command closely resembles the `find` command, but can be seen as a stricter version as the results queried do not include substring searches. Therefore, it is justified to separate this command from the `find` command as two distinct commands with distinct `commandParsers`.
2. The rationale behind excluding substring searches for `appointment`(s) is that if a hospital clerk is searching for a specific `patient`'s scheduled `appointment`(s), the hospital clerk already has the `patient`'s unique `Nric` and hence including substring querying is irrelevant.


### \[Proposed\] Undo/redo feature


#### Proposed Implementation


The proposed undo/redo mechanism is facilitated by `VersionedMediCLI`. It extends `MediCLI` with an undo/redo history, stored internally as an `mediCLIStateList` and `currentStatePointer`. Additionally, it implements the following operations:


* `VersionedMediCLI#commit()` — Saves the current MediCLI state in its history.

* `VersionedMediCLI#undo()` — Restores the previous MediCLI state from its history.

* `VersionedMediCLI#redo()` — Restores a previously undone MediCLI state from its history.


These operations are exposed in the `Model` interface as `Model#commitMediCLI()`, `Model#undoMediCLI()` and `Model#redoMediCLI()` respectively.


Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.


Step 1. The user launches the  application for the first time. The `VersionedMediCLI` will be initialized with the initial MediCLI state, and the `currentStatePointer` pointing to that single MediCLI state.


![UndoRedoState0](images/UndoRedoState0.png)


Step 2. The user executes `delete 5` command to delete the 5th person in the MediCLI. The `delete` command calls `Model#commitMediCLI()`, causing the modified state of the MediCLI after the `delete 5` command executes to be saved in the `mediCLIStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.


![UndoRedoState1](images/UndoRedoState1.png)


Step 3. The user executes `addpatient i/S1234567A n/John Doe d/2003-01-30 p/98765432` to add a new person. The `add` command also calls `Model#commitMediCLI()`, causing another modified MediCLI state to be saved into the `mediCLIStateList`.


![UndoRedoState2](images/UndoRedoState2.png)


<div markdown="span" class="alert alert-info">:information_source: **Note:** If a command fails its execution, it will not call `Model#commitMediCLI()`, so the MediCLI state will not be saved into the `mediCLIStateList`.


</div>


Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoMediCLI()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous MediCLI state, and restores the MediCLI to that state.


![UndoRedoState3](images/UndoRedoState3.png)


<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index 0, pointing to the initial MediCLI state, then there are no previous MediCLI states to restore. The `undo` command uses `Model#canUndoMediCLI()` to check if this is the case. If so, it will return an error to the user rather

than attempting to perform the undo.


</div>


The following sequence diagram shows how an undo operation goes through the `Logic` component:


![UndoSequenceDiagram](images/UndoSequenceDiagram-Logic.png)


<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.


</div>


Similarly, how an undo operation goes through the `Model` component is shown below:


![UndoSequenceDiagram](images/UndoSequenceDiagram-Model.png)


The `redo` command does the opposite — it calls `Model#redoMediCLI()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the MediCLI to that state.


<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index `mediCLIStateList.size() - 1`, pointing to the latest MediCLI state, then there are no undone MediCLI states to restore. The `redo` command uses `Model#canRedoMediCLI()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.


</div>


Step 5. The user then decides to execute the command `list`. Commands that do not modify the MediCLI, such as `list`, will usually not call `Model#commitMediCLI()`, `Model#undoMediCLI()` or `Model#redoMediCLI()`. Thus, the `mediCLIStateList` remains unchanged.


![UndoRedoState4](images/UndoRedoState4.png)


Step 6. The user executes `clear`, which calls `Model#commitMediCLI()`. Since the `currentStatePointer` is not pointing at the end of the `mediCLIStateList`, all MediCLI states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `addpatient i/S1234567A n/John Doe d/2003-01-30 p/98765432` command. This is the behavior that most modern desktop applications follow.


![UndoRedoState5](images/UndoRedoState5.png)


The following activity diagram summarizes what happens when a user executes a new command:


<img src="images/CommitActivityDiagram.png" width="250" />


#### Design considerations:


**Aspect: How undo & redo executes:**


* **Alternative 1 (current choice):** Saves the entire MediCLI.

  * Pros: Easy to implement.

  * Cons: May have performance issues in terms of memory usage.


* **Alternative 2:** Individual command knows how to undo/redo by

  itself.

  * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).

  * Cons: We must ensure that the implementation of each individual command are correct.


_{more aspects and alternatives to be added}_


### \[Proposed\] Data archiving


_{Explain here how the data archiving feature will be implemented}_

## Planned Enhancements

The MediCLI development team (consisting of 5 members) acknowledges the presense of known feature flaws in our system.
Thus, we have planned the following 10 enhancements to be added in the near future.
Please find them organised into their respective categories.

### Appointment Functionality Enhancements

1. Adding an end time to appointments

Currently, the MediCLI system only stores the date and start time of an appointment.
However, we recognise that in a fast-paced environment like a hospital, it'd be beneficial to also be able to indicate an end time for appointments.
This is so that the doctor can be safely booked by another patient without worrying about potential clashes in appointment timings.
* <b>Updated Command Format</b> - `addappt sdt/STARTDATETIME [edt/ENDDATETIME] dn/DOCTORNRIC pn/PATIENTNRIC`
* <b>Example Input</b> - `addappt sdt/2024-05-20 10:00 edt/2024-05-20 11:00 dn/S1234567A pn/S1234567B`

2. More robust appointment timing validation.

Currently, the MediCLI system allows two appointments with the same doctor/patient and date-time to be simultaneously stored in the system.
However, it is clearly impossible for a patient or doctor to attend two different appointments at the same time.
Thus, we plan to implement a more robust appointment validation system to ensure that appointments with clashing or unrealistic timings can not be entered.

3. Marking old appointments as completed.

Even though the MediCLI system does not allow appointments to be made in the future, it nonetheless retains entry of completed appointments.
However, there is currently no visual distinction between future, completed, and missed appointments. This can be rather confusing for the hospital clerks.
Thus, we plan to add a label (just like the patient/doctor labels) in the top right corner of each appointment card to help better distinguish them.
* <b>New Command Format</b> - `markappt index s/STATUS` (`STATUS` can be any one of {`completed`, `missed`})
* <b>Example Input</b> - `markappt 1 s/completed`
* <b>Example Input</b> - `markappt 2 s/missed`

### Parameter Checking Enhancements

4. Accommodate names with symbols and/or special characters.

The name parameter is currently restricted to just alphabetical characters and white-space.
However, we recognise the existence of names that contain symbols and other special characters.
In the future, we plan to implement a more accommodating constraint that allows UTF-8 characters instead.
This means that names of other languages will be accepted as well.

5. Allow foreign patients/doctors to be added to the system.

The current constraints for the NRIC and phone number parameters reflect the Singaporean format.
However, we recognise that for foreign users, this can be rather limiting.
Thus, in the future, we plan on introducing more refined parameter checking that allows international NRIC and phone number formats.

6. Ensure each person being added to the system is unique.

While the current MediCLI system already checks to ensure every person added is unique, it is only done by comparing the NRIC of the person.
However, this should not be the only checking condition. Two entries with the same name, date of birth, and/or phone number should also be flagged as non-unique.
Thus, we will devise a more holistic assessment criterion to ensure no duplicates are allowed.

### User Interface Enhancements

7. Refine the user interface when the window size is minimised.

The current MediCLI system is not particularly flexible when it comes to window sizing.
Users on smaller screens may encounter the issue of scrolling being disabled or labels being truncated if a long name is entered.
In the future, we plan to make the UI more adaptive and friendly to smaller screens.

8. Standardise displayed information.

For certain fields, the MediCLI system simply displays the text exactly as entered by the user.
However, this can introduce inconsistencies in capitalisation (especially with the NRIC field) when displayed in the user interface.
We plan on standardising these fields by automatically capitalising the users' input.

### Feature Enhancements

9. More advanced search options

Currently, the `find`, `patient`, and `doctor` commands return all entries whose details contain any of the given keywords.
However, this implementation is not particularly effective if the user would like to search for a person that matches all the provided keywords exactly
(e.g. when searching for a person by full name). In the future, we plan to add more advanced search options to allow for easy querying of information.
* <b>Updated Command Format</b> - `find t/TYPE KEYWORD`, `patient t/TYPE KEYWORD`, `doctor t/TYPE KEYWORD` (`TYPE` can be any one of {`full`, `partial`}).
* <b>Example Input</b> - `find t/full John Doe`
* <b>Example Input</b> - `doctor t/partial Smith Li`

10. More detailed error messages.

Some of the current error messages are not the most informative
(e.g. If two patient NRICs are provided when creating an appointment, the system only prompts `This appointment is invalid due to invalid inputs.`).
To decrease the learning curve for our system, we plan to replace all ambiguous error messages with more informative versions, e.g. `Please make sure the NRIC provided belongs to a person of the correct type as indicated by the prefix.`.


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* hospital clerks who deal with hospital related registration/administrative/management tasks
* has a need to manage a significant number of client details (patients/doctors/appointments)
* deals with many real time live updates, some being time-critical
* prefer desktop apps over other types
* can type fast and accurately
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps

**Value proposition**: manages the hospital database (querying/updating/creating/deleting) faster than a typical mouse/GUI driven database management app


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority  | As a …​                                    | I want to …​                               | So that I can…​                                                   |
|-----------|--------------------------------------------|--------------------------------------------|-------------------------------------------------------------------|
| `* * *`   | hospital clerk                             | add patients                               | handle incoming patients when handling emergency call-ins         |
| `* * *`   | hospital clerk                             | delete patients                            | remove old patients to prevent clogging of system                 |
| `* * *`   | hospital clerk                             | add doctors                                | register new doctors as they get hired                            |
| `* * *`   | hospital clerk                             | delete doctors                             | remove previous doctors that have left the hospital               |
| `* * *`   | hospital clerk                             | create appointments                        | arrange a meeting time between a doctor and a patient             |
| `* * *`   | hospital clerk                             | delete appointments                        | remove a meeting time if either party becomes unavailable         |
| `* * *`   | hospital clerk                             | query patient by name                      | retrieve their relevant information                               |
| `* * *`   | hospital clerk                             | query doctor by name                       | retrieve their relevant information                               |
| `* *`     | hospital clerk                             | query appointment by patient               | look up what appointments a patient has to attend                 |
| `* *`     | hospital clerk                             | query appointment by doctor                | look up what appointments a doctor has to service                 |
| `*`       | hospital clerk                             | query patient by other fields              | retrieve patient information through other fields if they call-in |
| `*`       | hospital clerk                             | find available timings to book appointment | schedule a time that suits both the patient and doctor            |


### Use cases

(For all use cases below, the **System** is the `MediCLI` and the **Actor** is the `hospital clerk`, unless specified otherwise)

(Note: For all use cases, if you enter the command format wrongly, MediCLI will show an error message and return to step 1.)

**Use case: Add a patient**

**MSS**

1.  Hospital clerk needs to add a patient
2.  Hospital clerk enters patient data 
3.  MediCLI adds the patient into database

Use case ends.

**Extensions**

* 2a. The entered patient data is not in the correct format
  * 2a1. MediCLI shows an error message.
  
    Use case resumes at step 1.

* 2b. The entered patient is already in the database
  * 2b1. MediCLI shows an error message.
  
     Use case resumes at step 1.

**Use case: Delete a patient**


**MSS**

1.  Hospital clerk requests to list persons
2.  MediCLI shows a list of persons
3.  Hospital clerk requests to delete a specific patient in the list
4.  MediCLI deletes the patient

Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case resumes at step 1.

* 3a. The given patient index is invalid.
  * 3a1. MediCLI shows an error message.

    Use case resumes at step 1.

**Use case: Create an appointment**

**MSS**

1.  Hospital clerk needs to create appointment between doctor and patient
2.  Hospital clerk enters doctor and patient details
3.  MediCLI creates the appointment

    Use case ends.


**Extensions**

* 2a. The entered doctor or patient detail is invalid.
  * 2a1. MediCLI will show an error message.
    
    Use case resumes at step 1.

* 2b. The entered appointment date is invalid
  * 2b1. MediCLI will show an error message.
      
     Use case resumes at step 1.

**Use case: Delete an appointment**

**MSS**

1.  Hospital clerk needs to delete appointment between doctor and patient
2.  Hospital clerk enters appointment index
3.  MediCLI deletes the appointment

    Use case ends.

**Extensions**

* 2a. The entered appointment index is invalid.
  * 2a1. MediCLI shows an error message.

    Use case resumes at step 1.

**Use case: Query patient by name**

**MSS**

1.  Hospital clerk needs to search for patient
2.  Hospital clerk enters patient name
3.  MediCLI lists patients with supplied name

Use case ends.

**Extensions**

* 3a. The list is empty

  Use case resumes at step 1.

**Use case: Query appointments by patient**

**MSS**

1.  Hospital clerk needs to search for appointment by patient
2.  Hospital clerk enters patient name
3.  MediCLI lists relevant appointments

Use case ends.

**Extensions**

* 3a. The list is empty

  Use case resumes at step 1.

**Use case: Query appointments by doctor**

**MSS**

1.  Hospital clerk needs to search for appointment by doctor
2.  Hospital clerk enters doctor name
3.  MediCLI lists relevant appointments

Use case ends.

**Extensions**

* 3a. The list is empty

    Use case resumes at step 1.

### Non-Functional Requirements

1.  Should work on any _mainstream OS_ as long as it has Java `11` or above installed.
2.  Should be able to hold up to 1000 medical staff without a noticeable sluggishness in performance for typical usage.
3.  A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4.  MediCLI should be easy to integrate with existing medical database systems so that staff can immediately switch to the new app.
5.  Comprehensive documentation should be provided, including user guides, command references, and troubleshooting resources.
6. MediCLI should not need an internet connection to run.
7. The GUI for MediCLI should be well organised, purpose oriented and easy to understand for users of any knowledge level.
8. MediCLI should handle the majority of common user errors and give the users suggestions to mitigate these errors.
9. MediCLI does not support concurrent usage between multiple users.
10. MediCLI does not support languages other than English.

### Glossary

* **Private contact detail**: A contact detail that is not meant to be shared with others.
* **CLI**: Command Line Interface, a way of interacting with a computer program where the user enters commands into a terminal or command prompt.
* **GUI**: Graphical User Interface, a way of interacting with a computer program using graphical elements such as windows, buttons, and menus.
* **JSON**: JSON: JavaScript Object Notation, a lightweight data interchange format used to store and exchange data.
* **API**: Application Programming Interface, a set of rules and protocols for building and interacting with software applications.
* **UI**: User Interface, the visual part of a computer program that allows users to interact with it.
* **XML**: Extensible Markup Language, a markup language that defines rules for encoding documents in a format that is both human-readable and machine-readable.
* **MSS**: Main Success Scenario, the primary flow of events in a use case that leads to the desired outcome.

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for Manual Testing**

This section provides guidance for testers to navigate through the user-testable features of MediCLI. It includes important test inputs along with the expected test results that can be copied and pasted into the app for testing purposes.

<div markdown="span" class="alert alert-info">:information_source: **INFO**: These instructions only provide a starting point for testers to work on. Testers are expected to do more *exploratory* testing.</div>


### Launch and shutdown
#### Initial launch
Steps:
1. Download the jar file and copy into an empty folder.
2. Double-click the jar file.

Expected Outcome:
* Shows the GUI with a set of sample contacts.
* The window size may not be optimum.

#### Saving window preferences
Steps:
1. Resize the window to an optimum size.
2. Move the window to a different location.
3. Close the window.
4. Re-launch the app by double-clicking the jar file.<br>

Expected Outcome:
* The most recent window size and location is retained.
                                                         
#### Closing MediCLI
Steps:
1. Execute the `exit` command, or simply close the window.

Expected Outcome:
* MediCLI closes without any errors.


### Person Related Commands
#### Adding a Patient : `addpatient`

Steps:
1. Execute the `addpatient` command with valid NRIC, name, DOB, and phone number.
2. Verify that the patient is successfully added to the system.
3. Try adding a patient with an existing NRIC and verify that the command fails.
4. Attempt to add a patient with invalid or missing fields and confirm appropriate error handling.

Valid Inputs:
* Valid NRIC, name, DOB, and phone number.
* Example: `addpatient i/S1234567A n/David Li d/2000-01-01 p/98765432`

Expected Outcome:
* Patient is successfully added to the system.

Invalid Inputs:
* Missing or invalid fields (e.g. invalid NRIC format, missing name).
* Example: `addpatient i/1234567A n/ d/2000-01-01 p/12345678`

Expected Error:
* Command fails with the 'Invalid command format' error message indicating the required command format.

#### Adding a Doctor : `adddoctor`
Steps:
1. Use the `adddoctor` command with valid NRIC, name, DOB, and phone number.
2. Verify that the doctor is added to the system.
3. Test adding a doctor with an existing NRIC and check if the command fails.
4. Test adding a doctor with invalid or missing fields and observe error handling.

Valid Inputs:
* Valid NRIC, name, DOB, and phone number.
* Example: `adddoctor i/S1234567A n/Dr. Jane Smith d/1975-05-15 p/98765432`

Expected Outcome:
* Doctor is successfully added to the system.

Invalid Inputs:
* Missing or invalid fields (e.g. invalid phone number).
* Example: `adddoctor i/S1234567A n/Dr. Jane Smith d/1975-05-15 p/1234567`

Expected Error:
* Command fails with the 'Invalid command format' error message indicating the required command format.

#### Editing a Person : `edit`
Steps:
1. Execute the `edit` command with the index of an existing person.
2. Update one or more fields (NRIC, name, DOB, or phone number) and confirm changes.
3. Test editing without changing any values and ensure it's handled correctly.
4. Try editing with an invalid index and verify error handling.

Valid Inputs:
* Index of an existing person and valid fields to update.
* Example: `edit 1 n/John Smith`

Expected Outcome:
* Person's name has been successfully updated to John Smith.

Invalid Inputs:
* Invalid index or missing fields.
* Example: `edit 0 n/John Smith`
* Example: `edit 1`

Expected Error:
* Command fails with the appropriate error message indicating the invalid index or missing fields.

#### Finding Persons by Name : `find`
Steps:
1. Use the `find` command with keywords to search for both patients and doctors.
2. Ensure the command returns expected results based on the provided keywords.
3. Try different combinations of keywords and verify the search results.

Valid Inputs:
* Keywords matching existing persons' names.
* Example: `find John`

Expected Outcome:
* List of persons matching the keywords is displayed.

Invalid Inputs:
* No matching keywords or invalid syntax.
* Example: `find 123`

Expected Error:
* Results display indicates '0 persons listed', and the Persons panel is empty.
                
#### Finding Persons by all Fields : `patient`, `doctor`
Steps:
1. Use the `patient` command with keywords to search for patients only.
2. Ensure the command returns expected results based on the provided keywords.
3. Similarly, use the `doctor` command to search for doctors.
4. Try different combinations of keywords and verify the search results.
                                                                                   
Valid Inputs:
* Keywords exactly matching or substring matching existing persons' nric, name, date of birth, or phone number.
* Example: `patient S1234`
* Example: `patient Doe`
* Example: `doctor 30 Jan`
* Example: `doctor 98765432`
                                                                                   
Expected Outcome:
* List of patients or doctors exactly matching or substring matching the keywords is displayed.

Invalid Inputs:
* No matching keywords or invalid syntax.
* Example: `patient`
* Example: `doctor @`

Expected Error:
* Command fails with the appropriate error message indicating the required command format.
* Or the results display indicates '0 persons listed', and the Persons panel is empty.

#### Deleting a Person (delete)
Steps:
1. Use the `list` command to display a list of persons.
2. Execute the `delete` command with the index of a person to delete them.
3. Confirm that the person is removed from MediCLI.
4. Verify that associated appointments are also deleted recursively.
5. Test deleting a person with an invalid index and observe error handling.

Valid Inputs:
 * Index of an existing person.
 * Example: `delete 1`

Expected Outcome:
 * Person is successfully deleted from the system.

Invalid Inputs:
* Invalid index.
* Example: `delete 0`

Expected Error:
* Command fails with appropriate error message indicating the required command format and parameter requirements.

### Appointment Related Commands
#### Adding an Appointment : `addappt`
Steps:
1. Execute the `addappt` command with valid datetime, doctor's NRIC, and patient's NRIC.
2. Ensure the appointment is successfully added to the system.
3. Test adding an appointment with invalid datetime or NRICs and verify error handling.

Valid Inputs:
* Valid datetime, doctor's NRIC, and patient's NRIC.
* Example: `addappt ad/2024-08-11 12:00 dn/S1234567A pn/S7654321B`

Expected Outcome:
* Appointment is successfully added to the system.

Invalid Inputs:
* Missing or invalid datetime, doctor's NRIC, or patient's NRIC.
* Example: `addappt ad/2024-08-11 dn/S1234567A pn/S7654321B`

Expected Error:
* Command fails with appropriate error message indicating the required command format.

#### Editing an Appointment : `editappt`
Steps:
1. Use the editappt command with the index of an existing appointment.
2. Update the datetime of the appointment and confirm changes.
3. Test editing without changing any values and ensure it's handled correctly.
4. Try editing with an invalid index and verify error handling.

Valid Inputs:
* Index of an existing appointment and valid datetime to update.
* Example: `editappt 1 ad/2024-08-12 14:00`

Expected Outcome:
* Appointment datetime is successfully updated.

Invalid Inputs:
* Invalid index or missing datetime.
* Example: `editappt 0 ad/2024-08-12 14:00`

Expected Error:
* Command fails with appropriate error message indicating the required command format.

#### Querying Appointments by Patient's NRIC : `apptforpatient`
Steps:
1. Execute the `apptforpatient` command with a patient's exact NRIC.
2. Verify that all appointments involving the specified patient are listed.
3. Test with different patient NRICs and confirm the results.

Valid Inputs:
* Patient's NRIC.
* Example: `apptforpatient S7654321B`

Expected Outcome:
* List of appointments involving the specified patient is displayed.

Invalid Inputs:
* No matching patient NRIC, missing NRIC or invalid NRIC.
* Example: `apptforpatient S1234567A`
* Example: `apptforpatient`
* Example: `apptforpatient S123456`

Expected Error:
* Command fails with appropriate error message indicating the required command format.
* Or the results display indicates '0 appointments listed', and the appointments panel is empty.

#### Querying Appointments by Doctor's NRIC : `apptfordoctor`
Steps:
1. Use the `apptfordoctor` command with a doctor's NRIC.
2. Ensure that all appointments involving the specified doctor are listed.
3. Test with different doctor NRICs and verify the results.

Valid Inputs:
* Doctor's NRIC.
* Example: `apptfordoctor S1234567A`

Expected Outcome:
* List of appointments involving the specified doctor is displayed.

Invalid Inputs:
* No matching patient NRIC, missing NRIC or invalid NRIC.
* Example: `apptfordoctor S1234567A`
* Example: `apptfordoctor`
* Example: `apptfordoctor S123456`

Expected Error:
* Command fails with appropriate error message indicating the required command format.
* Or the results display indicates '0 appointments listed', and the appointments panel is empty.

#### Deleting an Appointment : `deleteappt`
Steps:
1. Use the `list` command to display a list of appointments.
2. Execute the `deleteappt` command with the index of an appointment to delete it.
3. Confirm that the appointment is removed from the system.
4. Test deleting an appointment with an invalid index and observe error handling.

Valid Inputs:
* Index of an existing appointment.
* Example: `deleteappt 1`

Expected Outcome:
* Appointment is successfully deleted from the system.

Invalid Inputs:
* Invalid index.
* Example: `deleteappt 0`

Expected Error:
* Command fails with appropriate error message indicating the required command format.

### Miscellaneous Commands
#### Viewing Help : `help`
Steps:
1. Execute the `help` command and ensure the help message is displayed.
2. Verify that the 'Help' pop up is displayed, and click the 'Copy URL' button.
3. Verify that pasting the URL in your browser leads you to MediCLI's updated User-Guide page.

Valid Inputs:
* None

Expected Outcome:
* 'Help' pop up is displayed successfully.

Invalid Inputs:
* None

Expected Error:
* None

#### Listing All Persons and Appointments : `list`
Steps:
1. Use the `list` command to display all persons and appointments.
2. Confirm that the Persons Panel and the Appointments Panel includes all existing patients, doctors, and appointments existing in MediCLI.

Valid Inputs:
* None

Expected Outcome:
* All persons and appointments are displayed.

Invalid Inputs:
* None

Expected Error:
* None

#### Clearing All Entries : `clear`
Steps:
1. Execute the `clear` command and confirm if all data is wiped from MediClI.
2. Ensure there is no remaining data after executing this command.
3. Verify that there is no confirmation prompt and data deletion is immediate.

Valid Inputs:
* None

Expected Outcome:
* All data is wiped from MediCLI.
* Results display indicates that 'MediCLI's storage has been cleared!'

Invalid Inputs:
* None

Expected Error:
* None

#### Exiting the Program : `exit`
Steps:
1. Execute the `exit` command and ensure the program exits gracefully.

Valid Inputs:
* None

Expected Outcome:
* Program exits without errors.

Invalid Inputs:
* None

Expected Error:
* None

-------------------------------------------------
## **Appendix: Effort**

### Difficulty level:
If AB3 was at a difficulty level of 5/10, `MediCLI` reached was at a level of 8/10. This is most significantly because of the following reasons:
- Expansion of `Person` entity type to include `Patient` and `Doctor` entities.
- Inclusion of a completely new `Appointment` entity type.

The expansion into `Patient` and `Doctor` required us to expand the suite of commands to account for them, as well as modify the backend methods to account for the changes.
However, the `Appointment` class required us to build a completely new parallel infrastructure from UI to the commands to the storage and significantly increased the challenge and difficulty of the project.

### Challenges faced:
We faced multiple challenges in the project, most significant of which are highlighted below:
- Developing the `Appointments` feature: As a completely new entity type, we had to build this from the ground up while also integrating it the best way we could with the existing AB3 codebase, which was a big challenge. Even simple things like the `addAppointment` command required a large amount of implementation effort. Beyond just the functional code, writing tests for this and making sure that a sufficient portion was tested using automated tests was quite difficult as we had no existing code that we could build off-of, or modify.
- Achieving sufficient test coverage - as we have a fairly large amount of functional LOC written, a natural consequence is that we had to write a lot of test code to ensure that they were sufficiently tested. This was a challenge, especially in the beginning when we were just familiarising ourselves with automated tests.
- UI enhancements: As `javafx` was a completely new framework for everyone in our team, we found it rather difficult to actually make the UI enhancements that we wanted. Particularly we had to make a completely new `card` for appointments and we made changes to the general look and feel to make it more suitable for a hospital, which was a significant step away from the AB3 UI, hence the difficulty. 
- Finally, another difficulty we faced was actually considering the edge-cases and various scenarios that may arise from interactions between the different entities we were managing:
  - Let's say you have a `Doctor` with multiple `Appointments`, what do you do if the user deletes the `Doctor` from MediCLI? We decided that the most appropriate approach would be to recursively delete all `Appointments` associated with the doctor.
  - Similarly, what if you want to add an `Appointment` but the `Patient` involved doesn't exist in the records? We had to implement checks to prevent this.
  - Many other similar issues resulting from the interactions of different entities were noted and we found it quite challenging to actually identify and resolve all of them so they didn't become a bug or feature flaw down the line.

### Effort required:
In the previous sections we have already elaborated a little on the challenges and difficulties and talked about the efforts we put into addressing them. As such, for this section we have focused on quantifying the overall project effort.

On the whole, the project took us the entire duration that was offered, with most of us writing upwards of 1000 lines of functional code. The `Appointments` feature was naturally the biggest cause of this and it led to a number of issues that were quite effort-intensive to resolve. 
On a weekly basis, each of us invested about 8 hours on average in writing code, writing automated tests, documenting and manually testing the feature. 

For team meetings, we would meet up twice a week, one time at the start to distribute work for the week and discuss deliverables, and one time in the end to wrap up the milestone/work for the week and submit the deliverables. Altogether we spent about 3 hours in meetings per week. 

### Achievements:
Some achievements we are particulary proud of are:
- Robustness of `Appointments` feature: We tried to consider all the edge cases and problematic aspects of this and preemptively prevent them to make sure the user has a seamless experience.
- UI improvements: We are quite happy with the way `Appointments` and `Patients`/`Doctors` are integrated into the same window to allow for easy visualisation with minimal use of the mouse. We are also quite proud of the new clean and minimalist interface which we think reflects a hospital setting quite well.
- Variety of commands: We also are happy with the suite of commands that we offer to users. We focused on the most high-impact commands and ensured that our product offers all CRUD capabilities such that it may actually be used in a functional setting for a small clinic/hospital.
- Comprehensive testing: While we do not claim to be bug-free, we did a comprehensive amount of testing including testing our product ourselves manually, through automated tests, and even asking other teams to perform their own UAT on our product to identify pain-points/bugs/feature flaws (that we went on to address).
