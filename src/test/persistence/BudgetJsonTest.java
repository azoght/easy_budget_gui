package persistence;

import model.Budget;
import model.BudgetItem;
import model.Category;
import model.exceptions.ZeroOrLessException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BudgetJsonTest {

    Budget testBudget;
    BudgetItem testBudgetItem1;
    BudgetItem testBudgetItem2;
    BudgetItem testBudgetItem3;

    @BeforeEach
    void setup() {
        try {
            testBudget = new Budget(1000);
        } catch (Exception e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        try {
            testBudgetItem1 = new BudgetItem(100, Category.OTHER);
            testBudgetItem2 = new BudgetItem(300, Category.FOOD);
            testBudgetItem3 = new BudgetItem(500, Category.HOUSING);
        } catch (ZeroOrLessException e) {
            fail();
        }
    }


    @Test
    void testWriteBudgetItemToJson() {
        try {
            PersistenceManager.write(testBudgetItem1, "budgetItem_t.json");
        } catch (Exception e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
    }

    @Test
    void testReadBudgetItemFromJson() {
        try {
            String contentString = PersistenceManager.read("budgetItem_t.json");
            BudgetItem testBudgetItem = BudgetItem.fromJson(new JSONObject(contentString));
            assert testBudgetItem != null;
            assertEquals(testBudgetItem1.getLimit(), testBudgetItem.getLimit());
            assertEquals(testBudgetItem1.getCategory(), testBudgetItem.getCategory());
        } catch (Exception e) {
            fail(e.getMessage() + "\nThis exception should not be thrown!");
        }
    }

    @Test
    void testWriteBudgetToJson() {
        try {
            testBudget.addBudgetItem(testBudgetItem1);
        } catch (Exception e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        try {
            testBudget.addBudgetItem(testBudgetItem2);
        } catch (Exception e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        try {
            testBudget.addBudgetItem(testBudgetItem3);
        } catch (Exception e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        try {
            PersistenceManager.write(testBudget, "budget_t.json");
        } catch (Exception e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
    }

    @Test
    void testReadBudgetFromJson() {
        try {
            String contentString = PersistenceManager.read("budget_t.json");
            double originalLimit = testBudget.getTotalLimit();
            testBudget = Budget.fromJson(new JSONObject(contentString));
            assertEquals(originalLimit, testBudget.getTotalLimit());
            assertEquals(3, testBudget.getItems().size());
            assertEquals(testBudgetItem1.getLimit(), testBudget.getItems().get(0).getLimit());
            assertEquals(testBudgetItem1.getCategory(), testBudget.getItems().get(0).getCategory());
            assertEquals(testBudgetItem2.getLimit(), testBudget.getItems().get(1).getLimit());
            assertEquals(testBudgetItem2.getCategory(), testBudget.getItems().get(1).getCategory());
            assertEquals(testBudgetItem3.getLimit(), testBudget.getItems().get(2).getLimit());
            assertEquals(testBudgetItem3.getCategory(), testBudget.getItems().get(2).getCategory());
        } catch (Exception e) {
            fail(e.getMessage() + "\nThis exception should not be thrown!");
        }
    }

    @Test
    void testLoadBudgetWithZeroLimitThrowsZeroOrLessException() {
        JSONObject obj = new JSONObject();
        obj.put("limit", 0);
        obj.put("items", new JSONArray());
        try {
            Budget.fromJson(obj);
            fail("A ZeroOrLessException should have been thrown!");
        } catch (ZeroOrLessException e) {
            //
        } catch (Exception e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
    }

    @Test
    void testLoadBudgetWithNegativeLimitThrowsZeroOrLessException() {
        JSONObject obj = new JSONObject();
        obj.put("limit", -100);
        obj.put("items", new JSONArray());
        try {
            Budget.fromJson(obj);
            fail("A ZeroOrLessException should have been thrown!");
        } catch (ZeroOrLessException e) {
            //
        } catch (Exception e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown");
        }
    }

    @Test
    void testLoadBudgetWithNoRoom() {
        JSONObject obj = new JSONObject();
        obj.put("limit", 100);
        JSONArray items = new JSONArray();
        JSONObject budgetItem1 = new JSONObject();
        budgetItem1.put("limit", 200);
        budgetItem1.put("category", "others");
        items.put(budgetItem1);
        obj.put("items", items);
        try {
            Budget budget = Budget.fromJson(obj);
            assertEquals(0, budget.getItems().size());
        } catch (Exception e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown");
        }
    }

    @Test
    void testLoadBudgetWithTwoOfSameCategoryThrowsCategoryExistsException() {
        JSONObject obj = new JSONObject();
        obj.put("limit", 800);
        JSONArray items = new JSONArray();
        JSONObject budgetItem1 = new JSONObject();
        budgetItem1.put("limit", 100);
        budgetItem1.put("category", "others");
        items.put(budgetItem1);
        JSONObject budgetItem2 = new JSONObject();
        budgetItem2.put("limit", 500);
        budgetItem2.put("category", "others");
        items.put(budgetItem2);
        obj.put("items", items);
        Budget budget = null;
        try {
            budget = Budget.fromJson(obj);
        } catch (Exception e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        assertEquals(1, budget.getItems().size());
    }
}
