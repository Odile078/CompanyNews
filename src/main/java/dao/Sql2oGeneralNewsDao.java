package dao;

import models.DepartmentNews;
import models.GeneralNews;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.List;

public class Sql2oGeneralNewsDao implements GeneralNewsDao{
    private final Sql2o sql2o;
    public Sql2oGeneralNewsDao(Sql2o sql2o) { this.sql2o = sql2o; }
    @Override
    public void addGeneralNews(GeneralNews generalnews) {
        String sql = "INSERT INTO generalnews (title,writtenby, content,createdat, employee_id) VALUES (:tittle,:writtenBy,:content,:createdat, :employee_id)";
        try (Connection con = sql2o.open()) {
            int id = (int) con.createQuery(sql, true)
                    .bind(generalnews)
                    .executeUpdate()
                    .getKey();
            generalnews.setId(id);
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }

    }



    @Override
    public List<GeneralNews> getAll() {
        try(Connection con=sql2o.open()) {
            String sql="SELECT * FROM generalnews";
            return con.createQuery(sql,true)
                    .executeAndFetch(GeneralNews.class);

        }
    }

    @Override
    public GeneralNews findById(int id) {
        try(Connection con=sql2o.open()) {
            String sql="SELECT * FROM generalnews WHERE id=:id";
            return con.createQuery(sql)
                    .addParameter("id",id)
                    .executeAndFetchFirst(GeneralNews.class);
        }
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE from generalnews WHERE id=:id";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }

    }

    @Override
    public void clearAll() {
        try (Connection con=sql2o.open()){
            String sql="DELETE FROM departments";
            String sqlNews="DELETE FROM generalnews";
            String sqlEmployeesDepartments="DELETE FROM employees_departments";
            con.createQuery(sql).executeUpdate();
            con.createQuery(sqlEmployeesDepartments).executeUpdate();
            con.createQuery(sqlNews).executeUpdate();

        }catch (Sql2oException e){
            System.out.println(e);
        }

    }
}
