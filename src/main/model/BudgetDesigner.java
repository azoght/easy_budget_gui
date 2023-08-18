package model;

import model.exceptions.ZeroOrLessException;

// Represents a factory for budgets
public class BudgetDesigner {

    public BudgetDesigner() {

    }

    /*
     * EFFECTS: constructs a monthly budget with a limit
     */
    public Budget createBudget(double budgetLimit) {
        try {
            return new Budget(budgetLimit);
        } catch (ZeroOrLessException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

}
