package dao;

import models.Department;
import models.DepartmentNews;
import models.Employee;

import java.util.List;

public interface DepartmentDao {
    //create
    void add(Department department);
    void addEmployeeToDepartment(Employee employee, Department department);

    //read
    List<Department> getAll();
    List<Employee> getAllEmployeeForADepartment(int id);
    Department findById(int id);
    List<DepartmentNews> getDepartmentNews(int id);


    //update
    //omit for now

    //delete
    void deleteById(int id);
    void clearAll();
}
