package model;

import java.util.HashSet;
import java.util.Set;

// Represents a filter for expenses by one or more categories
public class FilterByCategories implements ExpenseFilter {

    private Set<Category> categories;   // the list of categories to filter by

    /*
     * EFFECTS: constructs a category filter for a given single category
     */
    public FilterByCategories(Category category) {
        categories = new HashSet<>();
        categories.add(category);
    }

    /*
     * EFFECTS: constructs a category filter for a set of categories
     */
    public FilterByCategories(Set<Category> categories) {
        this.categories = categories;
    }

    /*
     * EFFECTS: if the category of e is in categories, returns true
     *          otherwise returns false
     */
    @Override
    public boolean accept(Expense e) {
        return categories.contains(e.getCategory());
    }

    /*
     * EFFECTS: returns a string representation of the category filter
     */
    @Override
    public String toString() {
        StringBuilder filterString = new StringBuilder("Filter by categories: ");
        for (Category c : categories) {
            filterString.append(c.name);
            filterString.append(", ");
        }
        return filterString.substring(0, filterString.length() - 2); // everything but the last comma and space
    }

    /*
     * MODIFIES: this
     * EFFECTS:  adds a category to the filter
     */
    public void addCategory(Category category) {
        categories.add(category);
    }

    /*
     * REQUIRES: category is in categories
     * MODIFIES: this
     * EFFECTS:  removes a category from the filter
     */
    public void removeCategory(Category category) {
        categories.remove(category);
    }
}
