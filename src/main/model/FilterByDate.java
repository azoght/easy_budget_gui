package model;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.format.DateTimeFormatter;

// Represents a filter for expenses by a date range with a start date and an end date
public class FilterByDate implements ExpenseFilter {

    private LocalDate startDate;
    private LocalDate endDate;

    /*
     * EFFECTS: constructs a date filter for a specified date range
     */
    public FilterByDate(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /*
     * EFFECTS: constructs a date filter for a given month in a given year
     */
    public FilterByDate(Month month, Year year) {
        this.startDate = LocalDate.of(year.getValue(), month, 1);
        this.endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
    }

    /*
     * EFFECTS: constructs a date filter for a given month in the current year
     */
    public FilterByDate(Month month) {
        int year = Year.now().getValue();

        this.startDate = LocalDate.of(year, month, 1);
        this.endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    /*
     * EFFECTS: if the date of e is between startDate and endDate, returns true
     *          otherwise returns false
     */
    @Override
    public boolean accept(Expense e) {
        return e.getDate().compareTo(startDate) >= 0 && e.getDate().compareTo(endDate) <= 0;
    }

    /*
     * EFFECTS: returns a string representation of the date filter
     */
    @Override
    public String toString() {
        if (startDate == null || endDate == null) {
            return "";
        }
        return "Filter by date: " + DateTimeFormatter.ofPattern("MMMM dd yyyy").format(startDate) + " to "
                + DateTimeFormatter.ofPattern("MMMM dd yyyy").format(endDate);
    }
}
