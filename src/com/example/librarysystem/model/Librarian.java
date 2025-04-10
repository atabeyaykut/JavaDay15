package com.example.librarysystem.model;

public class Librarian extends AbstractPerson {
    private String employeeNumber;

    public Librarian(String id, String name, String username, String password, String employeeNumber) {
        super(id, name, username, password);
        this.employeeNumber = employeeNumber;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public void verifyMembership(Reader reader) {
        System.out.println("Üyelik doğrulandı: " + reader.getName());
    }
}
