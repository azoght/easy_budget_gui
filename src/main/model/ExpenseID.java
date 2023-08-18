package model;

import java.util.UUID;

// Represents a unique identifier for an expense
public class ExpenseID {
    
    private final String id;

    /*
     * EFFECTS: constructs a new ExpenseID using a random UUID string
     */
    public ExpenseID() {
        id = UUID.randomUUID().toString();
    }

    /*
     * EFFECTS: constructs a new ExpenseID with an existing string id
     */
    public ExpenseID(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
