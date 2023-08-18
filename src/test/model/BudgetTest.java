package model;

import model.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class BudgetTest {
    private Budget budget;

    @BeforeEach
    void setup() {
        try {
            budget = new Budget(1000);
        } catch (ZeroOrLessException e) {
            fail("Budget should have been constructed!");
        }
    }

    @Test
    void testBudgetDesigner() {
        BudgetDesigner testBudgetDesigner = new BudgetDesigner();
        budget = testBudgetDesigner.createBudget(500);
        assertEquals(500, budget.getTotalLimit());
        assertEquals(0, budget.getItems().size());
    }

    @Test
    void testBudgetDesignerZero() {
        BudgetDesigner testBudgetDesigner = new BudgetDesigner();
        budget = testBudgetDesigner.createBudget(0);
        assertNull(budget);
    }

    @Test
    void testBudgetDesignerLessThanZero() {
        BudgetDesigner testBudgetDesigner = new BudgetDesigner();
        budget = testBudgetDesigner.createBudget(-100);
        assertNull(budget);
    }

    @Test
    void testCreateBudgetItemZeroLimitThrowsZeroOrLessException() {
        try {
            new BudgetItem(0, Category.OTHER);
            fail();
        } catch (ZeroOrLessException e) {
            //
        }
    }

    @Test
    void testCreateBudgetItemNegativeLimitThrowsZeroOrLessException() {
        try {
            new BudgetItem(-100, Category.OTHER);
            fail();
        } catch (ZeroOrLessException e) {
            //
        }
    }

    @Test
    void testConstructor() {
        assertEquals(1000, budget.getTotalLimit());
        assertEquals(0, budget.getItems().size());
    }

    @Test
    void testConstructorZeroInitialBalanceThrowsNegativeAmountException() {
        try {
            budget = new Budget(0);
            fail("Budget should have not been constructed!");
        } catch (ZeroOrLessException e) {
            //
        }
    }

    @Test
    void testConstructorNegativeBalanceThrowsNegativeAmountException() {
        try {
            budget = new Budget(-100);
            fail("Budget should have not been constructed!");
        } catch (ZeroOrLessException e) {
            assertEquals("Invalid: amount is less than or equal to 0!", e.getMessage());
        }
    }

    @Test
    void testSetTotalLimit() {
        try {
            budget.setTotalLimit(2000);
        } catch (Exception e) {
            fail("New total limit should have been set!");
        }
        assertEquals(2000, budget.getTotalLimit());
    }

    @Test
    void testSetTotalLimitToZeroThrowsZeroOrLessException() {
        try {
            budget.setTotalLimit(0);
            fail("New total limit should not have been set!");
        } catch (ZeroOrLessException e) {
            assertEquals("Invalid: amount is less than or equal to 0!", e.getMessage());
        } catch (CannotChangeBudgetException e) {
            fail("This exception should not have been thrown!");
        }
    }

    @Test
    void testSetTotalLimitToLessThanSumOfLimits() {
        testAddBudgetItemOnce();
        try {
            budget.setTotalLimit(budget.sumOfLimits() - 1);
            fail("New total limit should not have been set!");
        } catch (CannotChangeBudgetException e) {
            //
        } catch (ZeroOrLessException e) {
            fail("This exception should not have been thrown!");
        }
    }

    @Test
    void testAddBudgetItemOnce() {
        BudgetItem budgetItem = null;
        try {
            budgetItem = new BudgetItem(100, Category.OTHER);
        } catch (ZeroOrLessException e) {
            fail();
        }
        try {
            budget.addBudgetItem(budgetItem.getLimit(), budgetItem.getCategory());
        } catch (ZeroOrLessException | CannotChangeBudgetException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        BudgetItem lastItemAddedToBudget = budget.getItems().get(budget.getItems().size() - 1);
        assertEquals(lastItemAddedToBudget.getLimit(), budgetItem.getLimit());
        assertEquals(lastItemAddedToBudget.getCategory(), budgetItem.getCategory());
    }

    @Test
    void testAddBudgetItemMultipleTimes() {
        testAddBudgetItemOnce();
        try {
            BudgetItem anotherBudgetItem = new BudgetItem(200, Category.PERSONAL_CARE);
            budget.addBudgetItem(anotherBudgetItem.getLimit(), anotherBudgetItem.getCategory());
            assertEquals(2, budget.getItems().size());
            BudgetItem lastItemAddedToBudget = budget.getItems().get(budget.getItems().size() - 1);
            assertEquals(lastItemAddedToBudget.getLimit(), anotherBudgetItem.getLimit());
            assertEquals(lastItemAddedToBudget.getCategory(), anotherBudgetItem.getCategory());
        } catch (ZeroOrLessException | CannotChangeBudgetException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
    }

    @Test
    void testAddBudgetItemNegativeLimitThrowsZeroOrLessException() {
        try {
            BudgetItem budgetItem = new BudgetItem(-100, Category.OTHER);
            budget.addBudgetItem(budgetItem.getLimit(), budgetItem.getCategory());
            fail("A ZeroOrLessException should have been thrown!");
        } catch (ZeroOrLessException e) {
            assertEquals("Invalid: amount is less than or equal to 0!", e.getMessage());
        } catch (CannotChangeBudgetException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        assertEquals(0, budget.getItems().size());
    }

    @Test
    void testAddFullBudgetItemNegativeLimitThrowsZeroOrLessException() {
        try {
            BudgetItem budgetItem = new BudgetItem(-100, Category.OTHER);
            budget.addBudgetItem(budgetItem);
            fail("A ZeroOrLessException should have been thrown!");
        } catch (ZeroOrLessException e) {
            assertEquals("Invalid: amount is less than or equal to 0!", e.getMessage());
        } catch (CannotChangeBudgetException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        assertEquals(0, budget.getItems().size());
    }

    @Test
    void testAddBudgetItemCategoryExistsThrowsCategoryExistsException() {
        testAddBudgetItemOnce();
        try {
            BudgetItem budgetItem2 = new BudgetItem(400, Category.OTHER);
            budget.addBudgetItem(budgetItem2.getLimit(), budgetItem2.getCategory());
            fail("CategoryExistsException should have been thrown!");
        } catch (CategoryExistsException e) {
            assertEquals("The category \"Others\" already exists in the budget!", e.getMessage());
        } catch (CannotChangeBudgetException | ZeroOrLessException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        assertEquals(1, budget.getItems().size());
    }

    @Test
    void testAddBudgetItemWithLimitOfLeftOver() {
        BudgetItem budgetItem = null;
        try {
            budgetItem = new BudgetItem(budget.getTotalLimit(), Category.OTHER);
        } catch (ZeroOrLessException e) {
            fail();
        }
        try {
            budget.addBudgetItem(budgetItem.getLimit(), budgetItem.getCategory());
        } catch (ZeroOrLessException | CannotChangeBudgetException e) {
            fail("An exception should not have been thrown!");
        }
        assertEquals(1, budget.getItems().size());
    }

    @Test
    void testAddBudgetItemWithLimitOfMoreThanLeftOverThrowsNoRoomForCategoryException() {
        BudgetItem budgetItem = null;
        try {
            budgetItem = new BudgetItem(budget.getTotalLimit() + 10, Category.OTHER);
        } catch (ZeroOrLessException e) {
            fail();
        }
        try {
            budget.addBudgetItem(budgetItem.getLimit(), budgetItem.getCategory());
            fail("NoRoomForCategoryException should have been thrown!");
        } catch (NoRoomForCategoryException e) {
            assertEquals("The limit of this category will not fit the budget!", e.getMessage());
        } catch (ZeroOrLessException | CannotChangeBudgetException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        assertEquals(0, budget.getItems().size());
    }

    @Test
    void testEditLimitOnce() {
        testAddBudgetItemOnce();
        try {
            budget.editLimitOf(Category.OTHER, 200);
        } catch (ZeroOrLessException | CannotChangeBudgetException e) {
            fail(e.getMessage() + "\n This exception should not have been thrown!");
        }
        BudgetItem lastItemAddedToBudget = budget.getItems().get(budget.getItems().size() - 1);
        assertEquals(200, lastItemAddedToBudget.getLimit());
    }

    @Test
    void testEditLimitMultipleTimes() {
        testAddBudgetItemOnce();
        try {
            budget.editLimitOf(Category.OTHER, 200);
        } catch (ZeroOrLessException | CannotChangeBudgetException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        BudgetItem lastItemAddedToBudget = budget.getItems().get(budget.getItems().size() - 1);
        assertEquals(200, lastItemAddedToBudget.getLimit());
        try {
            budget.editLimitOf(Category.OTHER, 300);
        } catch (ZeroOrLessException | CannotChangeBudgetException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        lastItemAddedToBudget = budget.getItems().get(budget.getItems().size() - 1);
        assertEquals(300, lastItemAddedToBudget.getLimit());
    }

    @Test
    void testEditLimitZeroLimitThrowsNegativeAmountException() {
        testAddBudgetItemOnce();
        try {
            budget.editLimitOf(Category.OTHER, 0);
        } catch (ZeroOrLessException e) {
            assertEquals("Invalid: amount is less than or equal to 0!", e.getMessage());
        } catch (CannotChangeBudgetException e) {
            fail(e.getMessage() + "\nThis exception should not have been added!");
        }
        BudgetItem lastItemAddedToBudget = budget.getItems().get(budget.getItems().size() - 1);
        assertEquals(100, lastItemAddedToBudget.getLimit());
    }

    @Test
    void testEditLimitNegativeThrowsNegativeAmountException() {
        testAddBudgetItemOnce();
        try {
            budget.editLimitOf(Category.OTHER, -200);
        } catch (ZeroOrLessException e) {
            assertEquals("Invalid: amount is less than or equal to 0!", e.getMessage());
        } catch (CannotChangeBudgetException e) {
            fail(e.getMessage() + "\nThis exception should not have been added!");
        }
        BudgetItem lastItemAddedToBudget = budget.getItems().get(budget.getItems().size() - 1);
        assertEquals(100, lastItemAddedToBudget.getLimit());
    }

    @Test
    void testEditLimitCategoryDoesNotExistThrowsCategoryDoesNotExistException() {
        testAddBudgetItemOnce();
        try {
            budget.editLimitOf(Category.PERSONAL_CARE, 200);
            fail("A CategoryDoesNotExistException should have been thrown!");
        } catch (CategoryDoesNotExistException e) {
            assertEquals("The category \"Personal Care\" does not exist in the budget!", e.getMessage());
        } catch (ZeroOrLessException | CannotChangeBudgetException e) {
            fail(e.getMessage() + "\nThis exception should have not been thrown!");
        }
        BudgetItem lastItemAddedToBudget = budget.getItems().get(budget.getItems().size() - 1);
        assertEquals(100, lastItemAddedToBudget.getLimit());
    }

    @Test
    void testEditLimitExactlyLeftOver() {
        testAddBudgetItemOnce();
        double newLimit = budget.getTotalLimit() - budget.sumOfLimits();
        try {
            budget.editLimitOf(Category.OTHER, newLimit);
        } catch (ZeroOrLessException | CannotChangeBudgetException e) {
            fail(e.getMessage() + "\nThis exception should have not been thrown!");
        }
        BudgetItem lastItemAddedToBudget = budget.getItems().get(budget.getItems().size() - 1);
        assertEquals(newLimit, lastItemAddedToBudget.getLimit());
    }

    @Test
    void testEditLimitMoreThanLeftOverThrowsNoRoomForCategoryException() {
        testAddBudgetItemOnce();
        double newLimit = (budget.getTotalLimit() - budget.sumOfLimitsMinusCategory(Category.OTHER)) + 10;
        try {
            budget.editLimitOf(Category.OTHER, newLimit);
        } catch (NoRoomForCategoryException e) {
            assertEquals("The limit of this category will not fit the budget!", e.getMessage());
        } catch (ZeroOrLessException | CannotChangeBudgetException e) {
            fail(e.getMessage() + "\nThis exception should have not been thrown!");
        }
        BudgetItem lastItemAddedToBudget = budget.getItems().get(budget.getItems().size() - 1);
        assertEquals(100, lastItemAddedToBudget.getLimit());
    }

    @Test
    void testDeleteBudgetItemOnce() {
        testAddBudgetItemOnce();
        try {
            budget.deleteBudgetItem(Category.OTHER);
        } catch (CategoryDoesNotExistException e) {
            fail("An exception should not have been thrown!");
        }
        assertEquals(0, budget.getItems().size());
    }

    @Test
    void testDeleteBudgetItemMultipleTimes() {
        testAddBudgetItemMultipleTimes();
        try {
            budget.deleteBudgetItem(Category.OTHER);
        } catch (CategoryDoesNotExistException e) {
            fail("An exception should not have been thrown!");
        }
        assertEquals(1, budget.getItems().size());
        try {
            budget.deleteBudgetItem(Category.PERSONAL_CARE);
        } catch (CategoryDoesNotExistException e) {
            fail("An exception should not have been thrown!");
        }
        assertEquals(0, budget.getItems().size());
    }

    @Test
    void testDeleteBudgetItemCategoryDoesNotExist() {
        testAddBudgetItemOnce();
        try {
            budget.deleteBudgetItem(Category.PERSONAL_CARE);
            fail("A CategoryDoesNotExistException should have been thrown!");
        } catch (CategoryDoesNotExistException e) {
            assertEquals("The category \"Personal Care\" does not exist in the budget!", e.getMessage());
        }
        assertEquals(1, budget.getItems().size());
    }

    @Test
    void testCompareToBudgetUnderBudget() {
        BudgetItem budgetItem = null;
        try {
            budgetItem = new BudgetItem(100, Category.TRANSPORTATION);
        } catch (ZeroOrLessException e) {
            fail();
        }
        try {
            budget.addBudgetItem(budgetItem.getLimit(), budgetItem.getCategory());
        } catch (ZeroOrLessException | CannotChangeBudgetException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        SpendingTracker tracker = new SpendingTracker();
        try {
            tracker.addExpense(60, " ", " ",
                    LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), 1),
                    Category.TRANSPORTATION);
        } catch (ZeroOrLessException | ZeroLengthException | NullPointerException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        int compareInteger = 5;
        try {
            compareInteger = budget.compareToBudget(tracker, Category.TRANSPORTATION);
        } catch (CategoryDoesNotExistException | NoExpenseOfCategoryException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        assertEquals(-1, compareInteger);
    }

    @Test
    void testCompareToBudgetExactlyBudget(){
        BudgetItem budgetItem = null;
        try {
            budgetItem = new BudgetItem(100, Category.TRANSPORTATION);
        } catch (ZeroOrLessException e) {
            fail();
        }
        try {
            budget.addBudgetItem(budgetItem.getLimit(), budgetItem.getCategory());
        } catch (ZeroOrLessException | CannotChangeBudgetException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        SpendingTracker tracker = new SpendingTracker();
        try {
            tracker.addExpense(100, " ", " ",
                    LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), 1),
                    Category.TRANSPORTATION);
        } catch (ZeroOrLessException | ZeroLengthException | NullPointerException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        int compareInteger = 5;
        try {
            compareInteger = budget.compareToBudget(tracker, Category.TRANSPORTATION);
        } catch (CategoryDoesNotExistException | NoExpenseOfCategoryException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        assertEquals(0, compareInteger);
    }

    @Test
    void testCompareToBudgetOverBudget(){
        BudgetItem budgetItem = null;
        try {
            budgetItem = new BudgetItem(100, Category.TRANSPORTATION);
        } catch (ZeroOrLessException e) {
            fail();
        }
        try {
            budget.addBudgetItem(budgetItem.getLimit(), budgetItem.getCategory());
        } catch (ZeroOrLessException | CannotChangeBudgetException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        SpendingTracker tracker = new SpendingTracker();
        try {
            tracker.addExpense(110, " ", " ",
                    LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), 1),
                    Category.TRANSPORTATION);
        } catch (ZeroOrLessException | ZeroLengthException | NullPointerException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        int compareInteger = 5;
        try {
            compareInteger = budget.compareToBudget(tracker, Category.TRANSPORTATION);
        } catch (CategoryDoesNotExistException | NoExpenseOfCategoryException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        assertEquals(1, compareInteger);
    }

    @Test
    void testCompareToBudgetCategoryDoesNotExistInBudgetThrowsCategoryDoesNotExistException(){
        BudgetItem budgetItem = null;
        try {
            budgetItem = new BudgetItem(100, Category.OTHER);
        } catch (ZeroOrLessException e) {
            fail();
        }
        try {
            budget.addBudgetItem(budgetItem.getLimit(), budgetItem.getCategory());
        } catch (ZeroOrLessException | CannotChangeBudgetException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        SpendingTracker tracker = new SpendingTracker();
        try {
            tracker.addExpense(110, " ", " ",
                    LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), 1),
                    Category.TRANSPORTATION);
        } catch (ZeroOrLessException | ZeroLengthException | NullPointerException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        int compareInteger = 5;
        try {
            compareInteger = budget.compareToBudget(tracker, Category.TRANSPORTATION);
            fail("A CategoryDoesNotException should have been thrown!");
        } catch (CategoryDoesNotExistException e) {
            assertEquals("The category \"Transportation\" does not exist in the budget!", e.getMessage());
        } catch (NoExpenseOfCategoryException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        assertEquals(5, compareInteger);
    }

    @Test
    void testCompareToBudgetCategoryDoesNotExistInExpenses(){
        BudgetItem budgetItem = null;
        try {
            budgetItem = new BudgetItem(100, Category.OTHER);
        } catch (ZeroOrLessException e) {
            fail();
        }
        try {
            budget.addBudgetItem(budgetItem.getLimit(), budgetItem.getCategory());
        } catch (ZeroOrLessException | CannotChangeBudgetException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        SpendingTracker tracker = new SpendingTracker();
        try {
            tracker.addExpense(110, " ", " ",
                    LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), 1),
                    Category.TRANSPORTATION);
        } catch (ZeroOrLessException | ZeroLengthException | NullPointerException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        int compareInteger = 5;
        try {
            compareInteger = budget.compareToBudget(tracker, Category.OTHER);
            fail("A NoExpenseOfCategoryException should have been thrown!");
        } catch (NoExpenseOfCategoryException e) {
            assertEquals("No expense of category \"Others\" in tracker!", e.getMessage());
        } catch (CategoryDoesNotExistException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        assertEquals(5, compareInteger);
    }

    @Test
    void testSumOfLimits() {
        testAddBudgetItemMultipleTimes();
        assertEquals(budget.getItems().get(0).getLimit() + budget.getItems().get(1).getLimit(),
                budget.sumOfLimits());
    }

    @Test
    void testIterator() {
        Iterator<BudgetItem> iterator = budget.iterator();
        assertFalse(iterator.hasNext());
        try {
            iterator.next();
            fail("A NoSuchElementException should have been thrown!");
        } catch (NoSuchElementException e) {
            //
        }
    }

    @Test
    void testStringToCategory() {
        assertNull(Category.stringToCategory("category"));
    }

}