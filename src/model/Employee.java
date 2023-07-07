package model;

public class Employee {
    private int employeeID;
    private String lastName;
    private String firstName;

    public Employee(int employeeID, String lastName, String firstName) {
        this.employeeID = employeeID;
        this.lastName = lastName;
        this.firstName = firstName;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }
}
