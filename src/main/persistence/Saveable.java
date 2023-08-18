package persistence;

import org.json.JSONObject;

// Represents the functionality of an object that can be saved to a JSON file
public interface Saveable {

    /*
     * EFFECTS: saves the Saveable as a JSONObject
     */
    JSONObject saveToJson();
}
