package model;

import model.exceptions.*;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Saveable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

// Represents a budget having a name, limit (in dollars), and set of expense categories (items)
public class Budget implements Saveable, Iterable<BudgetItem> {

    private double budgetLimit;                                // the current limit of the budget
    private ArrayList<BudgetItem> items = new ArrayList<>();   // the list of items in the budget

    /*
     * REQUIRES: budgetLimit > 0
     * EFFECTS:  if budgetLimit is less than or equal to zero, throws ZeroOrLessException
     *           otherwise, constructs a Budget with a limit of budgetLimit
     */
    public Budget(double budgetLimit) throws ZeroOrLessException {
        if (budgetLimit <= 0) {
            throw new ZeroOrLessException();
        }
        this.budgetLimit = budgetLimit;
    }

    public double getTotalLimit() {
        return budgetLimit;
    }

    public ArrayList<BudgetItem> getItems() {
        return items;
    }

    /*
     * REQUIRES: budgetLimit > 0, budgetLimit >= sumOfLimits()
     * MODIFIES: this
     * EFFECTS:  if budgetLimit is less than or equal to zero, throws ZeroOrLessException
     *           if budgetLimit is less than sumOfLimits(), throws CannotChangeBudgetException
     *           otherwise, changes the total limit of the budget to budgetLimit
     */
    public void setTotalLimit(double budgetLimit) throws ZeroOrLessException, CannotChangeBudgetException {
        if (budgetLimit <= 0) {
            throw new ZeroOrLessException();
        }
        if (budgetLimit < sumOfLimits()) {
            throw new CannotChangeBudgetException();
        }
        this.budgetLimit = budgetLimit;
        EventLog.getInstance().logEvent(new Event("Total limit of budget set to $"
                + String.format("%.2f", budgetLimit)));
    }

    /*
     * REQUIRES: b.getLimit() > 0
     *           AND b.getLimit() <= getTotalLimit() - sumOfLimits()
     *           AND containsCategory(b.getCategory()) == null
     * MODIFIES: this
     * EFFECTS:  if b.getLimit() is less than or equal to zero, throws ZeroOrLessException
     *           if b.getLimit() is greater than getTotalLimit() - sumOfLimits(), throws NoRoomForCategoryException
     *           if an item with b.getCategory() does not exist in items, throws CategoryExistsException
     *           otherwise, returns false
     */
    public void addBudgetItem(BudgetItem b) throws ZeroOrLessException, CannotChangeBudgetException {
        if (b.getLimit() <= 0) {
            throw new ZeroOrLessException();
        }
        if (b.getLimit() > getTotalLimit() - sumOfLimits()) {
            throw new NoRoomForCategoryException();
        }
        if (containsCategory(b.getCategory()) != null) {
            throw new CategoryExistsException(b.getCategory());
        }
        items.add(b);
        EventLog.getInstance().logEvent(new Event("Budget item with category '" + b.getCategory().name
                + "' and limit $" + String.format("%.2f", b.getLimit()) + " added to budget"));
    }

    /*
     * REQUIRES: limit > 0
     *           AND limit <= getTotalLimit() - sumOfLimits()
     *           AND containsCategory(category) == null
     * MODIFIES: this
     * EFFECTS:  if limit is less than or equal to zero, throws ZeroOrLessException
     *           if limit is greater than getTotalLimit() - sumOfLimits(), throws NoRoomForCategoryException
     *           if an item with category does not exist in items, throws CategoryExistsException
     *           otherwise, returns false
     */
    public void addBudgetItem(double limit, Category category) throws ZeroOrLessException,
            CannotChangeBudgetException {
        if (limit <= 0) {
            throw new ZeroOrLessException();
        }
        if (limit > getTotalLimit() - sumOfLimits()) {
            throw new NoRoomForCategoryException();
        }
        if (containsCategory(category) != null) {
            throw new CategoryExistsException(category);
        }
        items.add(new BudgetItem(limit, category));
        EventLog.getInstance().logEvent(new Event("Budget item with category '" + category.name
                + "' and limit $" + String.format("%.2f", limit) + " added to budget"));
    }

    /*
     * REQUIRES: containsCategory(category) != null
     * MODIFIES: this
     * EFFECTS:  if an item with category does not exist in items, throws CategoryDoesNotExistException
     *           otherwise, removes that item from items
     */
    public void deleteBudgetItem(Category category) throws CategoryDoesNotExistException {
        BudgetItem itemToRemove = containsCategory(category);
        if (itemToRemove == null) {
            throw new CategoryDoesNotExistException(category);
        }
        items.remove(itemToRemove);
        EventLog.getInstance().logEvent(new Event("Item with category '" + category.name + "' and limit $"
                + String.format("%.2f", itemToRemove.getLimit()) + " deleted from budget"));
    }

    /*
     * EFFECTS: returns the sum of all limits in items
     */
    public double sumOfLimits() {
        double sum = 0;
        for (BudgetItem b : items) {
            sum += b.getLimit();
        }
        return sum;
    }

