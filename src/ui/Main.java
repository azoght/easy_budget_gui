package ui;

import model.Employee;

import java.util.HashSet;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        Employee e1 = new Employee(1314, "Smith", "Jane");
        Employee e2 = e1;
        Employee e3 = new Employee(1314, "Smith", "Jane");
        Employee e4 = new Employee(1314, "Smith-Chan", "Jane");

        Set<Employee> employees = new HashSet<>();
        employees.add(e1);

        System.out.println("e1 == e2: " + (e1 == e2));
        System.out.println("e1.equals(e2): " + (e1.equals(e2)));

        System.out.println("\ne1 == e3: " + (e1 == e3));
        System.out.println("e1.equals(e3): " + (e1.equals(e3)));

        System.out.println("\ne1 == e4: " + (e1 == e4));
        System.out.println("e1.equals(e4): " + (e1.equals(e4)));

        System.out.println("\n\nEmployees contains e2: " + employees.contains(e2));
        System.out.println("Employees contains e3: " + employees.contains(e3));
        System.out.println("Employees contains e4: " + employees.contains(e4));
    }
}
