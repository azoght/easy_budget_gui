package model;

import org.json.JSONObject;
import persistence.Saveable;

import java.time.LocalDate;

// Represents an expense with an amount, description, vendor, date, category, and unique id
public class Expense implements Saveable {

    private double price;             // the price of the expense
    private String description;       // the description of the expense
    private String vendor;            // the vendor/seller of the expense
    private LocalDate date;           // the date the expense was purchased on
    private Category category;        // the category that the expense belongs to
    private ExpenseID id;             // the unique id of the expense

    /*
     * EFFECTS: constructs an expense with the given information and a random id
     */
    public Expense(double price, String description, String vendor, LocalDate date, Category category) {
        this.price = price;
        this.description = description;
        this.vendor = vendor;
        this.date = date;
        this.category = category;
        this.id = new ExpenseID();
    }

    /*
     * EFFECTS: constructs an expense with the given information
     */
    public Expense(double price, String description, String vendor, LocalDate date, Category category, String id) {
        this.price = price;
        this.description = description;
        this.vendor = vendor;
        this.date = date;
        this.category = category;
        this.id = new ExpenseID(id);
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getVendor() {
        return vendor;
    }

    public LocalDate getDate() {
        return date;
    }

    public Category getCategory() {
        return category;
    }

    public String getId() {
        return id.getId();
    }

    /*
     * REQUIRES: jsonObj is not empty
     * EFFECTS:  returns the Expense represented by jsonObj
     */
    public static Expense fromJson(JSONObject jsonObj) {
        return new Expense(jsonObj.getDouble("price"),
                jsonObj.getString("description"),
                jsonObj.getString("vendor"),
                LocalDate.parse(jsonObj.getString("date")),
                Category.stringToCategory(jsonObj.getString("category")),
                jsonObj.getString("id"));
    }

    /*
     * EFFECTS: saves the Expense as a JSONObject
     */
    @Override
    public JSONObject saveToJson() {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("id", id.getId());
        jsonObj.put("price", price);
        jsonObj.put("description", description);
        jsonObj.put("vendor", vendor);
        jsonObj.put("date", date.toString());
        jsonObj.put("category", category.name);
        return jsonObj;
    }

}