    /*
     * EFFECTS: returns the sum of all limits in items, minus the specified category
     */
    public double sumOfLimitsMinusCategory(Category category) {
        double sum = 0;
        for (BudgetItem b : items) {
            if (b.getCategory() != category) {
                sum += b.getLimit();
            }
        }
        return sum;
    }

    /*
     * EFFECTS: if the specified category exists in items, returns the BudgetItem with that category
     *          otherwise, returns null
     */
    private BudgetItem containsCategory(Category category) {
        for (BudgetItem b : items) {
            if (b.getCategory() == category) {
                return b;
            }
        }
        return null;
    }

    /*
     * REQUIRES: newLimit > 0
     *           AND newLimit <= getTotalLimit() - sumOfLimits()
     *           AND containsCategory(category) != null
     * MODIFIES: this
     * EFFECTS:  if newLimit <= 0, throws ZeroOrLessException
     *           if newLimit > getTotalLimit() - sumOfLimitsMinusCategory(category), throws NoRoomForCategoryException
     *           if an item with category does not exist in items, throws CategoryDoesNotExistException
     *           otherwise, changes the limit of that item to newLimit
     */
    public void editLimitOf(Category category, double newLimit) throws ZeroOrLessException,
            CannotChangeBudgetException {
        if (newLimit <= 0) {
            throw new ZeroOrLessException();
        }
        if (newLimit > getTotalLimit() - sumOfLimitsMinusCategory(category)) {
            throw new NoRoomForCategoryException();
        }
        BudgetItem itemToEdit = containsCategory(category);
        if (itemToEdit == null) {
            throw new CategoryDoesNotExistException(category);
        }
        itemToEdit.setLimit(newLimit);
        EventLog.getInstance().logEvent(new Event("Edited limit of budget item with category '"
                + category.name + "' to $" + String.format("%.2f", newLimit)));
    }

    /*
     * REQUIRES: containsCategory(category) != null
     *           AND containsExpenseOfCategory(tracker, category)
     * EFFECTS:
     *           if an item with category does not exist in items, throws CategoryDoesNotExistException
     *           if there is no expense in tracker.getExpenses() of category, throws NoExpenseOfCategoryException
     *           otherwise,
     *           -  filters tracker.getExpenses() by current month in current year and category
     *           -  if total amount of expenses < spending limit of category, returns -1
     *           -  if total amount of expenses == spending limit of category, returns 0
     *           -  if total amount of expenses > spending limit of category, returns 1
     */
    public int compareToBudget(SpendingTracker tracker, Category category) throws CategoryDoesNotExistException,
            NoExpenseOfCategoryException {
        BudgetItem budgetItem = containsCategory(category);
        if (budgetItem == null) {
            throw new CategoryDoesNotExistException(category);
        }

        List<ExpenseFilter> expenseFilters = new ArrayList<>();
        expenseFilters.add(new FilterByDate(LocalDate.now().getMonth()));
        expenseFilters.add(new FilterByCategories(category));
        List<Expense> expenses = tracker.filter(expenseFilters);
        if (!containsExpenseOfCategory(expenses, category)) {
            throw new NoExpenseOfCategoryException(category);
        }

        double expenseSum = 0;
        for (Expense e: expenses) {
            if (e.getCategory() == category) {
                expenseSum += e.getPrice();
            }
        }

        return Double.compare(expenseSum, budgetItem.getLimit());
    }

    /*
     * EFFECTS: returns true if at least one expense in expenses has category
     *          otherwise, returns false
     */
    private boolean containsExpenseOfCategory(List<Expense> expenses, Category category) {
        for (Expense e : expenses) {
            if (e.getCategory() == category) {
                return true;
            }
        }
        return false;
    }

    /*
     * REQUIRES: jsonObj is not empty
     * EFFECTS:  returns the Budget represented by jsonObj
     */
    public static Budget fromJson(JSONObject jsonObj) throws ZeroOrLessException {
        double totalLimit = jsonObj.getDouble("limit");
        if (totalLimit <= 0) {
            throw new ZeroOrLessException();
        }
        Budget budget = new Budget(totalLimit);
        JSONArray items = jsonObj.getJSONArray("items");
        for (Object obj : items) {
            try {
                budget.addBudgetItem(Objects.requireNonNull(BudgetItem.fromJson((JSONObject) obj)));
            } catch (CannotChangeBudgetException e) {
                //
            }
        }
        return budget;
    }

    /*
     * EFFECTS: saves the Budget as a JSONObject
     */
    @Override
    public JSONObject saveToJson() {
        JSONObject obj = new JSONObject();
        obj.put("limit", budgetLimit);
        JSONArray budgetItems = new JSONArray();
        for (BudgetItem b : items) {
            budgetItems.put(b.saveToJson());
        }
        obj.put("items", budgetItems);
        return obj;
    }

    /*
     * EFFECTS: returns the iterator of items
     */
    @Override
    public Iterator<BudgetItem> iterator() {
        return items.iterator();
    }
}
