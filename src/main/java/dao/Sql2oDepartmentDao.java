package dao;

import models.Department;
import models.DepartmentNews;
import models.Employee;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.ArrayList;
import java.util.List;

public class Sql2oDepartmentDao implements DepartmentDao{
    private final Sql2o sql2o;
    public Sql2oDepartmentDao(Sql2o sql2o) { this.sql2o = sql2o; }

    @Override
    public void add(Department department) {
        try(Connection con=sql2o.open()) {
            String sql="INSERT INTO departments (name,description,size) VALUES (:name,:description,:size)";
            int id=(int) con.createQuery(sql,true)
                    .bind(department)
                    .executeUpdate()
                    .getKey();
            department.setId(id);

        }catch (Sql2oException e){
            System.out.println(e);
        }

    }

    @Override
    public void addEmployeeToDepartment(Employee employee, Department department) {
        try(Connection con=sql2o.open()) {
            String sql="INSERT INTO employees_departments (employee_id,department_id) VALUES (:employee_id,:department_id)";
            con.createQuery(sql)
                    .addParameter("employee_id",employee.getId())
                    .addParameter("department_id",department.getId())
                    .executeUpdate();
            String sizeQuery="SELECT employee_id FROM employees_departments";
            List<Integer> size=con.createQuery(sizeQuery)
                    .executeAndFetch(Integer.class);
            String updateDepartmentSize="UPDATE departments SET size=:size WHERE id=:id";
            con.createQuery(updateDepartmentSize).addParameter("id",department.getId())
                    .addParameter("size",size.size())
                    .executeUpdate();

        }catch (Sql2oException e){
            System.out.println(e);
        }

    }

    @Override
    public List<Department> getAll() {
        try (Connection con=sql2o.open()){
            String sql= "SELECT * FROM departments";
            return con.createQuery(sql)
                    .executeAndFetch(Department.class);
        }
    }

    @Override
    public List<Employee> getAllEmployeeForADepartment(int department_id) {
        List<Employee> employees=new ArrayList<>();
        try (Connection con=sql2o.open()){
            String sql= "SELECT employee_id FROM employees_departments WHERE department_id=:department_id";
            List<Integer> employee_ids=con.createQuery(sql)
                    .addParameter("department_id",department_id)
                    .executeAndFetch(Integer.class);

            for(Integer id : employee_ids){
                String employeeResults="SELECT * FROM employees WHERE id=:id";
                employees.add(con.createQuery(employeeResults)
                        .addParameter("id",id)
                        .executeAndFetchFirst(Employee.class));

            }

            return employees;
        }
    }

    @Override
    public Department findById(int id) {
        try (Connection con=sql2o.open()){
            String sql= "SELECT * FROM departments WHERE id=:id";
            return con.createQuery(sql)
                    .addParameter("id",id)
                    .executeAndFetchFirst(Department.class);

        }
    }

    @Override
    public List<DepartmentNews> getDepartmentNews(int id) {
        try(Connection con=sql2o.open()) {
            String sql="SELECT * FROM departmentnews WHERE id=:id ";
            return con.createQuery(sql)
                    .addParameter("id",id)
                    .executeAndFetch(DepartmentNews.class);
        }

    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE from departments WHERE id=:id";
        String deleteJoin = "DELETE from employees_departments WHERE employee_id = :employee_id";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();

            con.createQuery(deleteJoin)
                    .addParameter("employee_id", id)
                    .executeUpdate();
        } catch (Sql2oException ex){
            System.out.println(ex);
        }

    }

    @Override
    public void clearAll() {
        try (Connection con=sql2o.open()){
            String sql="DELETE FROM departments";
            String sqlEmployeesDepartments="DELETE FROM employees_departments";
            con.createQuery(sql).executeUpdate();
            con.createQuery(sqlEmployeesDepartments).executeUpdate();

        }catch (Sql2oException e){
            System.out.println(e);
        }

    }
}

