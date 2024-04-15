package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.logging.Level;
import java.util.logging.Logger;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.PatientContainsKeywordsPredicate;

/**
 * Queries and returns all patients whose name matches the input string.
 * Keyword matching is case insensitive.
 * Query more than one name at a time is supported
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

    public QueryPatientCommand(PatientContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
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

        // instanceof handles nulls
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
