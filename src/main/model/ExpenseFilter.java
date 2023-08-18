package model;

// Represents the functionality of a filter for expenses
public interface ExpenseFilter {

    /*
     * EFFECTS: if e satisfies the property of the filter, returns true
     *          otherwise, returns false
     */
    boolean accept(Expense e);

}
