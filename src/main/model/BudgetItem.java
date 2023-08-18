package model;

import model.exceptions.ZeroOrLessException;
import org.json.JSONObject;
import persistence.Saveable;

// Represents an item in a budget with a limit and category
public class BudgetItem implements Saveable {

    private double limit;       // the limit of the item
    private Category category;  // the category of the item, belonging to enum Category

    /*
     * REQUIRES: limit > 0
     * EFFECTS:  if limit <= 0, throws ZeroOrLessException
     *           otherwise, constructs a budget item with a limit and category
     */
    public BudgetItem(double limit, Category category) throws ZeroOrLessException {
        if (limit <= 0) {
            throw new ZeroOrLessException();
        }
        this.limit = limit;
        this.category = category;
    }

    /*
     * REQUIRES: limit > 0
     * MODIFIES: this
     * EFFECTS:  changes the limit of the budget item
     */
    public void setLimit(double limit) {
        this.limit = limit;
    }

    public double getLimit() {
        return limit;
    }

    public Category getCategory() {
        return category;
    }

    /*
     * REQUIRES: jsonObj is not empty
     * EFFECTS:  returns the BudgetItem represented by jsonObj
     */
    public static BudgetItem fromJson(JSONObject jsonObj) {
        try {
            return new BudgetItem(jsonObj.getDouble("limit"),
                    Category.stringToCategory(jsonObj.getString("category")));
        } catch (ZeroOrLessException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /*
     * EFFECTS: saves the BudgetItem as a JSONObject
     */
    @Override
    public JSONObject saveToJson() {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("category", category.name);
        jsonObj.put("limit", limit);
        return jsonObj;
    }

}
