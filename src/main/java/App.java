import java.util.HashMap;
import java.util.List;

import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;


public class App {
  public static void main(String[] args) {
    staticFileLocation("/public/");
    String layout = "templates/layout.vtl";

    get("/", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/index.vtl");
      model.put("bands", Band.all());
      model.put("venues", Venue.all());
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/add/band", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      String inputName = request.queryParams("name");
      Band newBand = new Band(inputName);
      newBand.firstToUppercase();
      newBand.save();
      model.put("band", newBand);
      model.put("bands", Band.all());
      model.put("venues", Venue.all());
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/add/venue", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      String inputName = request.queryParams("name");
      Venue newVenue = new Venue(inputName);
      newVenue.firstToUppercase();
      newVenue.save();
      model.put("venue", newVenue);
      model.put("bands", Band.all());
      model.put("venues", Venue.all());
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/band/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      int id = Integer.parseInt(request.params("id"));
      Band myBand = Band.find(id);
      model.put("band", myBand);
      model.put("template", "templates/band.vtl");
      model.put("venues", Venue.all());
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/band/update/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      int id = Integer.parseInt(request.params("id"));
      Band myBand = Band.find(id);
      int venueId = Integer.parseInt(request.queryParams("venue_id"));
      Venue myVenue = Venue.find(venueId);
      myBand.addVenue(myVenue);
      model.put("band", myBand);
      model.put("template", "templates/band.vtl");
      model.put("venues", Venue.all());
      return new ModelAndView(model, layout);
      }, new VelocityTemplateEngine());

