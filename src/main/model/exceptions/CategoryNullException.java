package model.exceptions;

public class CategoryNullException extends NullPointerException {

    /*
     * EFFECTS: returns the message displayed when this exception is caught
     */
    @Override
    public String getMessage() {
        return "Category is null!";
    }
}
