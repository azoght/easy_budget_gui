package persistence;

import model.Expense;
import model.Category;
import model.SpendingTracker;
import model.exceptions.CategoryNullException;
import model.exceptions.DateNullException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class SpendingTrackerJsonTest {

    @Test
    void testWriteReadExpenseJson() {
        String pathname = "expense_t.json";
        Expense testExpense1 = new Expense(100,
                "something",
                "someone",
                LocalDate.of(2023, 7, 22),
                Category.OTHER);
        try {
            FileWriter fileWriter = new FileWriter("./data/" + pathname);
            fileWriter.write(testExpense1.saveToJson().toString());
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        try {
            String contentString = PersistenceManager.read(pathname);
            Expense testExpense = Expense.fromJson(new JSONObject(contentString));
            assertEquals(testExpense1.getId(), testExpense.getId());
            assertEquals(testExpense1.getPrice(), testExpense.getPrice());
            assertEquals(testExpense1.getDescription(), testExpense.getDescription());
            assertEquals(testExpense1.getVendor(), testExpense.getVendor());
            assertEquals(testExpense1.getDate(), testExpense.getDate());
            assertEquals(testExpense1.getCategory(), testExpense.getCategory());
        } catch (Exception e) {
            fail(e.getMessage() + "\nThis exception should not be thrown!");
        }
    }

    @Test
    void testWriteReadSpendingTrackerJson() {
        String pathname = "spendingTracker_t.json";
        SpendingTracker testSpendingTracker = new SpendingTracker();
        Expense testExpense1 = new Expense(100,
                "something",
                "someone",
                LocalDate.of(2023, 7, 22),
                Category.OTHER);
        Expense testExpense2 = new Expense(400,
                "groceries",
                "safeway",
                LocalDate.of(2023, 7, 16),
                Category.FOOD);
        Expense testExpense3 = new Expense(300,
                "rent",
                "n/a",
                LocalDate.of(2023, 7, 1),
                Category.HOUSING);
        try {
            testSpendingTracker.addExpense(testExpense1);
        } catch (Exception e) {
            fail(e.getMessage() + "\nThis exception should not be thrown!");
        }
        try {
            testSpendingTracker.addExpense(testExpense2);
        } catch (Exception e) {
            fail(e.getMessage() + "\nThis exception should not be thrown!");
        }
        try {
            testSpendingTracker.addExpense(testExpense3);
        } catch (Exception e) {
            fail(e.getMessage() + "\nThis exception should not be thrown!");
        }
        try {
            PersistenceManager.write(testSpendingTracker, pathname);
        } catch (Exception e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        try {
            String contentString = PersistenceManager.read(pathname);
            testSpendingTracker = SpendingTracker.fromJson(new JSONObject(contentString));
            assertEquals(3, testSpendingTracker.getExpenses().size());
            assertEquals(testExpense1.getId(), testSpendingTracker.getExpenses().get(0).getId());
            assertEquals(testExpense1.getPrice(),  testSpendingTracker.getExpenses().get(0).getPrice());
            assertEquals(testExpense1.getDescription(),  testSpendingTracker.getExpenses().get(0).getDescription());
            assertEquals(testExpense1.getVendor(),  testSpendingTracker.getExpenses().get(0).getVendor());
            assertEquals(testExpense1.getDate(),  testSpendingTracker.getExpenses().get(0).getDate());
            assertEquals(testExpense1.getCategory(),  testSpendingTracker.getExpenses().get(0).getCategory());
            assertEquals(testExpense2.getId(), testSpendingTracker.getExpenses().get(1).getId());
            assertEquals(testExpense2.getPrice(),  testSpendingTracker.getExpenses().get(1).getPrice());
            assertEquals(testExpense2.getDescription(),  testSpendingTracker.getExpenses().get(1).getDescription());
            assertEquals(testExpense2.getVendor(),  testSpendingTracker.getExpenses().get(1).getVendor());
            assertEquals(testExpense2.getDate(),  testSpendingTracker.getExpenses().get(1).getDate());
            assertEquals(testExpense2.getCategory(),  testSpendingTracker.getExpenses().get(1).getCategory());
            assertEquals(testExpense3.getId(), testSpendingTracker.getExpenses().get(2).getId());
            assertEquals(testExpense3.getPrice(),  testSpendingTracker.getExpenses().get(2).getPrice());
            assertEquals(testExpense3.getDescription(),  testSpendingTracker.getExpenses().get(2).getDescription());
            assertEquals(testExpense3.getVendor(),  testSpendingTracker.getExpenses().get(2).getVendor());
            assertEquals(testExpense3.getDate(),  testSpendingTracker.getExpenses().get(2).getDate());
            assertEquals(testExpense3.getCategory(),  testSpendingTracker.getExpenses().get(2).getCategory());
        } catch (Exception e) {
            fail(e.getMessage() + "\nThis exception should not be thrown!");
        }
    }

    @Test
    void testLoadSpendingTrackerFromInvalidJsonObjectThrowsNullException() {
        JSONObject obj = new JSONObject();
        try {
            SpendingTracker.fromJson(obj);
            fail("An exception should have been thrown!");
        } catch (Exception e) {
            //
        }
    }

    @Test
    void testLoadSpendingTrackerWithZeroPricedExpense() {
        JSONObject obj = new JSONObject();
        JSONArray expenses = new JSONArray();
        Expense testExpense = new Expense(0,
                "something",
                "someone",
                LocalDate.now(),
                Category.OTHER);
        expenses.put(testExpense.saveToJson());
        obj.put("expenses", expenses);
        try {
            SpendingTracker spendingTracker = SpendingTracker.fromJson(obj);
            assertEquals(0, spendingTracker.getExpenses().size());
        } catch (Exception e) {
            fail(e.getMessage() + "\nThis exception should have not been thrown!");
        }
    }

    @Test
    void testLoadSpendingTrackerWithNegativelyPricedExpense() {
        JSONObject obj = new JSONObject();
        JSONArray expenses = new JSONArray();
        Expense testExpense = new Expense(-100,
                "something",
                "someone",
                LocalDate.now(),
                Category.OTHER);
        expenses.put(testExpense.saveToJson());
        obj.put("expenses", expenses);
        try {
            SpendingTracker spendingTracker = SpendingTracker.fromJson(obj);
            assertEquals(0, spendingTracker.getExpenses().size());
        } catch (Exception e) {
            fail(e.getMessage() + "\nThis exception should have not been thrown!");
        }
    }

    @Test
    void testLoadSpendingTrackerWithEmptyDescriptionExpense() {
        JSONObject obj = new JSONObject();
        JSONArray expenses = new JSONArray();
        Expense testExpense = new Expense(100,
                "",
                "someone",
                LocalDate.now(),
                Category.OTHER);
        expenses.put(testExpense.saveToJson());
        obj.put("expenses", expenses);
        try {
            SpendingTracker spendingTracker = SpendingTracker.fromJson(obj);
            assertEquals(0, spendingTracker.getExpenses().size());
        } catch (Exception e) {
            fail(e.getMessage() + "\nThis exception should have not been thrown!");
        }
    }

    @Test
    void testLoadSpendingTrackerWithEmptyVendorExpense() {
        JSONObject obj = new JSONObject();
        JSONArray expenses = new JSONArray();
        Expense testExpense = new Expense(100,
                "something",
                "",
                LocalDate.now(),
                Category.OTHER);
        expenses.put(testExpense.saveToJson());
        obj.put("expenses", expenses);
        try {
            SpendingTracker spendingTracker = SpendingTracker.fromJson(obj);
            assertEquals(0, spendingTracker.getExpenses().size());
        } catch (Exception e) {
            fail(e.getMessage() + "\nThis exception should have not been thrown!");
        }
    }

    @Test
    void testWriteReadAddExpenseWithNullDateToSpendingTracker() {
        SpendingTracker testSpendingTracker = new SpendingTracker();
        Expense testExpense1 = new Expense(100,
                "something",
                "someone",
                null,
                Category.OTHER);
        try {
            testSpendingTracker.addExpense(testExpense1);
            fail("A DateNullException should have been thrown!");
        } catch (DateNullException e) {
            //
        } catch (Exception e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
    }

    @Test
    void testWriteReadAddExpenseWithNullCategoryToSpendingTracker() {
        SpendingTracker testSpendingTracker = new SpendingTracker();
        Expense testExpense1 = new Expense(100,
                "something",
                "someone",
                LocalDate.now(),
                null);
        try {
            testSpendingTracker.addExpense(testExpense1);
            fail("A DateNullException should have been thrown!");
        } catch (CategoryNullException e) {
            //
        } catch (Exception e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
    }

}
