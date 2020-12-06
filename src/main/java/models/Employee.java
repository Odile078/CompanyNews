package models;

import java.util.Objects;

public class Employee {
    private int id;
    private String name;
    private String position;
    private String role;

    public Employee(String name, String position, String role){
        this.name = name;
        this.position = position;
        this.position = role;
        this.id =id;


    }

    public int getId() { return id; }
    public String getName() {
        return name;
    }
    public String getPosition() {
        return position;
    }
    public String getRole() {
        return role;
    }


    public void setId(int id) { this.id = id; }
    public void setName(String name) {
        this.name = name;
    }
    public void setPosition(String position) {
        this.position = position;
    }
    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return id == employee.id &&
                Objects.equals(name, employee.name) &&
                Objects.equals(position, employee.position) &&
                Objects.equals(role, employee.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, position, role);
    }
}
