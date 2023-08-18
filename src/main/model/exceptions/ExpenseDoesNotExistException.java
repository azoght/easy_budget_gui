package model.exceptions;

// An exception thrown when an expense that does not exist in the spending tracker is being accessed
public class ExpenseDoesNotExistException extends Exception {

    /*
     * EFFECTS: returns the message displayed when this exception is caught
     */
    @Override
    public String getMessage() {
        return "The expense does not exist!";
    }
}
