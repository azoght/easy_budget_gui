package model.exceptions;

// An exception thrown when the vendor of an expense being added to the spending tracker is empty
public class VendorZeroLengthException extends ZeroLengthException {

    /*
     * EFFECTS: returns the message displayed when this exception is caught
     */
    @Override
    public String getMessage() {
        return "Vendor is empty!";
    }
}
