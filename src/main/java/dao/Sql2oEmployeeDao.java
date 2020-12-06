package dao;

import models.Department;
import models.Employee;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.ArrayList;
import java.util.List;

public class Sql2oEmployeeDao implements EmployeeDao{
    private final Sql2o sql2o;
    public Sql2oEmployeeDao(Sql2o sql2o) { this.sql2o = sql2o; }

    @Override
    public void add(Employee employee) {
        String sql = "INSERT INTO employees (name, position, role) VALUES (:name, :position, :role)";
        try (Connection con = sql2o.open()) {
            int id = (int) con.createQuery(sql, true)
                    .bind(employee)
                    .executeUpdate()
                    .getKey();
            employee.setId(id);
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public List<Employee> getAll() {
        try (Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM employees")
                    .executeAndFetch(Employee.class);
        }
    }

    @Override
    public Employee findById(int id) {
        try (Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM employees WHERE id = :id")
                    .addParameter("id", id)
                    .executeAndFetchFirst(Employee.class);
        }
    }

    @Override
    public List<Department> getAllEmployeeByDepartment(int employee_id) {
        List<Department> departments=new ArrayList<>();
        try (Connection con=sql2o.open()) {
            String sql = "SELECT department_id FROM employees_departments WHERE employee_id=:employee_id";
            List<Integer> department_ids = con.createQuery(sql)
                    .addParameter("employee_id", employee_id)
                    .executeAndFetch(Integer.class);

            for (Integer id : department_ids) {
                String employeeResults = "SELECT * FROM departments WHERE id=:id";
                departments.add(con.createQuery(employeeResults)
                        .addParameter("id", id)
                        .executeAndFetchFirst(Department.class));

            }

            return departments;
        }
    }


    @Override
    public void update(int id,String name, String position, String role) {
        String sql = "UPDATE employees SET (name, position, role) = (:name, :position, :role) WHERE id=:id"; //CHECK!!!
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("name", name)
                    .addParameter("position", position)
                    .addParameter("zipcode", role)
                    .addParameter("id", id)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }

    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE from employees WHERE id = :id"; //raw sql
        String deleteJoin = "DELETE from employees_departments WHERE employee_id = :employee_id";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();
            con.createQuery(deleteJoin)
                    .addParameter("employee_d", id)
                    .executeUpdate();

        } catch (Sql2oException ex){
            System.out.println(ex);
        }

    }

    @Override
    public void clearAll() {
        try (Connection con=sql2o.open()){
            String sql ="DELETE FROM employees ";
            con.createQuery(sql).executeUpdate();
            String sqlEmployeesDepartments="DELETE FROM employees_departments";
            con.createQuery(sqlEmployeesDepartments).executeUpdate();


        }catch (Sql2oException e){
            System.out.println(e);
        }

    }
}

