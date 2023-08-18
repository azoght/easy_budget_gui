package model.exceptions;

// An exception thrown when the date of an expense being added to the spending tracker is null
public class DateNullException extends NullPointerException {

    /*
     * EFFECTS: returns the message displayed when this exception is caught
     */
    @Override
    public String getMessage() {
        return "Date is null!";
    }
}
