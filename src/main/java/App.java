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

        port(getHerokuAssignedPort());
        staticFileLocation("/public");

        get("/",(request, response) -> {
            Map<String,Object> model=new HashMap<String, Object>();
            return new ModelAndView(model,"index.hbs");
        },new HandlebarsTemplateEngine());

        //ranger
        get("/create/department",(request, response) -> {
            Map<String,Object> model=new HashMap<String, Object>();
            return new ModelAndView(model,"departmentform.hbs");
        },new HandlebarsTemplateEngine());

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