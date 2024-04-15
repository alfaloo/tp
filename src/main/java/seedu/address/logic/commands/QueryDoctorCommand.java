package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.logging.Level;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.DoctorContainsKeywordsPredicate;

/**
 * Queries and returns all doctors whose name matches the input string.
 * Keyword matching is case insensitive.
 * Query more than one name at a time is supported
 */
public class QueryDoctorCommand extends Command {

    public static final String COMMAND_WORD = "doctor";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all doctors whose name, "
            + "NRIC, DoB or phone number contains any of "
            + "the specified keywords (case-insensitive) and displays them as a list with index numbers.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " alice bob charlie";

    private static Logger logger = LogsCenter.getLogger(QueryDoctorCommand.class);

    private final DoctorContainsKeywordsPredicate predicate;

    public QueryDoctorCommand(DoctorContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        logger.log(Level.INFO, "Executing QueryDoctorCommand");
        model.updateFilteredPersonList(predicate);
        int numberOfDoctors = model.getFilteredPersonList().size();
        logger.log(Level.INFO, "Number of Doctor found: " + numberOfDoctors);
        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof QueryDoctorCommand)) {
            return false;
        }

        QueryDoctorCommand otherQueryDoctorCommand = (QueryDoctorCommand) other;
        return predicate.equals(otherQueryDoctorCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
