package model.exceptions;

// An exception thrown when the description of an expense being added to the spending tracker is empty
public class DescriptionZeroLengthException extends ZeroLengthException {

    /*
     * EFFECTS: returns the message displayed when this exception is caught
     */
    @Override
    public String getMessage() {
        return "Description is empty!";
    }
}
