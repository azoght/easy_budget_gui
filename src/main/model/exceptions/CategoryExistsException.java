package model.exceptions;

import model.Category;

// An exception thrown when a category that already exists in the budget is being added
public class CategoryExistsException extends CannotChangeBudgetException {

    private Category category;

    /*
     * EFFECTS: constructs a new CategoryExistsException with the category that already exists in the budget
     */
    public CategoryExistsException(Category category) {
        this.category = category;
    }

    /*
     * EFFECTS: returns the message displayed when this exception is caught
     */
    @Override
    public String getMessage() {
        return "The category \"" + category.name + "\" already exists in the budget!";
    }
}
