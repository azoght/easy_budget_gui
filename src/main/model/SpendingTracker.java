package model;

import model.exceptions.*;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Saveable;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.List;

// Represents a spending tracker with a list of expenses added by the user
public class SpendingTracker implements Saveable {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("EEE MMM dd yyyy");
    // the date formatter that displays weekday, month, day of month, and 4-digit year respectively

    private List<Expense> expenses; // the list of expenses in the tracker

    /*
     * EFFECTS: constructs a spending tracker with an empty list of expenses
     */
    public SpendingTracker() {
        expenses = new ArrayList<>();
    }

    /*
     * REQUIRES: price > 0
     *           AND description.length() != 0
     *           AND vendor.length() != 0
     *           AND date != null
     *           AND category != null
     * MODIFIES: this
     * EFFECTS:  if price <= 0, throws ZeroOrLessException
     *           if description.length() == 0 OR vendor.length() == 0, throws ZeroLengthException
     *           if date == null OR category == null, throws NullPointerException
     *           otherwise, adds an expense to expenses with price, description, vendor, date, and category
     */
    public void addExpense(double price, String description, String vendor, LocalDate date, Category category) throws
            ZeroOrLessException, ZeroLengthException, NullPointerException {
        if (price <= 0) {
            throw new ZeroOrLessException();
        }
        if (description.length() == 0) {
            throw new DescriptionZeroLengthException();
        }
        if (vendor.length() == 0) {
            throw new VendorZeroLengthException();
        }
        if (date == null) {
            throw new DateNullException();
        }
        if (category == null) {
            throw new CategoryNullException();
        }
        Expense expense = new Expense(price, description, vendor, date, category);
        expenses.add(expense);
        EventLog.getInstance().logEvent(new Event("Expense '" + description + "' from " + vendor
                + " purchased " + "for $" + String.format("%.2f", price) + " on "
                + date.format(FORMATTER).replaceAll("\\.", "") + " with category '" + category.name
                + "' added to spending tracker"));
    }

    /*
     * REQUIRES: e.getPrice() > 0
     *           AND e.getDescription().length() != 0
     *           AND e.getVendor().length() != 0
     *           AND e.getDate() != null
     *           AND e.getCategory() != null
     * MODIFIES: this
     * EFFECTS:  if e.getPrice() <= 0, throws ZeroOrLessException
     *           if e.getDescription().length() == 0 OR e.getVendor().length() == 0, throws ZeroLengthException
     *           if e.getDate() == null OR e.getCategory() == null, throws NullPointerException
     *           otherwise, adds e to expenses
     */
    public void addExpense(Expense e) throws ZeroOrLessException, ZeroLengthException, NullPointerException {
        if (e.getPrice() <= 0) {
            throw new ZeroOrLessException();
        }
        if (e.getDescription().length() == 0) {
            throw new DescriptionZeroLengthException();
        }
        if (e.getVendor().length() == 0) {
            throw new VendorZeroLengthException();
        }
        if (e.getDate() == null) {
            throw new DateNullException();
        }
        if (e.getCategory() == null) {
            throw new CategoryNullException();
        }
        expenses.add(e);
        EventLog.getInstance().logEvent(new Event("Expense '" + e.getDescription() + "' from " + e.getVendor()
                + " purchased for $" + String.format("%.2f", e.getPrice()) + " on "
                + e.getDate().format(FORMATTER).replaceAll("\\.", "") + " with category '"
                + e.getCategory().name + "' added to spending tracker"));
    }

    /*
     * REQUIRES: containsExpense(id) != null
     * MODIFIES: this
     * EFFECTS:  if an expense with id does not exist in expenses, throws ExpenseDoesNotExistException
     *           otherwise, removes that expense from expenses
     */
    public void deleteExpense(String id) throws ExpenseDoesNotExistException {
        Expense expenseToDelete = containsExpense(id);
        if (expenseToDelete == null) {
            throw new ExpenseDoesNotExistException();
        }
        expenses.remove(expenseToDelete);
        EventLog.getInstance().logEvent(new Event("Expense '" + expenseToDelete.getDescription() + "' from "
                + expenseToDelete.getVendor() + " purchased for $" + String.format("%.2f", expenseToDelete.getPrice())
                + " on " + expenseToDelete.getDate().format(FORMATTER).replaceAll("\\.", "")
                + " with category '" + expenseToDelete.getCategory().name + "' deleted from spending tracker"));
    }

