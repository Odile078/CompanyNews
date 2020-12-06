
import com.google.gson.Gson;
import dao.Sql2oDepartmentNewsDao;
import dao.Sql2oEmployeeDao;
import dao.Sql2oDepartmentDao;
import dao.Sql2oGeneralNewsDao;
import models.Employee;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import static spark.Spark.*;

import models.*;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;

public class App {
    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }

    public static void main(String[] args) {
        Sql2oDepartmentDao sql2oDepartmentDao;
        Sql2oEmployeeDao sql2oEmployeeDao;
        Sql2oGeneralNewsDao sql2oGeneralNewsDao;
        Sql2oDepartmentNewsDao sql2oDepartmentNewsDao;
        Connection conn;
        Gson gson = new Gson();

        String connectionString = "jdbc:postgresql://localhost:5432/companynews";
        Sql2o sql2o = new Sql2o(connectionString, "odile", "123");

        sql2oDepartmentDao = new Sql2oDepartmentDao(sql2o);
        sql2oEmployeeDao = new Sql2oEmployeeDao(sql2o);
        sql2oGeneralNewsDao = new  Sql2oGeneralNewsDao(sql2o);
        sql2oDepartmentNewsDao = new Sql2oDepartmentNewsDao(sql2o);
        conn = sql2o.open();

        port(getHerokuAssignedPort());
        staticFileLocation("/public");

        get("/",(request, response) -> {
            Map<String,Object> model=new HashMap<String, Object>();
            return new ModelAndView(model,"index.hbs");
        },new HandlebarsTemplateEngine());
        //ranger
//department
    //interface
        get("/create/department",(request, response) -> {
            Map<String,Object> model=new HashMap<String, Object>();
            return new ModelAndView(model,"departmentform.hbs");
        },new HandlebarsTemplateEngine());

        post("/create/department/new",(request, response) -> {
            Map<String,Object> model=new HashMap<String, Object>();
            String name=request.queryParams("name");
            String description=request.queryParams("description");
            String size=request.queryParams("size");
            Department department=new Department(name,description);
            sql2oDepartmentDao.add( department);
            request.session().attribute("item", name);
            model.put("item", request.session().attribute("item"));
            return new ModelAndView(model,"departmentform.hbs");
            //return new ModelAndView(model,"departmentsuccess.hbs");
        },new HandlebarsTemplateEngine());

        //retrieving the department
        get("/view/departments",(request, response) -> {
            Map<String,Object> model=new HashMap<String, Object>();
            model.put("departments",sql2oDepartmentDao.getAll());
            return new ModelAndView(model,"departmentview.hbs");
        },new HandlebarsTemplateEngine());

        //retrive department news
       /*get("/view/location/sightings/:id",(request, response) -> {
            Map<String,Object> model=new HashMap<String, Object>();
            int idOfLocation= Integer.parseInt(request.params(":id"));
            Department foundLocation= Department.sql2oDepartmentDao.findById(idOfLocation);
            List<DepartmentNews> news=foundLocation.getAll();
            ArrayList<String> animals=new ArrayList<String>();
            ArrayList<String> types=new ArrayList<String>();
            for (RegSighting sighting : news){
                String animal_name=RegAnimal.find(sighting.getRegAnimal_id()).getName();
                String animal_type=RegAnimal.find(sighting.getRegAnimal_id()).getType();
                animals.add(animal_name);
                types.add(animal_type);
            }
            model.put("sightings",news);
            model.put("animals",animals);
            model.put("types",types);
            model.put("locations",RegLocation.all());
            return new ModelAndView(model,"locationview.hbs");
        },new HandlebarsTemplateEngine());*/


    //Api
        //
        post("/departments/new", "application/json", (req, res) -> {
            Department department = gson.fromJson(req.body(), Department.class);
            sql2oDepartmentDao.add(department);
            res.status(201);
            res.type("application/json");
            return gson.toJson(department);
        });

// Employee
    // interface
        //creating employee interface
        get("/create/employee",(request, response) -> {
            Map<String,Object> model=new HashMap<String, Object>();
            return new ModelAndView(model,"employeeform.hbs");
        },new HandlebarsTemplateEngine());
        //employee retrieval
        post("/create/employee/new",(request, response) -> {
            Map<String,Object> model=new HashMap<String, Object>();
            String name=request.queryParams("name");
            String position=request.queryParams("position");
            String role=request.queryParams("role");
            Employee employee=new Employee(name, position, role);
            sql2oEmployeeDao.add( employee);
            request.session().attribute("item", name);
            model.put("item", request.session().attribute("item"));
            return new ModelAndView(model,"employeeform.hbs");
            //return new ModelAndView(model,"employeesuccess.hbs");
        },new HandlebarsTemplateEngine());
        //retrieving the employee
        get("/view/employees",(request, response) -> {
            Map<String,Object> model=new HashMap<String, Object>();
            model.put("employees",sql2oEmployeeDao.getAll());
            return new ModelAndView(model,"employeeview.hbs");
        },new HandlebarsTemplateEngine());
    //Api

//General News
    //Interface
        // creating news interface
        get("/create/news",(request, response) -> {
            Map<String,Object> model=new HashMap<String, Object>();
            return new ModelAndView(model,"generalnewsform.hbs");
        },new HandlebarsTemplateEngine());

        //news retrieval
        post("/create/news/new",(request, response) -> {
            Map<String,Object> model=new HashMap<String, Object>();
            String title=request.queryParams("title");
            String writtenBy=request.queryParams("writtenBy");
            String content=request.queryParams("content");
            int employee_id=Integer.parseInt(request.params("id"));
            int department_id=Integer.parseInt(request.params("id"));
            GeneralNews generalnews=new GeneralNews(title,writtenBy, content,employee_id);
            sql2oGeneralNewsDao.addGeneralNews(generalnews);
            request.session().attribute("item", title);
            model.put("item", request.session().attribute("item"));
            return new ModelAndView(model,"generalnewsform.hbs");
            //return new ModelAndView(model,"employeesuccess.hbs");
        },new HandlebarsTemplateEngine());

        //retrieving the department
        get("/view/news",(request, response) -> {
            Map<String,Object> model=new HashMap<String, Object>();
            model.put("news",sql2oGeneralNewsDao.getAll());
            return new ModelAndView(model,"generalnewsview.hbs");
        },new HandlebarsTemplateEngine());

    //Api
//General News
    //Interface
        // creating news interface
        get("/create/news",(request, response) -> {
            Map<String,Object> model=new HashMap<String, Object>();
            return new ModelAndView(model,"departmentnewsform.hbs");
        },new HandlebarsTemplateEngine());

        //news retrieval
        post("/create/news/new",(request, response) -> {
            Map<String,Object> model=new HashMap<String, Object>();
            String title=request.queryParams("title");
            String writtenBy=request.queryParams("writtenBy");
            String content=request.queryParams("content");
            int employee_id=Integer.parseInt(request.params("id"));
            int department_id=Integer.parseInt(request.params("id"));
            DepartmentNews departmentnews=new DepartmentNews(title,writtenBy, content,employee_id,department_id);
            sql2oDepartmentNewsDao.addDepartmentNews(departmentnews);
            request.session().attribute("item", title);
            model.put("item", request.session().attribute("item"));
            return new ModelAndView(model,"departmentnewsform.hbs");
            //return new ModelAndView(model,"employeesuccess.hbs");
        },new HandlebarsTemplateEngine());

        //retrieving the department
        get("/view/news",(request, response) -> {
            Map<String,Object> model=new HashMap<String, Object>();
            model.put("news",sql2oDepartmentNewsDao.getAll());
            return new ModelAndView(model,"departmentnewsview.hbs");
        },new HandlebarsTemplateEngine());

    //Api
//exception


//after

/*

        post("/create/ranger/new",(request, response) -> {
            Map<String,Object> model=new HashMap<String, Object>();
            String name=request.queryParams("name");
            String badge_id=request.queryParams("badge");
            String phone_number=request.queryParams("phone");
            RegRanger ranger=new RegRanger(name,badge_id,phone_number);
            ranger.save();
            request.session().attribute("item", name);
            model.put("item", request.session().attribute("item"));
            return new ModelAndView(model,"rangersuccess.hbs");
        },new HandlebarsTemplateEngine());

        get("/view/rangers",(request, response) -> {
            Map<String,Object> model=new HashMap<String, Object>();
            model.put("rangers",RegRanger.all());
            return new ModelAndView(model,"rangerview.hbs");
        },new HandlebarsTemplateEngine());
        get("/view/ranger/sightings/:id",(request, response) -> {
            Map<String,Object> model=new HashMap<String, Object>();
            int idOfRanger= Integer.parseInt(request.params(":id"));
            RegRanger foundRanger=RegRanger.find(idOfRanger);
            List<RegSighting> sightings=foundRanger.getRangerSightings();
            ArrayList<String> animals=new ArrayList<String>();
            ArrayList<String> types=new ArrayList<String>();
            for (RegSighting sighting : sightings){
                String animal_name= RegAnimal.find(sighting.getRegAnimal_id()).getName();
                String animal_type=RegAnimal.find(sighting.getRegAnimal_id()).getType();
                animals.add(animal_name);
                types.add(animal_type);
            }
            model.put("sightings",sightings);
            model.put("animals",animals);
            model.put("types",types);
            model.put("rangers",RegRanger.all());
            return new ModelAndView(model,"rangerview.hbs");
        },new HandlebarsTemplateEngine());





        //location
        get("/create/location",(request, response) -> {
            Map<String,Object> model=new HashMap<String, Object>();
            return new ModelAndView(model,"locationform.hbs");
        },new HandlebarsTemplateEngine());


        post("/create/location/new",(request, response) -> {
            Map<String,Object> model=new HashMap<String, Object>();
            String name=request.queryParams("name");
            RegLocation location=new RegLocation(name);
            try {
                location.save();
            }catch (IllegalArgumentException e){
                System.out.println(e);
            }

            request.session().attribute("item", name);
            model.put("item", request.session().attribute("item"));

            return new ModelAndView(model,"locationsuccess.hbs");
        },new HandlebarsTemplateEngine());


        get("/view/locations",(request, response) -> {
            Map<String,Object> model=new HashMap<String, Object>();
            model.put("locations",RegLocation.all());
            return new ModelAndView(model,"locationview.hbs");
        },new HandlebarsTemplateEngine());

        get("/view/location/sightings/:id",(request, response) -> {
            Map<String,Object> model=new HashMap<String, Object>();
            int idOfLocation= Integer.parseInt(request.params(":id"));
            RegLocation foundLocation=RegLocation.find(idOfLocation);
            List<RegSighting> sightings=foundLocation.getLocationSightings();
            ArrayList<String> animals=new ArrayList<String>();
            ArrayList<String> types=new ArrayList<String>();
            for (RegSighting sighting : sightings){
                String animal_name=RegAnimal.find(sighting.getRegAnimal_id()).getName();
                String animal_type=RegAnimal.find(sighting.getRegAnimal_id()).getType();
                animals.add(animal_name);
                types.add(animal_type);
            }
            model.put("sightings",sightings);
            model.put("animals",animals);
            model.put("types",types);
            model.put("locations",RegLocation.all());
            return new ModelAndView(model,"locationview.hbs");
        },new HandlebarsTemplateEngine());


        //animal
        get("/create/animal",(request, response) -> {
            Map<String,Object> model=new HashMap<String, Object>();
            return new ModelAndView(model,"animalform.hbs");
        },new HandlebarsTemplateEngine());
*/
        /*post("/create/animal/new",(request, response) -> {
            Map<String,Object> model=new HashMap<String, Object>();
            String type=request.queryParams("type");
            System.out.println(type);
            String health=request.queryParams("health");
            System.out.println(health);
            String age=request.queryParams("age");
            System.out.println(age);
            String name=request.queryParams("name");
            System.out.println(name);
            if(type.equals(RegEndangeredAnimal.ANIMAL_TYPE)){
                RegEndangeredAnimal endangered=new RegEndangeredAnimal(name,RegEndangeredAnimal.ANIMAL_TYPE,health,age);
                endangered.save();
            }
            else {
                RegAnimal animal=new RegAnimal(name,RegAnimal.ANIMAL_TYPE);
                animal.save();
            }

            return new ModelAndView(model,"animalform.hbs");
        },new HandlebarsTemplateEngine());*/
/*
        post("/create/animal/new",(request, response) -> {
            Map<String,Object> model=new HashMap<String, Object>();
            String type=request.queryParams("type");
            System.out.println(type);
            String health=request.queryParams("health");
            System.out.println(health);
            String age=request.queryParams("age");
            System.out.println(age);
            String name=request.queryParams("name");
            System.out.println(name);
            if(type.equals(RegEndangeredAnimal.ANIMAL_TYPE)){
                RegEndangeredAnimal endangered=new RegEndangeredAnimal(name,RegEndangeredAnimal.ANIMAL_TYPE,health,age);
                endangered.save();
            }
            else {
                RegAnimal animal=new RegAnimal(name,RegAnimal.ANIMAL_TYPE);
                animal.save();
            }

            request.session().attribute("item", name);
            model.put("item", request.session().attribute("item"));

            return new ModelAndView(model,"animalsuccess.hbs");
        },new HandlebarsTemplateEngine());


        get("/create/animal/endangered",(request, response) -> {
            Map<String,Object> model=new HashMap<String, Object>();
            List<String> health= new ArrayList<String>();
            health.add(RegEndangeredAnimal.HEALTH_HEALTHY);
            health.add(RegEndangeredAnimal.HEALTH_ILL);
            health.add(RegEndangeredAnimal.HEALTH_OKAY);
            List<String> age= new ArrayList<String>();
            age.add(RegEndangeredAnimal.AGE_ADULT);
            age.add(RegEndangeredAnimal.AGE_NEWBORN);
            age.add(RegEndangeredAnimal.AGE_YOUNG);
            model.put("health",health);
            model.put("age",age);
            String typeChosen="endangered";
            model.put("endangered",typeChosen);
            return new ModelAndView(model,"animalform.hbs");
        },new HandlebarsTemplateEngine());

        get("/view/animals",(request, response) -> {
            Map<String,Object> model=new HashMap<String, Object>();
            model.put("animals",RegAnimal.all());
            return new ModelAndView(model,"animalview.hbs");
        },new HandlebarsTemplateEngine());


        //sighting
        get("/create/sighting",(request, response) -> {
            Map<String,Object> model=new HashMap<String, Object>();
            model.put("rangers",RegRanger.all());
            model.put("locations",RegLocation.all());
            model.put("animals",RegAnimal.all());
            return new ModelAndView(model,"sightingform.hbs");
        },new HandlebarsTemplateEngine());

        post("/create/sighting/new",(request, response) -> {
            Map<String,Object> model=new HashMap<String, Object>();
            int location_id= Integer.parseInt(request.queryParams("location"));
            int ranger_id= Integer.parseInt(request.queryParams("ranger"));
            int animal_id= Integer.parseInt(request.queryParams("animal"));

            RegSighting sighting=new RegSighting(location_id,ranger_id,animal_id);
            sighting.save();

            request.session().attribute("item", location_id);
            model.put("item", request.session().attribute("item"));
            return new ModelAndView(model,"sightingsuccess.hbs");
        },new HandlebarsTemplateEngine());

        get("/view/sightings",(request, response) -> {
            Map<String,Object> model=new HashMap<String, Object>();
            List<RegSighting> sightings=RegSighting.all();
            ArrayList<String> animals=new ArrayList<String>();
            ArrayList<String> types=new ArrayList<String>();
            for (RegSighting sighting : sightings){
                String animal_name=RegAnimal.find(sighting.getRegAnimal_id()).getName();
                String animal_type=RegAnimal.find(sighting.getRegAnimal_id()).getType();
                animals.add(animal_name);
                types.add(animal_type);
            }
            model.put("sightings",sightings);
            model.put("animals",animals);
            model.put("types",types);
            return new ModelAndView(model,"sightingview.hbs");
        },new HandlebarsTemplateEngine());*/



    }
}