package model;

// Represents a category which a budget item or expense belongs to
public enum Category {
    HOUSING("Housing"),
    TRANSPORTATION("Transportation"),
    FOOD("Food & Groceries"),
    HEALTH("Health & Medical Expenses"),
    INSURANCE("Insurance"),
    DEBT("Debt Payments"),
    ENTERTAINMENT_RECREATION("Entertainment & Recreation"),
    EDUCATION("Education"),
    SAVINGS_INVESTMENTS("Savings & Investments"),
    PERSONAL_CARE("Personal Care"),
    OTHER("Others");

    public final String name; // the name of the category

    /*
     * EFFECTS: constructs a new Category with a name
     */
    Category(String name) {
        this.name = name;
    }

    /*
     * EFFECTS: returns the Category with string category, if it exists
     *          otherwise, returns null
     */
    public static Category stringToCategory(String category) {
        for (Category c : Category.values()) {
            if (category.equalsIgnoreCase(c.name)) {
                return c;
            }
        }
        return null;
    }
}