    /*
     * EFFECTS: if an expense with id exists in items, returns the Expense with that id
     *          otherwise, returns null
     */
    private Expense containsExpense(String id) {
        for (Expense e : expenses) {
            if (e.getId().equals(id)) {
                return e;
            }
        }
        return null;
    }

    /*
     * REQUIRES: newPrice > 0
     *           containsExpense(id) != null
     * MODIFIES: this
     * EFFECTS:  if newPrice <= 0, throws ZeroOrLessException
     *           if an expense with id does not exist in expenses, throws ExpenseDoesNotExistException
     *           otherwise,
     *              - if newPrice is different from the price of the expense, sets the price of that expense to newPrice
     *              - otherwise, does nothing
     */
    public void editPriceOf(String id, double newPrice) throws ZeroOrLessException, ExpenseDoesNotExistException {
        if (newPrice <= 0) {
            throw new ZeroOrLessException();
        }
        Expense expenseToEdit = containsExpense(id);
        if (expenseToEdit == null) {
            throw new ExpenseDoesNotExistException();
        }
        if (expenseToEdit.getPrice() != newPrice) {
            expenseToEdit.setPrice(newPrice);
            EventLog.getInstance().logEvent(new Event("Price of expense '" + expenseToEdit.getDescription()
                    + "' from " + expenseToEdit.getVendor() + " purchased on "
                    + expenseToEdit.getDate().format(FORMATTER).replaceAll("\\.", "")
                    + " with category '" + expenseToEdit.getCategory().name + "' in spending tracker set to $"
                    + String.format("%.2f", newPrice)));
        }
    }

    /*
     * REQUIRES: newDescription.length() != 0
     *           containsExpense(id) != null
     * MODIFIES: this
     * EFFECTS:  if newDescription.length() == 0, throws ZeroLengthException
     *           if an expense with id does not exist in expenses, throws ExpenseDoesNotExistException
     *           otherwise,
     *              - if newDescription is different from the description of the expense, sets the description of that
     *                expense to newDescription
     *              - otherwise, does nothing
     */
    public void editDescriptionOf(String id, String newDescription) throws ZeroLengthException,
            ExpenseDoesNotExistException {
        if (newDescription.length() == 0) {
            throw new DescriptionZeroLengthException();
        }
        Expense expenseToEdit = containsExpense(id);
        if (expenseToEdit == null) {
            throw new ExpenseDoesNotExistException();
        }
        if (!expenseToEdit.getDescription().equals(newDescription)) {
            expenseToEdit.setDescription(newDescription);
            EventLog.getInstance().logEvent(new Event("Description of expense purchased for $"
                    + String.format("%.2f", expenseToEdit.getPrice()) + " from " + expenseToEdit.getVendor()
                    + " on " + expenseToEdit.getDate().format(FORMATTER).replaceAll("\\.", "")
                    + " with category '" + expenseToEdit.getCategory().name + "' in spending tracker set to '"
                    + newDescription + "'"));
        }
    }

    /*
     * REQUIRES: newVendor.length() != 0
     *           containsExpense(id) != null
     * MODIFIES: this
     * EFFECTS:  if newVendor.length() == 0, throws ZeroLengthException
     *           if an expense with id does not exist in expenses, throws ExpenseDoesNotExistException
     *           otherwise,
     *              - if newVendor is different from the vendor of the expense, sets the vendor of that expense to
     *                newVendor
     *              - otherwise, does nothing
     */
    public void editVendorOf(String id, String newVendor) throws ZeroLengthException, ExpenseDoesNotExistException {
        if (newVendor.length() == 0) {
            throw new VendorZeroLengthException();
        }
        Expense expenseToEdit = containsExpense(id);
        if (expenseToEdit == null) {
            throw new ExpenseDoesNotExistException();
        }
        if (!expenseToEdit.getVendor().equals(newVendor)) {
            expenseToEdit.setVendor(newVendor);
            EventLog.getInstance().logEvent(new Event("Vendor of expense '" + expenseToEdit.getDescription()
                    + "' purchased for $" + String.format("%.2f", expenseToEdit.getPrice()) + " on "
                    + expenseToEdit.getDate().format(FORMATTER).replaceAll("\\.", "")
                    + " with category '" + expenseToEdit.getCategory().name + "' in spending tracker set to "
                    + newVendor));
        }
    }

