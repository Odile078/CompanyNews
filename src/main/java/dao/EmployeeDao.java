package dao;

import models.Department;
import models.Employee;

import java.util.List;

public interface EmployeeDao {
    //create
    void add (Employee employee);
    //void addEmployeeToDepartment(Employee employee, Department department);

    //read
    List<Employee> getAll();
    Employee findById(int id);
    List<Department> getAllEmployeeByDepartment(int employee_id);

    //update
    void update(int id,String name, String position, String role);

    //delete
    void deleteById(int id);
    void clearAll();

}
