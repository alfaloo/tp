package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.logging.Level;
import java.util.logging.Logger;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.PatientContainsKeywordsPredicate;

/**
 * Queries and returns all patients whose name, NRIC, DoB and phone number matches
 * or substring matches the input string.
 * Keyword matching is case-insensitive.
 * Query more than one name, nric, date of birth and phone number at a time is supported
 */
public class QueryPatientCommand extends Command {

    public static final String COMMAND_WORD = "patient";
    private static final Logger logger = Logger.getLogger(QueryPatientCommand.class.getName());

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all patients whose name, "
            + "NRIC, DoB or phone number contains any of "
            + "the specified keywords (case-insensitive) and displays them as a list with index numbers.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " alice bob ethan";

    private final PatientContainsKeywordsPredicate predicate;

    /**
     * Constructs a QueryPatientCommand with the given predicate.
     *
     * @param predicate The predicate to be used for querying patients.
     * @throws NullPointerException if the predicate is null.
     */
    public QueryPatientCommand(PatientContainsKeywordsPredicate predicate) {
        requireNonNull(predicate, "Predicate cannot be null in QueryPatientCommand constructor.");
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model, "Model cannot be null in execute method of QueryPatientCommand.");

        logger.log(Level.INFO, "Executing QueryPatientCommand");

        model.updateFilteredPersonList(predicate);
        int numberOfPatients = model.getFilteredPersonList().size();
        logger.log(Level.INFO, "Number of patients found: " + numberOfPatients);

        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, numberOfPatients));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof QueryPatientCommand)) {
            return false;
        }

        QueryPatientCommand otherQueryPatientCommand = (QueryPatientCommand) other;
        return predicate.equals(otherQueryPatientCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
