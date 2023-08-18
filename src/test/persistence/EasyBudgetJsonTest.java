package persistence;

import model.*;
import model.exceptions.ZeroOrLessException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class EasyBudgetJsonTest {

    Budget savedBudget;
    SpendingTracker savedSpendingTracker;
    Budget loadedBudget;
    SpendingTracker loadedSpendingTracker;
    BudgetItem budgetItem1;
    BudgetItem budgetItem2;
    BudgetItem budgetItem3;
    Expense expense1;
    Expense expense2;
    Expense expense3;

    @BeforeEach
    void setup() {
        try {
            budgetItem1 = new BudgetItem(100, Category.OTHER);
            budgetItem2 = new BudgetItem(300, Category.FOOD);
            budgetItem3 = new BudgetItem(500, Category.HOUSING);
        } catch (ZeroOrLessException e) {
            fail();
        }
        expense1 = new Expense(100,
                "something",
                "someone",
                LocalDate.of(2023, 7, 22),
                Category.OTHER);
        expense2 = new Expense(400,
                "groceries",
                "safeway",
                LocalDate.of(2023, 7, 16),
                Category.FOOD);
        expense3 = new Expense(300,
                "rent",
                "n/a",
                LocalDate.of(2023, 7, 1),
                Category.HOUSING);
    }

    @Test
    void testSaveLoadInvalid() {
        try {
            PersistenceManager.saveEasyBudget(savedBudget, savedSpendingTracker, "foo");
            fail("An exception should have been thrown!");
        } catch (Exception e) {
            //
        }
    }

    @Test
    void testSaveLoadEmpty() {
        try {
            savedBudget = new Budget(1);
        } catch (Exception e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        savedSpendingTracker = new SpendingTracker();
        saveToAndLoadFromFile("applicationEmpty_t.json");
        assertEquals(loadedBudget.getTotalLimit(), savedBudget.getTotalLimit());
        assertEquals(loadedBudget.getItems().size(), savedBudget.getItems().size());
        assertEquals(loadedSpendingTracker.getExpenses().size(), savedSpendingTracker.getExpenses().size());
    }

    @Test
    void testSaveLoadBudgetOnly() {
        try {
            savedBudget = new Budget(1000);
        } catch (Exception e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        try {
            savedBudget.addBudgetItem(budgetItem1);
        } catch (Exception e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        try {
            savedBudget.addBudgetItem(budgetItem2);
        } catch (Exception e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        try {
            savedBudget.addBudgetItem(budgetItem3);
        } catch (Exception e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        savedSpendingTracker = new SpendingTracker();
        saveToAndLoadFromFile("applicationBudgetOnly_t.json");
        assertEquals(loadedBudget.getTotalLimit(), savedBudget.getTotalLimit());
        assertEquals(loadedBudget.getItems().size(), savedBudget.getItems().size());
        assertEquals(loadedBudget.getItems().get(0).getLimit(), budgetItem1.getLimit());
        assertEquals(loadedBudget.getItems().get(0).getCategory(), budgetItem1.getCategory());
        assertEquals(loadedBudget.getItems().get(1).getLimit(), budgetItem2.getLimit());
        assertEquals(loadedBudget.getItems().get(1).getCategory(), budgetItem2.getCategory());
        assertEquals(loadedBudget.getItems().get(2).getLimit(), budgetItem3.getLimit());
        assertEquals(loadedBudget.getItems().get(2).getCategory(), budgetItem3.getCategory());
        assertEquals(loadedSpendingTracker.getExpenses().size(), savedSpendingTracker.getExpenses().size());
    }

    @Test
    void testSaveLoadSpendingTrackerOnly() {
        try {
            savedBudget = new Budget(1);
        } catch (Exception e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        savedSpendingTracker = new SpendingTracker();
        try {
            savedSpendingTracker.addExpense(expense1);
        } catch (Exception e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        try {
            savedSpendingTracker.addExpense(expense2);
        } catch (Exception e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        try {
            savedSpendingTracker.addExpense(expense3);
        } catch (Exception e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        saveToAndLoadFromFile("applicationSpendingTrackerOnly_t.json");
        assertEquals(loadedBudget.getTotalLimit(), savedBudget.getTotalLimit());
        assertEquals(loadedBudget.getItems().size(), savedBudget.getItems().size());
        assertEquals(loadedSpendingTracker.getExpenses().size(), savedSpendingTracker.getExpenses().size());
        assertEquals(loadedSpendingTracker.getExpenses().get(0).getId(), expense1.getId());
        assertEquals(loadedSpendingTracker.getExpenses().get(0).getPrice(), expense1.getPrice());
        assertEquals(loadedSpendingTracker.getExpenses().get(0).getDescription(), expense1.getDescription());
        assertEquals(loadedSpendingTracker.getExpenses().get(0).getVendor(), expense1.getVendor());
        assertEquals(loadedSpendingTracker.getExpenses().get(0).getDate(), expense1.getDate());
        assertEquals(loadedSpendingTracker.getExpenses().get(0).getCategory(), expense1.getCategory());
        assertEquals(loadedSpendingTracker.getExpenses().get(1).getId(), expense2.getId());
        assertEquals(loadedSpendingTracker.getExpenses().get(1).getPrice(), expense2.getPrice());
        assertEquals(loadedSpendingTracker.getExpenses().get(1).getDescription(), expense2.getDescription());
        assertEquals(loadedSpendingTracker.getExpenses().get(1).getVendor(), expense2.getVendor());
        assertEquals(loadedSpendingTracker.getExpenses().get(1).getDate(), expense2.getDate());
        assertEquals(loadedSpendingTracker.getExpenses().get(1).getCategory(), expense2.getCategory());
        assertEquals(loadedSpendingTracker.getExpenses().get(2).getId(), expense3.getId());
        assertEquals(loadedSpendingTracker.getExpenses().get(2).getPrice(), expense3.getPrice());
        assertEquals(loadedSpendingTracker.getExpenses().get(2).getDescription(), expense3.getDescription());
        assertEquals(loadedSpendingTracker.getExpenses().get(2).getVendor(), expense3.getVendor());
        assertEquals(loadedSpendingTracker.getExpenses().get(2).getDate(), expense3.getDate());
        assertEquals(loadedSpendingTracker.getExpenses().get(2).getCategory(), expense3.getCategory());
    }

    @Test
    void testSaveLoadBoth() {
        try {
            savedBudget = new Budget(1000);
        } catch (Exception e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        try {
            savedBudget.addBudgetItem(budgetItem1);
        } catch (Exception e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        try {
            savedBudget.addBudgetItem(budgetItem2);
        } catch (Exception e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        try {
            savedBudget.addBudgetItem(budgetItem3);
        } catch (Exception e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        savedSpendingTracker = new SpendingTracker();
        try {
            savedSpendingTracker.addExpense(expense1);
        } catch (Exception e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        try {
            savedSpendingTracker.addExpense(expense2);
        } catch (Exception e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        try {
            savedSpendingTracker.addExpense(expense3);
        } catch (Exception e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        saveToAndLoadFromFile("application_t.json");
        assertEquals(loadedBudget.getTotalLimit(), savedBudget.getTotalLimit());
        assertEquals(loadedBudget.getItems().size(), savedBudget.getItems().size());
        assertEquals(loadedBudget.getItems().get(0).getLimit(), budgetItem1.getLimit());
        assertEquals(loadedBudget.getItems().get(0).getCategory(), budgetItem1.getCategory());
        assertEquals(loadedBudget.getItems().get(1).getLimit(), budgetItem2.getLimit());
        assertEquals(loadedBudget.getItems().get(1).getCategory(), budgetItem2.getCategory());
        assertEquals(loadedBudget.getItems().get(2).getLimit(), budgetItem3.getLimit());
        assertEquals(loadedBudget.getItems().get(2).getCategory(), budgetItem3.getCategory());
        assertEquals(loadedSpendingTracker.getExpenses().size(), savedSpendingTracker.getExpenses().size());
        assertEquals(loadedSpendingTracker.getExpenses().get(0).getId(), expense1.getId());
        assertEquals(loadedSpendingTracker.getExpenses().get(0).getPrice(), expense1.getPrice());
        assertEquals(loadedSpendingTracker.getExpenses().get(0).getDescription(), expense1.getDescription());
        assertEquals(loadedSpendingTracker.getExpenses().get(0).getVendor(), expense1.getVendor());
        assertEquals(loadedSpendingTracker.getExpenses().get(0).getDate(), expense1.getDate());
        assertEquals(loadedSpendingTracker.getExpenses().get(0).getCategory(), expense1.getCategory());
        assertEquals(loadedSpendingTracker.getExpenses().get(1).getId(), expense2.getId());
        assertEquals(loadedSpendingTracker.getExpenses().get(1).getPrice(), expense2.getPrice());
        assertEquals(loadedSpendingTracker.getExpenses().get(1).getDescription(), expense2.getDescription());
        assertEquals(loadedSpendingTracker.getExpenses().get(1).getVendor(), expense2.getVendor());
        assertEquals(loadedSpendingTracker.getExpenses().get(1).getDate(), expense2.getDate());
        assertEquals(loadedSpendingTracker.getExpenses().get(1).getCategory(), expense2.getCategory());
        assertEquals(loadedSpendingTracker.getExpenses().get(2).getId(), expense3.getId());
        assertEquals(loadedSpendingTracker.getExpenses().get(2).getPrice(), expense3.getPrice());
        assertEquals(loadedSpendingTracker.getExpenses().get(2).getDescription(), expense3.getDescription());
        assertEquals(loadedSpendingTracker.getExpenses().get(2).getVendor(), expense3.getVendor());
        assertEquals(loadedSpendingTracker.getExpenses().get(2).getDate(), expense3.getDate());
        assertEquals(loadedSpendingTracker.getExpenses().get(2).getCategory(), expense3.getCategory());
    }

    @Test
    void testSaveLoadInvalidBudgetZeroLimit() {
        String pathname = "applicationInvalidBudgetZeroLimit_t.json";
        JSONObject obj = new JSONObject();
        JSONObject budgetObj = new JSONObject();
        budgetObj.put("limit", 0);
        budgetObj.put("items", new JSONArray());
        obj.put("budget", budgetObj);
        JSONObject spendingTrackerObj = new JSONObject();
        JSONArray expenses = new JSONArray();
        spendingTrackerObj.put("expenses", expenses);
        obj.put("spendingTracker", spendingTrackerObj);
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter("./data/" + pathname);
            fileWriter.write(obj.toString());
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        try {
            savedBudget = PersistenceManager.loadBudget(pathname);
            savedSpendingTracker = PersistenceManager.loadSpendingTracker(pathname);
            saveToAndLoadFromFile(pathname);
            assertNull(loadedBudget);
        } catch (IOException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
    }

    @Test
    void testSaveLoadInvalidBudgetNegativeLimit() {
        String pathname = "applicationInvalidBudgetNegativeLimit_t.json";
        JSONObject obj = new JSONObject();
        JSONObject budgetObj = new JSONObject();
        budgetObj.put("limit", -100);
        budgetObj.put("items", new JSONArray());
        obj.put("budget", budgetObj);
        JSONObject spendingTrackerObj = new JSONObject();
        JSONArray expenses = new JSONArray();
        spendingTrackerObj.put("expenses", expenses);
        obj.put("spendingTracker", spendingTrackerObj);
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter("./data/" + pathname);
            fileWriter.write(obj.toString());
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        try {
            savedBudget = PersistenceManager.loadBudget(pathname);
            savedSpendingTracker = PersistenceManager.loadSpendingTracker(pathname);
            saveToAndLoadFromFile(pathname);
            assertNull(loadedBudget);
        } catch (IOException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
    }

    @Test
    void testSaveLoadInvalidBudgetDuplicateCategory() {
        String pathname = "applicationInvalidBudgetDuplicateCategory_t.json";
        JSONObject obj = new JSONObject();
        JSONObject budgetObj = new JSONObject();
        budgetObj.put("limit", 800);
        JSONArray items = new JSONArray();
        JSONObject budgetItem1 = new JSONObject();
        budgetItem1.put("limit", 100);
        budgetItem1.put("category", "others");
        items.put(budgetItem1);
        JSONObject budgetItem2 = new JSONObject();
        budgetItem2.put("limit", 500);
        budgetItem2.put("category", "others");
        items.put(budgetItem2);
        budgetObj.put("items", items);
        obj.put("budget", budgetObj);
        JSONObject spendingTrackerObj = new JSONObject();
        JSONArray expenses = new JSONArray();
        spendingTrackerObj.put("expenses", expenses);
        obj.put("spendingTracker", spendingTrackerObj);
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter("./data/" + pathname);
            fileWriter.write(obj.toString());
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        try {
            savedBudget = PersistenceManager.loadBudget(pathname);
            savedSpendingTracker = PersistenceManager.loadSpendingTracker(pathname);
            saveToAndLoadFromFile(pathname);
            assertEquals(1, loadedBudget.getItems().size());
        } catch (IOException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
    }

    @Test
    void testLoadSpendingTrackerFromEmptyJsonFileThrowsJSONException() {
        try {
            loadedSpendingTracker = PersistenceManager.loadSpendingTracker("emptyJson_t.json");
        } catch (JSONException e) {
            //
        } catch (Exception e){
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        } finally {
            assertNull(loadedSpendingTracker);
        }
    }

    void saveToAndLoadFromFile(String filename) {
        try {
            PersistenceManager.saveEasyBudget(savedBudget, savedSpendingTracker, filename);
        } catch (Exception e) {
            System.out.println(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        try {
            loadedBudget = PersistenceManager.loadBudget(filename);
        } catch (Exception e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        try {
            loadedSpendingTracker = PersistenceManager.loadSpendingTracker(filename);
        } catch (Exception e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
    }

}
