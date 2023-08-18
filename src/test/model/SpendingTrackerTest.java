package model;

import model.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class SpendingTrackerTest {

    SpendingTracker testSpendingTracker;

    @BeforeEach
    void setup() {
        testSpendingTracker = new SpendingTracker();
    }

    @Test
    void testConstructor() {
        assertEquals(0, testSpendingTracker.getExpenses().size());
    }

    @Test
    void testAddExpenseOnce() {
        try {
            testSpendingTracker.addExpense(100, " ", " ",
                    LocalDate.of(2023, 7, 7), Category.OTHER);
        } catch (ZeroOrLessException | ZeroLengthException | NullPointerException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        assertEquals(1, testSpendingTracker.getExpenses().size());
    }

    @Test
    void testAddExpenseMultipleTimes() {
        testAddExpenseOnce();
        try {
            testSpendingTracker.addExpense(50, " ", " ",
                    LocalDate.of(2023, 8, 8), Category.PERSONAL_CARE);
        } catch (ZeroOrLessException | ZeroLengthException | NullPointerException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        assertEquals(2, testSpendingTracker.getExpenses().size());
    }

    @Test
    void testAddExpenseMultipleTimesSame() {
        testAddExpenseOnce();
        try {
            testSpendingTracker.addExpense(100, " ", " ",
                    LocalDate.of(2023, 7, 7), Category.OTHER);
        } catch (ZeroOrLessException | ZeroLengthException | NullPointerException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        assertEquals(2, testSpendingTracker.getExpenses().size());
    }

    @Test
    void testAddExpenseZeroPriceThrowsZeroOrLessException() {
        try {
            testSpendingTracker.addExpense(0, " ", " ",
                    LocalDate.of(2023, 7, 7), Category.OTHER);
            fail("A ZeroOrLessException should have been thrown!");
        } catch (ZeroOrLessException e) {
            assertEquals("Invalid: amount is less than or equal to 0!", e.getMessage());
        } catch (ZeroLengthException | NullPointerException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
    }

    @Test
    void testAddExpenseNegativePriceThrowsZeroOrLessException() {
        try {
            testSpendingTracker.addExpense(-100, " ", " ",
                    LocalDate.of(2023, 7, 7), Category.OTHER);
            fail("A ZeroOrLessException should have been thrown!");
        } catch (ZeroOrLessException e) {
            assertEquals("Invalid: amount is less than or equal to 0!", e.getMessage());
        } catch (ZeroLengthException | NullPointerException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
    }

    @Test
    void testAddExpenseEmptyDescriptionThrowsZeroLengthException() {
        try {
            testSpendingTracker.addExpense(100, "", " ",
                    LocalDate.of(2023, 7, 7), Category.OTHER);
            fail("A DescriptionZeroLengthException should have been thrown!");
        } catch(DescriptionZeroLengthException e) {
            assertEquals("Description is empty!", e.getMessage());
        } catch (ZeroOrLessException | ZeroLengthException | NullPointerException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
    }

    @Test
    void testAddExpenseEmptyVendorThrowsZeroLengthException() {
        try {
            testSpendingTracker.addExpense(100, " ", "",
                    LocalDate.of(2023, 7, 7), Category.OTHER);
            fail("A DescriptionZeroLengthException should have been thrown!");
        } catch (VendorZeroLengthException e) {
            assertEquals("Vendor is empty!", e.getMessage());
        } catch (ZeroOrLessException | ZeroLengthException | NullPointerException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
    }

    @Test
    void testAddExpenseNullDateThrowsNullPointerException() {
        try {
            testSpendingTracker.addExpense(100, " ", " ", null, Category.OTHER);
            fail("A DateNullException should have been thrown!");
        } catch (DateNullException e) {
            assertEquals("Date is null!", e.getMessage());
        } catch (ZeroOrLessException | ZeroLengthException | NullPointerException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
    }

    @Test
    void testAddExpenseNullCategoryThrowsNullPointerException() {
        try {
            testSpendingTracker.addExpense(100, " ", " ",
                    LocalDate.of(2023, 7, 7), null);
            fail("A CategoryNullException should have been thrown!");
        } catch (CategoryNullException e) {
            assertEquals("Category is null!", e.getMessage());
        } catch (ZeroOrLessException | ZeroLengthException | NullPointerException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
    }

    @Test
    void testDeleteExpenseOnce() {
        testAddExpenseOnce();
        String id = testSpendingTracker.getExpenses().get(0).getId();
        try {
            testSpendingTracker.deleteExpense(id);
        } catch (ExpenseDoesNotExistException e) {
            fail(e.getMessage() + "\nThis exception should not be thrown!");
        }
        assertEquals(0, testSpendingTracker.getExpenses().size());
    }

    @Test
    void testDeleteExpenseMultipleTimes() {
        testAddExpenseMultipleTimes();
        String id = testSpendingTracker.getExpenses().get(0).getId();
        try {
            testSpendingTracker.deleteExpense(id);
        } catch (ExpenseDoesNotExistException e) {
            fail(e.getMessage() + "\nThis exception should not be thrown!");
        }
        assertEquals(1, testSpendingTracker.getExpenses().size());
        id = testSpendingTracker.getExpenses().get(0).getId();
        try {
            testSpendingTracker.deleteExpense(id);
        } catch (ExpenseDoesNotExistException e) {
            fail(e.getMessage() + "\nThis exception should not be thrown!");
        }
        assertEquals(0, testSpendingTracker.getExpenses().size());
    }

    @Test
    void testDeleteExpenseDoesNotExist() {
        testAddExpenseOnce();
        String id = testSpendingTracker.getExpenses().get(0).getId();
        try {
            testSpendingTracker.deleteExpense(id);
        } catch (ExpenseDoesNotExistException e) {
            fail(e.getMessage() + "\nThis exception should not be thrown!");
        }
        assertEquals(0, testSpendingTracker.getExpenses().size());
        try {
            testSpendingTracker.deleteExpense(id);
            fail("An ExpenseDoesNotExistException should have been thrown!");
        } catch (ExpenseDoesNotExistException e) {
            assertEquals("The expense does not exist!", e.getMessage());
        }
    }

    @Test
    void testEditPriceOfOnce() {
        testAddExpenseOnce();
        String id = testSpendingTracker.getExpenses().get(0).getId();
        try {
            testSpendingTracker.editPriceOf(id, 200);
        } catch (ZeroOrLessException | ExpenseDoesNotExistException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        assertEquals(200, testSpendingTracker.getExpenses().get(0).getPrice());
    }

    @Test
    void testEditPriceOfMultipleTimes() {
        testEditPriceOfOnce();
        String id = testSpendingTracker.getExpenses().get(0).getId();
        try {
            testSpendingTracker.editPriceOf(id, 100);
        } catch (ZeroOrLessException | ExpenseDoesNotExistException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        assertEquals(100, testSpendingTracker.getExpenses().get(0).getPrice());
    }

    @Test
    void testEditPriceOfDoesNotExist() {
        testAddExpenseOnce();
        String id = testSpendingTracker.getExpenses().get(0).getId();
        try {
            testSpendingTracker.deleteExpense(id);
        } catch (ExpenseDoesNotExistException e) {
            fail(e.getMessage() + "\nThis exception should not be thrown!");
        }
        assertEquals(0, testSpendingTracker.getExpenses().size());
        try {
            testSpendingTracker.editPriceOf(id, 200);
            fail("An ExpenseDoesNotExistException should have been thrown!");
        } catch (ExpenseDoesNotExistException e) {
            assertEquals("The expense does not exist!", e.getMessage());
        } catch (ZeroOrLessException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
    }

    @Test
    void testEditPriceZeroThrowsZeroOrLessException() {
        testAddExpenseOnce();
        String id = testSpendingTracker.getExpenses().get(0).getId();
        try {
            testSpendingTracker.editPriceOf(id, 0);
            fail("An ZeroOrLessException should have been thrown!");
        } catch (ZeroOrLessException e) {
            assertEquals("Invalid: amount is less than or equal to 0!", e.getMessage());
        } catch (ExpenseDoesNotExistException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        assertNotEquals(0, testSpendingTracker.getExpenses().get(0).getPrice());
    }

    @Test
    void testEditPriceNegativeThrowsZeroOrLessException() {
        testAddExpenseOnce();
        String id = testSpendingTracker.getExpenses().get(0).getId();
        try {
            testSpendingTracker.editPriceOf(id, -100);
            fail("An ZeroOrLessException should have been thrown!");
        } catch (ZeroOrLessException e) {
            assertEquals("Invalid: amount is less than or equal to 0!", e.getMessage());
        } catch (ExpenseDoesNotExistException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        assertNotEquals(-100, testSpendingTracker.getExpenses().get(0).getPrice());
    }

    @Test
    void testEditPriceSame() {
        testAddExpenseOnce();
        String id = testSpendingTracker.getExpenses().get(0).getId();
        try {
            testSpendingTracker.editPriceOf(id, testSpendingTracker.getExpenses().get(0).getPrice());
        } catch (ZeroOrLessException | ExpenseDoesNotExistException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        assertEquals(100, testSpendingTracker.getExpenses().get(0).getPrice());
    }

    @Test
    void testEditDescriptionOfOnce() {
        testAddExpenseOnce();
        String id = testSpendingTracker.getExpenses().get(0).getId();
        try {
            testSpendingTracker.editDescriptionOf(id, "something");
        } catch (ZeroLengthException | ExpenseDoesNotExistException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        assertEquals("something", testSpendingTracker.getExpenses().get(0).getDescription());
    }

    @Test
    void testEditDescriptionOfMultipleTimes() {
        testEditDescriptionOfOnce();
        String id = testSpendingTracker.getExpenses().get(0).getId();
        try {
            testSpendingTracker.editDescriptionOf(id, " ");
        } catch (ZeroLengthException | ExpenseDoesNotExistException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        assertEquals(" ", testSpendingTracker.getExpenses().get(0).getDescription());
    }

    @Test
    void testEditDescriptionOfDoesNotExist() {
        testAddExpenseOnce();
        String id = testSpendingTracker.getExpenses().get(0).getId();
        try {
            testSpendingTracker.deleteExpense(id);
        } catch (ExpenseDoesNotExistException e) {
            fail(e.getMessage() + "\nThis exception should not be thrown!");
        }
        assertEquals(0, testSpendingTracker.getExpenses().size());
        try {
            testSpendingTracker.editDescriptionOf(id, "something");
            fail("An ExpenseDoesNotExistException should have been thrown!");
        } catch (ExpenseDoesNotExistException e) {
            assertEquals("The expense does not exist!", e.getMessage());
        } catch (ZeroLengthException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
    }

    @Test
    void testEditDescriptionOfEmptyThrowsZeroLengthException() {
        testAddExpenseOnce();
        String id = testSpendingTracker.getExpenses().get(0).getId();
        try {
            testSpendingTracker.editDescriptionOf(id, "");
            fail("A DescriptionZeroLengthException should have been thrown!");
        } catch (DescriptionZeroLengthException e) {
            assertEquals("Description is empty!", e.getMessage());
        } catch (ExpenseDoesNotExistException | ZeroLengthException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        assertNotEquals("", testSpendingTracker.getExpenses().get(0).getDescription());
    }

    @Test
    void testEditDescriptionSame() {
        testAddExpenseOnce();
        String id = testSpendingTracker.getExpenses().get(0).getId();
        try {
            testSpendingTracker.editDescriptionOf(id, testSpendingTracker.getExpenses().get(0).getDescription());
        } catch (ZeroLengthException | ExpenseDoesNotExistException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        assertEquals(" ", testSpendingTracker.getExpenses().get(0).getDescription());
    }

    @Test
    void testEditVendorOfOnce() {
        testAddExpenseOnce();
        String id = testSpendingTracker.getExpenses().get(0).getId();
        try {
            testSpendingTracker.editVendorOf(id, "IKEA");
        } catch (ZeroLengthException | ExpenseDoesNotExistException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        assertEquals("IKEA", testSpendingTracker.getExpenses().get(0).getVendor());
    }

    @Test
    void testEditVendorOfMultipleTimes() {
        testEditVendorOfOnce();
        String id = testSpendingTracker.getExpenses().get(0).getId();
        try {
            testSpendingTracker.editVendorOf(id, " ");
        } catch (ZeroLengthException | ExpenseDoesNotExistException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        assertEquals(" ", testSpendingTracker.getExpenses().get(0).getVendor());
    }

    @Test
    void testEditVendorOfDoesNotExist() {
        testAddExpenseOnce();
        String id = testSpendingTracker.getExpenses().get(0).getId();
        try {
            testSpendingTracker.deleteExpense(id);
        } catch (ExpenseDoesNotExistException e) {
            fail(e.getMessage() + "\nThis exception should not be thrown!");
        }
        assertEquals(0, testSpendingTracker.getExpenses().size());
        try {
            testSpendingTracker.editVendorOf(id, "IKEA");
            fail("An ExpenseDoesNotExistException should have been thrown!");
        } catch (ExpenseDoesNotExistException e) {
            assertEquals("The expense does not exist!", e.getMessage());
        } catch (ZeroLengthException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        assertEquals(0, testSpendingTracker.getExpenses().size());
    }

    @Test
    void testEditVendorOfEmptyThrowsZeroLengthException() {
        testAddExpenseOnce();
        String id = testSpendingTracker.getExpenses().get(0).getId();
        try {
            testSpendingTracker.editVendorOf(id, "");
            fail("A VendorZeroLengthException should have been thrown!");
        } catch (VendorZeroLengthException e) {
            assertEquals("Vendor is empty!", e.getMessage());
        } catch (ExpenseDoesNotExistException | ZeroLengthException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        assertNotEquals("", testSpendingTracker.getExpenses().get(0).getVendor());
    }

    @Test
    void testEditVendorOfSame() {
        testAddExpenseOnce();
        String id = testSpendingTracker.getExpenses().get(0).getId();
        try {
            testSpendingTracker.editVendorOf(id, testSpendingTracker.getExpenses().get(0).getVendor());
        } catch (ZeroLengthException | ExpenseDoesNotExistException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        assertEquals(" ", testSpendingTracker.getExpenses().get(0).getVendor());
    }

    @Test
    void testEditDateOfOnce() {
        testAddExpenseOnce();
        String id = testSpendingTracker.getExpenses().get(0).getId();
        LocalDate newDate = LocalDate.of(2023, 6, 6);
        try {
            testSpendingTracker.editDateOf(id, newDate);
        } catch (ExpenseDoesNotExistException | NullPointerException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        assertEquals(newDate, testSpendingTracker.getExpenses().get(0).getDate());
    }

    @Test
    void testEditDateOfMultipleTimes() {
        testEditDateOfOnce();
        String id = testSpendingTracker.getExpenses().get(0).getId();
        LocalDate newDate = LocalDate.of(2023, 7, 14);
        try {
            testSpendingTracker.editDateOf(id, newDate);
        } catch (ExpenseDoesNotExistException | NullPointerException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        assertEquals(newDate, testSpendingTracker.getExpenses().get(0).getDate());
    }

    @Test
    void testEditDateOfDoesNotExist() {
        testAddExpenseOnce();
        String id = testSpendingTracker.getExpenses().get(0).getId();
        try {
            testSpendingTracker.deleteExpense(id);
        } catch (ExpenseDoesNotExistException e) {
            fail(e.getMessage() + "\nThis exception should not be thrown!");
        }
        assertEquals(0, testSpendingTracker.getExpenses().size());
        LocalDate newDate = LocalDate.of(2023, 6, 6);
        try {
            testSpendingTracker.editDateOf(id, newDate);
            fail("An ExpenseDoesNotExistException should have been thrown!");
        } catch (ExpenseDoesNotExistException e) {
            assertEquals("The expense does not exist!", e.getMessage());
        } catch (NullPointerException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
    }

    @Test
    void testEditDateOfNullThrowsNullPointerException() {
        testAddExpenseOnce();
        String id = testSpendingTracker.getExpenses().get(0).getId();
        try {
            testSpendingTracker.editDateOf(id, null);
            fail("A DateNullException should have been thrown!");
        } catch (DateNullException e) {
            assertEquals("Date is null!", e.getMessage());
        } catch (NullPointerException | ExpenseDoesNotExistException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        assertNotNull(testSpendingTracker.getExpenses().get(0).getDate());
    }

    @Test
    void testEditDateOfSame() {
        testAddExpenseOnce();
        String id = testSpendingTracker.getExpenses().get(0).getId();
        LocalDate newDate = LocalDate.of(2023, 7, 7);
        try {
            testSpendingTracker.editDateOf(id, testSpendingTracker.getExpenses().get(0).getDate());
        } catch (ExpenseDoesNotExistException | NullPointerException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        assertEquals(newDate, testSpendingTracker.getExpenses().get(0).getDate());
    }

    @Test
    void testEditCategoryOfOnce() {
        testAddExpenseOnce();
        String id = testSpendingTracker.getExpenses().get(0).getId();
        try {
            testSpendingTracker.editCategoryOf(id, Category.DEBT);
        } catch (ExpenseDoesNotExistException | NullPointerException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        assertEquals(Category.DEBT, testSpendingTracker.getExpenses().get(0).getCategory());
    }

    @Test
    void testEditCategoryOfMultipleTimes() {
        testEditCategoryOfOnce();
        String id = testSpendingTracker.getExpenses().get(0).getId();
        try {
            testSpendingTracker.editCategoryOf(id, Category.OTHER);
        } catch (ExpenseDoesNotExistException | NullPointerException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        assertEquals(Category.OTHER, testSpendingTracker.getExpenses().get(0).getCategory());
    }

    @Test
    void testEditCategoryOfDoesNotExist() {
        testAddExpenseOnce();
        String id = testSpendingTracker.getExpenses().get(0).getId();
        try {
            testSpendingTracker.deleteExpense(id);
        } catch (ExpenseDoesNotExistException e) {
            fail(e.getMessage() + "\nThis exception should not be thrown!");
        }
        assertEquals(0, testSpendingTracker.getExpenses().size());
        try {
            testSpendingTracker.editCategoryOf(id, Category.DEBT);
        } catch (ExpenseDoesNotExistException e) {
            assertEquals("The expense does not exist!", e.getMessage());
        } catch (NullPointerException e) {
            fail(e.getMessage() + "\nThis exception should not be thrown!");
        }
        assertEquals(0, testSpendingTracker.getExpenses().size());
    }

    @Test
    void testEditCategoryOfNullThrowsNullPointerException() {
        testAddExpenseOnce();
        String id = testSpendingTracker.getExpenses().get(0).getId();
        try {
            testSpendingTracker.editCategoryOf(id, null);
        } catch (CategoryNullException e) {
            assertEquals("Category is null!", e.getMessage());
        } catch (NullPointerException | ExpenseDoesNotExistException e) {
            fail(e.getMessage() + "\nThis exception should not be thrown!");
        }
        assertNotNull(testSpendingTracker.getExpenses().get(0).getCategory());
    }

    @Test
    void testEditCategoryOfSame() {
        testAddExpenseOnce();
        String id = testSpendingTracker.getExpenses().get(0).getId();
        try {
            testSpendingTracker.editCategoryOf(id, testSpendingTracker.getExpenses().get(0).getCategory());
        } catch (ExpenseDoesNotExistException | NullPointerException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        assertEquals(Category.OTHER, testSpendingTracker.getExpenses().get(0).getCategory());
    }

    @Test
    void testFilterEmpty() {
        testAddExpenseOnce();
        List<ExpenseFilter> filters = new ArrayList<>();
        List<Expense> filteredExpenses = testSpendingTracker.filter(filters);
        assertEquals(1, filteredExpenses.size());
        assertEquals(testSpendingTracker.getExpenses().get(0), filteredExpenses.get(0));
    }

    @Test
    void testFilterByDateStartDateEndDate() {
        testAddExpenseOnce();
        try {
            testSpendingTracker.addExpense(30, " ", " ",
                    LocalDate.of(2023, 7, 14), Category.FOOD);
        } catch (ZeroOrLessException | ZeroLengthException | NullPointerException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        assertEquals(2, testSpendingTracker.getExpenses().size());
        try {
            testSpendingTracker.addExpense(30, " ", " ",
                    LocalDate.of(2023, 9, 24), Category.FOOD);
        } catch (ZeroOrLessException | ZeroLengthException | NullPointerException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        assertEquals(3, testSpendingTracker.getExpenses().size());
        List<ExpenseFilter> filters = new ArrayList<>();
        filters.add(new FilterByDate(LocalDate.of(2023, 7, 1),
                LocalDate.of(2023, 7, 31)));
        List<Expense> filteredExpenses = testSpendingTracker.filter(filters);
        assertEquals(2, filteredExpenses.size());
        assertEquals(testSpendingTracker.getExpenses().get(0), filteredExpenses.get(0));
        assertEquals(testSpendingTracker.getExpenses().get(1), filteredExpenses.get(1));
    }

    @Test
    void testFilterByDateMonthYear() {
        testAddExpenseOnce();
        try {
            testSpendingTracker.addExpense(30, " ", " ",
                    LocalDate.of(2023, 7, 14), Category.FOOD);
        } catch (ZeroOrLessException | ZeroLengthException | NullPointerException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        assertEquals(2, testSpendingTracker.getExpenses().size());
        try {
            testSpendingTracker.addExpense(30, " ", " ",
                    LocalDate.of(2023, 9, 24), Category.FOOD);
        } catch (ZeroOrLessException | ZeroLengthException | NullPointerException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        assertEquals(3, testSpendingTracker.getExpenses().size());
        List<ExpenseFilter> filters = new ArrayList<>();
        filters.add(new FilterByDate(Month.JULY, Year.of(2023)));
        List<Expense> filteredExpenses = testSpendingTracker.filter(filters);
        assertEquals(2, filteredExpenses.size());
        assertEquals(testSpendingTracker.getExpenses().get(0), filteredExpenses.get(0));
        assertEquals(testSpendingTracker.getExpenses().get(1), filteredExpenses.get(1));
    }

    @Test
    void testFilterByDateMonthOfCurrentYear() {
        testAddExpenseOnce();
        try {
            testSpendingTracker.addExpense(30, " ", " ",
                    LocalDate.of(2023, 7, 14), Category.FOOD);
        } catch (ZeroOrLessException | ZeroLengthException | NullPointerException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        assertEquals(2, testSpendingTracker.getExpenses().size());
        try {
            testSpendingTracker.addExpense(30, " ", " ",
                    LocalDate.of(2023, 9, 24), Category.FOOD);
        } catch (ZeroOrLessException | ZeroLengthException | NullPointerException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        assertEquals(3, testSpendingTracker.getExpenses().size());
        List<ExpenseFilter> filters = new ArrayList<>();
        filters.add(new FilterByDate(Month.JULY));
        List<Expense> filteredExpenses = testSpendingTracker.filter(filters);
        assertEquals(2, filteredExpenses.size());
        assertEquals(testSpendingTracker.getExpenses().get(0), filteredExpenses.get(0));
        assertEquals(testSpendingTracker.getExpenses().get(1), filteredExpenses.get(1));
    }

    @Test
    void testFilterByDateSetStartDate() {
        testAddExpenseOnce();
        try {
            testSpendingTracker.addExpense(30, " ", " ",
                    LocalDate.of(2023, 7, 14), Category.FOOD);
        } catch (ZeroOrLessException | ZeroLengthException | NullPointerException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        assertEquals(2, testSpendingTracker.getExpenses().size());
        try {
            testSpendingTracker.addExpense(30, " ", " ",
                    LocalDate.of(2023, 9, 24), Category.FOOD);
        } catch (ZeroOrLessException | ZeroLengthException | NullPointerException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        assertEquals(3, testSpendingTracker.getExpenses().size());
        List<ExpenseFilter> filters = new ArrayList<>();
        FilterByDate dateFilter = new FilterByDate(Month.JULY);
        dateFilter.setStartDate(LocalDate.of(2023, 7, 9));
        filters.add(dateFilter);
        List<Expense> filteredExpenses = testSpendingTracker.filter(filters);
        assertEquals(1, filteredExpenses.size());
        assertEquals(testSpendingTracker.getExpenses().get(1), filteredExpenses.get(0));
    }

    @Test
    void testFilterByDateSetEndDate() {
        testAddExpenseOnce();
        try {
            testSpendingTracker.addExpense(30, " ", " ",
                    LocalDate.of(2023, 7, 14), Category.FOOD);
        } catch (ZeroOrLessException | ZeroLengthException | NullPointerException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        assertEquals(2, testSpendingTracker.getExpenses().size());
        try {
            testSpendingTracker.addExpense(30, " ", " ",
                    LocalDate.of(2023, 9, 24), Category.FOOD);
        } catch (ZeroOrLessException | ZeroLengthException | NullPointerException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        assertEquals(3, testSpendingTracker.getExpenses().size());
        List<ExpenseFilter> filters = new ArrayList<>();
        FilterByDate dateFilter = new FilterByDate(Month.JULY);
        dateFilter.setEndDate(LocalDate.of(2023, 7, 12));
        filters.add(dateFilter);
        List<Expense> filteredExpenses = testSpendingTracker.filter(filters);
        assertEquals(1, filteredExpenses.size());
        assertEquals(testSpendingTracker.getExpenses().get(0), filteredExpenses.get(0));
    }

    @Test
    void testFilterByDateNullStartDate() {
        List<ExpenseFilter> filters = new ArrayList<>();
        filters.add(new FilterByDate(null, LocalDate.now()));
        assertEquals("", filters.get(0).toString());
    }

    @Test
    void testFilterByDateNullEndDate() {
        List<ExpenseFilter> filters = new ArrayList<>();
        filters.add(new FilterByDate(LocalDate.now(), null));
        assertEquals("", filters.get(0).toString());
    }

    @Test
    void testFilterByCategory() {
        testAddExpenseOnce();
        try {
            testSpendingTracker.addExpense(45, " ", " ",
                    LocalDate.of(2022, 10, 21), Category.OTHER);
        } catch (ZeroOrLessException | ZeroLengthException | NullPointerException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        assertEquals(2, testSpendingTracker.getExpenses().size());
        try {
            testSpendingTracker.addExpense(250, " ", " ",
                    LocalDate.of(2023, 7, 6), Category.PERSONAL_CARE);
        } catch (ZeroOrLessException | ZeroLengthException | NullPointerException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        assertEquals(3, testSpendingTracker.getExpenses().size());
        List<ExpenseFilter> filters = new ArrayList<>();
        filters.add(new FilterByCategories(Category.OTHER));
        List<Expense> filteredExpenses = testSpendingTracker.filter(filters);
        assertEquals(2, filteredExpenses.size());
        assertEquals(testSpendingTracker.getExpenses().get(0), filteredExpenses.get(0));
        assertEquals(testSpendingTracker.getExpenses().get(1), filteredExpenses.get(1));
    }

    @Test
    void testFilterByCategories() {
        testAddExpenseOnce();
        try {
            testSpendingTracker.addExpense(80, " ", " ",
                    LocalDate.of(2022, 10, 10), Category.OTHER);
        } catch (ZeroOrLessException | ZeroLengthException | NullPointerException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        assertEquals(2, testSpendingTracker.getExpenses().size());
        try {
            testSpendingTracker.addExpense(125, " ", " ",
                    LocalDate.of(2023, 5, 23), Category.PERSONAL_CARE);
        } catch (ZeroOrLessException | ZeroLengthException | NullPointerException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        assertEquals(3, testSpendingTracker.getExpenses().size());
        try {
            testSpendingTracker.addExpense(110, " ", " ",
                    LocalDate.of(2022, 12, 15), Category.TRANSPORTATION);
        } catch (ZeroOrLessException | ZeroLengthException | NullPointerException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        assertEquals(4, testSpendingTracker.getExpenses().size());
        Set<Category> categories = new HashSet<>();
        categories.add(Category.OTHER);
        categories.add(Category.PERSONAL_CARE);
        List<ExpenseFilter> filters = new ArrayList<>();
        filters.add(new FilterByCategories(categories));
        List<Expense> filteredExpenses = testSpendingTracker.filter(filters);
        assertEquals(3, filteredExpenses.size());
        assertEquals(testSpendingTracker.getExpenses().get(0), filteredExpenses.get(0));
        assertEquals(testSpendingTracker.getExpenses().get(1), filteredExpenses.get(1));
        assertEquals(testSpendingTracker.getExpenses().get(2), filteredExpenses.get(2));
    }

    @Test
    void testFilterByCategoriesAddCategory() {
        testAddExpenseOnce();
        try {
            testSpendingTracker.addExpense(45, " ", " ",
                    LocalDate.of(2022, 10, 21), Category.OTHER);
        } catch (ZeroOrLessException | ZeroLengthException | NullPointerException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        assertEquals(2, testSpendingTracker.getExpenses().size());
        try {
            testSpendingTracker.addExpense(250, " ", " ",
                    LocalDate.of(2023, 7, 6), Category.PERSONAL_CARE);
        } catch (ZeroOrLessException | ZeroLengthException | NullPointerException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        assertEquals(3, testSpendingTracker.getExpenses().size());
        List<ExpenseFilter> filters = new ArrayList<>();
        FilterByCategories categoryFilter = new FilterByCategories(Category.OTHER);
        categoryFilter.addCategory(Category.PERSONAL_CARE);
        filters.add(categoryFilter);
        List<Expense> filteredExpenses = testSpendingTracker.filter(filters);
        assertEquals(3, filteredExpenses.size());
        assertEquals(testSpendingTracker.getExpenses().get(0), filteredExpenses.get(0));
        assertEquals(testSpendingTracker.getExpenses().get(1), filteredExpenses.get(1));
        assertEquals(testSpendingTracker.getExpenses().get(2), filteredExpenses.get(2));
    }

    @Test
    void testFilterByCategoriesRemoveCategory() {
        testAddExpenseOnce();
        try {
            testSpendingTracker.addExpense(80, " ", " ",
                    LocalDate.of(2022, 10, 10), Category.OTHER);
        } catch (ZeroOrLessException | ZeroLengthException | NullPointerException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        assertEquals(2, testSpendingTracker.getExpenses().size());
        try {
            testSpendingTracker.addExpense(125, " ", " ",
                    LocalDate.of(2023, 5, 23), Category.PERSONAL_CARE);
        } catch (ZeroOrLessException | ZeroLengthException | NullPointerException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        assertEquals(3, testSpendingTracker.getExpenses().size());
        try {
            testSpendingTracker.addExpense(110, " ", " ",
                    LocalDate.of(2022, 12, 15), Category.TRANSPORTATION);
        } catch (ZeroOrLessException | ZeroLengthException | NullPointerException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        assertEquals(4, testSpendingTracker.getExpenses().size());
        Set<Category> categories = new HashSet<>();
        categories.add(Category.OTHER);
        categories.add(Category.PERSONAL_CARE);
        List<ExpenseFilter> filters = new ArrayList<>();
        FilterByCategories categoryFilter = new FilterByCategories(categories);
        categoryFilter.removeCategory(Category.PERSONAL_CARE);
        filters.add(categoryFilter);
        List<Expense> filteredExpenses = testSpendingTracker.filter(filters);
        assertEquals(2, filteredExpenses.size());
        assertEquals(testSpendingTracker.getExpenses().get(0), filteredExpenses.get(0));
        assertEquals(testSpendingTracker.getExpenses().get(1), filteredExpenses.get(1));
    }

    @Test
    void testFilterByDateAndCategories() {
        testAddExpenseOnce();
        try {
            testSpendingTracker.addExpense(20, " ", " ",
                    LocalDate.of(2023, 7, 16), Category.OTHER);
        } catch (ZeroOrLessException | ZeroLengthException | NullPointerException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        assertEquals(2, testSpendingTracker.getExpenses().size());
        try {
            testSpendingTracker.addExpense(60, " ", " ",
                    LocalDate.of(2023, 7, 24), Category.PERSONAL_CARE);
        } catch (ZeroOrLessException | ZeroLengthException | NullPointerException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        assertEquals(3, testSpendingTracker.getExpenses().size());
        try {
            testSpendingTracker.addExpense(60, " ", " ",
                    LocalDate.of(2023, 7, 15), Category.HEALTH);
        } catch (ZeroOrLessException | ZeroLengthException | NullPointerException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        assertEquals(4, testSpendingTracker.getExpenses().size());
        try {
            testSpendingTracker.addExpense(45, " ", " ",
                    LocalDate.of(2023, 1, 3), Category.PERSONAL_CARE);
        } catch (ZeroOrLessException | ZeroLengthException | NullPointerException e) {
            fail(e.getMessage() + "\nThis exception should not have been thrown!");
        }
        assertEquals(5, testSpendingTracker.getExpenses().size());
        Set<Category> categories = new HashSet<>();
        categories.add(Category.OTHER);
        categories.add(Category.PERSONAL_CARE);
        List<ExpenseFilter> filters = new ArrayList<>();
        filters.add(new FilterByDate(Month.JULY));
        assertEquals("Filter by date: July 01 2023 to July 31 2023", filters.get(0).toString());
        filters.add(new FilterByCategories(categories));
        assertEquals("Filter by categories: Others, Personal Care", filters.get(1).toString());
        List<Expense> filteredExpenses = testSpendingTracker.filter(filters);
        assertEquals(3, filteredExpenses.size());
        assertEquals(testSpendingTracker.getExpenses().get(0), filteredExpenses.get(0));
        assertEquals(testSpendingTracker.getExpenses().get(1), filteredExpenses.get(1));
    }

}
