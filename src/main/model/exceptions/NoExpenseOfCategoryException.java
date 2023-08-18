package model.exceptions;

import model.Category;

// An exception thrown when there is no expense in the spending tracker with a specific category
public class NoExpenseOfCategoryException extends Exception {

    private Category category;

    /*
     * EFFECTS: constructs a new NoExpenseOfCategoryException with the category that no expense has
     */
    public NoExpenseOfCategoryException(Category category) {
        this.category = category;
    }

    /*
     * EFFECTS: returns the message displayed when this exception is caught
     */
    @Override
    public String getMessage() {
        return "No expense of category \"" + category.name + "\" in tracker!";
    }
}