    post("/band/update/name/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      int id = Integer.parseInt(request.params("id"));
      Band myBand = Band.find(id);
      String inputName = request.queryParams("name");
      myBand.update(inputName);
      model.put("band", myBand);
      // model.put("template", "templates/band.vtl"); //page wont redirect to same page
      model.put("venues", Venue.all());
      // return new ModelAndView(model, layout);
      // }, new VelocityTemplateEngine());
      response.redirect("/");
      return null;
    });


    post("/band/delete/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      int id = Integer.parseInt(request.params("id"));
      Band myBand = Band.find(id);
      myBand.delete();
      model.put("band", myBand);
        response.redirect("/");
        return null;
      });

      get("/venue/:id", (request, response) -> {
        HashMap<String, Object> model = new HashMap<String, Object>();
        int id = Integer.parseInt(request.params("id"));
        Venue myVenue = Venue.find(id);
        model.put("venue", myVenue);
        model.put("template", "templates/venue.vtl");
        model.put("bands", Band.all());
        return new ModelAndView(model, layout);
      }, new VelocityTemplateEngine());

      post("/venue/update/:id", (request, response) -> {
        HashMap<String, Object> model = new HashMap<String, Object>();
        int id = Integer.parseInt(request.params("id"));
        Venue myVenue = Venue.find(id);
        int bandId = Integer.parseInt(request.queryParams("band_id"));
        Band myBand = Band.find(bandId);
        myVenue.addBand(myBand);
        model.put("venue", myVenue);
        model.put("template", "templates/venue.vtl");
        model.put("bands", Band.all());
        return new ModelAndView(model, layout);
        }, new VelocityTemplateEngine());

        post("/venue/update/name/:id", (request, response) -> {
          HashMap<String, Object> model = new HashMap<String, Object>();
          int id = Integer.parseInt(request.params("id"));
          Venue myVenue = Venue.find(id);
          String inputName = request.queryParams("name");
          myVenue.update(inputName);
          model.put("venue", myVenue);
          // model.put("template", "templates/band.vtl"); //page wont redirect to same page
          model.put("bands", Band.all());
          // return new ModelAndView(model, layout);
          // }, new VelocityTemplateEngine());
          response.redirect("/");
          return null;
        });

      post("/venue/delete/:id", (request, response) -> {
        HashMap<String, Object> model = new HashMap<String, Object>();
        int id = Integer.parseInt(request.params("id"));
        Venue myVenue = Venue.find(id);
        myVenue.delete();
        model.put("venue", myVenue);
          response.redirect("/");
          return null;
        });
    //
    // get("/tasks", (request,response) -> {
    //   HashMap<String, Object> model = new HashMap<String, Object>();
    //   List<Task> tasks = Task.all();
    //   model.put("tasks", tasks);
    //   model.put("template", "templates/tasks.vtl");
    //   return new ModelAndView(model, layout);
    // }, new VelocityTemplateEngine());
    //
    // get("/categories", (request, response) -> {
    //   HashMap<String, Object> model = new HashMap<String, Object>();
    //   List<Category> categories = Category.all();
    //   model.put("categories", categories);
    //   model.put("template", "templates/categories.vtl");
    //   return new ModelAndView(model, layout);
    // }, new VelocityTemplateEngine());
    //
    // get("/tasks/:id", (request,response) -> {
    //   HashMap<String, Object> model = new HashMap<String, Object>();
    //   int id = Integer.parseInt(request.params("id"));
    //   Task task = Task.find(id);
    //   model.put("task", task);
    //   model.put("allCategories", Category.all());
    //   model.put("template", "templates/task.vtl");
    //   return new ModelAndView(model, layout);
    // }, new VelocityTemplateEngine());
    //
    // get("/categories/:id", (request,response) ->{
    //   HashMap<String, Object> model = new HashMap<String, Object>();
    //   int id = Integer.parseInt(request.params("id"));
    //   Category category = Category.find(id);
    //   model.put("category", category);
    //   model.put("allTasks", Task.all());
    //   model.put("template", "templates/category.vtl");
    //   return new ModelAndView(model, layout);
    // }, new VelocityTemplateEngine());
    //
    // post("/tasks", (request, response) -> {
    //   HashMap<String, Object> model = new HashMap<String, Object>();
    //   String description = request.queryParams("description");
    //   Task newTask = new Task(description);
    //   newTask.save();
    //   response.redirect("/tasks");
    //   return null;
    // });
    //
    // post("/categories", (request, response) -> {
    //   HashMap<String, Object> model = new HashMap<String, Object>();
    //   String name = request.queryParams("name");
    //   Category newCategory = new Category(name);
    //   newCategory.save();
    //   response.redirect("/categories");
    //   return null;
    // });
    //
    // post("/add_tasks", (request, response) -> {
    //   int taskId = Integer.parseInt(request.queryParams("task_id"));
    //   int categoryId = Integer.parseInt(request.queryParams("category_id"));
    //   Category category = Category.find(categoryId);
    //   Task task = Task.find(taskId);
    //   category.addTask(task);
    //   response.redirect("/categories/" + categoryId);
    //   return null;
    // });
    //
    // post("/add_categories", (request, response) -> {
    //   int taskId = Integer.parseInt(request.queryParams("task_id"));
    //   int categoryId = Integer.parseInt(request.queryParams("category_id"));
    //   Category category = Category.find(categoryId);
    //   Task task = Task.find(taskId);
    //   task.addCategory(category);
    //   response.redirect("/tasks/" + taskId);
    //   return null;
    // });
    //
    // get("/tasks/:id/edit", (request, response) -> {
    //   HashMap<String, Object> model = new HashMap<String, Object>();
    //   Task task = Task.find(Integer.parseInt(request.params("id")));
    //   model.put("task", task);
    //   model.put("template", "templates/task-edit.vtl");
    //   return new ModelAndView(model, layout);
    // }, new VelocityTemplateEngine());
    //
    // post("/tasks/:id/edit", (request, response) -> {
    //   HashMap<String, Object> model = new HashMap<String, Object>();
    //   Task task = Task.find(Integer.parseInt(request.params("id")));
    //   String description = request.queryParams("description");
    //   task.update(description);
    //   response.redirect("/tasks");
    //   return null;
    //   });
    //
    // post("/tasks/:id", (request, response) -> {
    //   HashMap<String, Object> model = new HashMap<String, Object>();
    //   Task task = Task.find(Integer.parseInt(request.params("id")));
    //   task.delete();
    //   response.redirect("/tasks");
    //   return null;
    // });
    //
    // post("/tasks/:id/complete", (request, response) -> {
    //   HashMap<String, Object> model = new HashMap<String, Object>();
    //   Task task = Task.find(Integer.parseInt(request.params("id")));
    //   task.complete();
    //   response.redirect("/tasks");
    //   return null;
    // });
  }
}