    /*
     * REQUIRES: newDate != null
     *           containsExpense(id) != null
     * MODIFIES: this
     * EFFECTS:  if newDate == null, throws NullPointerException
     *           if an expense with id does not exist in expenses, throws ExpenseDoesNotExistException
     *           otherwise,
     *              - if newDate is different from the date of the expense, sets the date of that expense to newDate
     *              - otherwise, does nothing
     */
    public void editDateOf(String id, LocalDate newDate) throws NullPointerException, ExpenseDoesNotExistException {
        if (newDate == null) {
            throw new DateNullException();
        }
        Expense expenseToEdit = containsExpense(id);
        if (expenseToEdit == null) {
            throw new ExpenseDoesNotExistException();
        }
        if (!expenseToEdit.getDate().toString().equals(newDate.toString())) {
            expenseToEdit.setDate(newDate);
            EventLog.getInstance().logEvent(new Event("Purchase date of expense '"
                    + expenseToEdit.getDescription() + "' purchased for $"
                    + String.format("%.2f", expenseToEdit.getPrice()) + " from " + expenseToEdit.getVendor()
                    + " with category '" + expenseToEdit.getCategory().name + "' in spending tracker set to "
                    + newDate.format(FORMATTER).replaceAll("\\.", "")));
        }
    }

    /*
     * REQUIRES: newCategory != null
     *           containsExpense(id) != null
     * MODIFIES: this
     * EFFECTS:  if newCategory == null, throws NullPointerException
     *           if an expense with id does not exist in expenses, throws ExpenseDoesNotExistException
     *           otherwise,
     *              - if newCategory is different from the category of the expense, sets the category of that expense to
     *                newCategory
     *              - otherwise, does nothing
     */
    public void editCategoryOf(String id, Category newCategory) throws NullPointerException,
            ExpenseDoesNotExistException {
        if (newCategory == null) {
            throw new CategoryNullException();
        }
        Expense expenseToEdit = containsExpense(id);

        if (expenseToEdit == null) {
            throw new ExpenseDoesNotExistException();
        }
        if (expenseToEdit.getCategory() != newCategory) {
            expenseToEdit.setCategory(newCategory);
            EventLog.getInstance().logEvent(new Event("Category of expense '" + expenseToEdit.getDescription()
                    + "' purchased for $" + String.format("%.2f", expenseToEdit.getPrice()) + " from "
                    + expenseToEdit.getVendor() + " on "
                    + expenseToEdit.getDate().format(FORMATTER).replaceAll("\\.", "")
                    + "' in spending tracker set to '" + newCategory.name + "'"));
        }
    }

    /*
     * EFFECTS: returns expenses with each expense filter in filters applied
     */
    public List<Expense> filter(List<ExpenseFilter> filters) {
        List<Expense> filteredExpenses = new ArrayList<>();
        for (Expense e : expenses) {
            if (acceptAll(filters, e)) {
                filteredExpenses.add(e);
            }
        }
        return filteredExpenses;
    }

    /*
     * REQUIRES: e is in expenses
     * EFFECTS:  if e satisfies all expense filters in filters, returns true
     *           otherwise returns false
     */
    private boolean acceptAll(List<ExpenseFilter> filters, Expense e) {
        for (ExpenseFilter f : filters) {
            if (!f.accept(e)) {
                return false;
            }
        }
        return true;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    /*
     * REQUIRES: jsonObj is not empty
     * EFFECTS:  returns the SpendingTracker represented by jsonObj
     */
    public static SpendingTracker fromJson(JSONObject jsonObj) {
        SpendingTracker spendingTracker = new SpendingTracker();
        JSONArray expenseItems = jsonObj.getJSONArray("expenses");
        for (Object obj : expenseItems) {
            try {
                spendingTracker.addExpense(Expense.fromJson((JSONObject) obj));
            } catch (ZeroOrLessException | ZeroLengthException e) {
                //
            }
        }
        return spendingTracker;
    }

    /*
     * EFFECTS: saves the SpendingTracker as a JSONObject
     */
    @Override
    public JSONObject saveToJson() {
        JSONObject obj = new JSONObject();
        JSONArray expenseItems = new JSONArray();
        for (Expense e : expenses) {
            expenseItems.put(e.saveToJson());
        }
        obj.put("expenses", expenseItems);
        return obj;
    }
}
