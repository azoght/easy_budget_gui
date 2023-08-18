package model.exceptions;

import model.Category;

// An exception thrown when a category in a budget that does not exist is being accessed
public class CategoryDoesNotExistException extends CannotChangeBudgetException {

    private Category category;

    /*
     * EFFECTS: constructs a new CategoryDoesNotExistException with the category that does not exist in the budget
     */
    public CategoryDoesNotExistException(Category category) {
        this.category = category;
    }

    /*
     * EFFECTS: returns the message displayed when this exception is caught
     */
    @Override
    public String getMessage() {
        return "The category \"" + category.name + "\" does not exist in the budget!";
    }
}
