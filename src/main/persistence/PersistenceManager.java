package persistence;

import model.Budget;
import model.SpendingTracker;
import model.exceptions.ZeroOrLessException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// Manages reading (loading) from and writing (saving) to the EasyBudget application
public class PersistenceManager {

    public static final String EASY_BUDGET_FILEPATH = "easyBudget.json"; // the filepath that the budget and spending
                                                                         // tracker is saved to when the user uses the
                                                                         // Easy Budget! application

    /*
     * REQUIRES: filename is a valid JSON filename
     * MODIFIES: filename
     * EFFECTS: saves b and s to the .json file provided
     */
    public static void saveEasyBudget(Budget b, SpendingTracker s, String filename) throws IOException {
        JSONObject obj = new JSONObject();
        JSONObject budget = b.saveToJson();
        obj.put("budget", budget);
        JSONObject spendingTracker = s.saveToJson();
        obj.put("spendingTracker", spendingTracker);
        FileWriter fileWriter = new FileWriter("./data/" + filename);
        fileWriter.write(obj.toString());
        fileWriter.flush();
        fileWriter.close();
    }

    /*
     * REQUIRES: filename is a valid JSON filename
     * EFFECTS:  loads a Budget from filename
     */
    public static Budget loadBudget(String filename) throws IOException {
        String contentString = read(filename);
        JSONObject obj = new JSONObject(contentString);
        try {
            return Budget.fromJson((JSONObject) obj.get("budget"));
        } catch (ZeroOrLessException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /*
     * REQUIRES: filename is a valid JSON filename
     * EFFECTS:  loads a SpendingTracker from filename
     */
    public static SpendingTracker loadSpendingTracker(String filename) throws IOException {
        String contentString = read(filename);
        JSONObject obj = new JSONObject(contentString);
        try {
            return SpendingTracker.fromJson((JSONObject) obj.get("spendingTracker"));
        } catch (JSONException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /*
     * REQUIRES: filename is a valid JSON filepath
     * EFFECTS: returns the string representation of the json object in filename
     */
    public static String read(String filename) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get("./data/" + filename), StandardCharsets.UTF_8)) {
            stream.forEach(contentBuilder::append);
        }

        return contentBuilder.toString();
    }

    /*
     * REQUIRES: filename is a valid JSON filepath
     * MODIFIES: filename
     * EFFECTS: writes s as a JSON object to filename
     */
    public static void write(Saveable s, String filename) throws IOException {
        JSONObject obj = s.saveToJson();
        FileWriter fileWriter = new FileWriter("./data/" + filename);
        fileWriter.write(obj.toString());
        fileWriter.flush();
        fileWriter.close();
    }

}
