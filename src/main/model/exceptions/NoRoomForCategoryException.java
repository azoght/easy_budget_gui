package model.exceptions;

// An exception thrown when there is no room for a category to be added to a budget due to its limit
public class NoRoomForCategoryException extends CannotChangeBudgetException {

    /*
     * EFFECTS: returns the message displayed when this exception is caught
     */
    @Override
    public String getMessage() {
        return "The limit of this category will not fit the budget!";
    }
}
