package model.exceptions;

// An exception thrown when a double field for a budget, budget item, or expense is zero or negative
public class ZeroOrLessException extends Exception {

    /*
     * EFFECTS: returns the message displayed when this exception is caught
     */
    @Override
    public String getMessage() {
        return "Invalid: amount is less than or equal to 0!";
    }
}
