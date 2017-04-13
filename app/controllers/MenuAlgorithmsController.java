package controllers;

import algorithms.MenuAlgorithms;
import models.recipe.Menu;
import models.recipe.Recipe;
import models.user.User;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by fredrikkindstrom on 2017-03-15.
 */
public class MenuAlgorithmsController extends Controller {

    @Inject FormFactory formFactory;

    // POST /weekmenu
    public Result weekmenu() {

        // TODO: Refractor: Logic should not be in a controller

        DynamicForm requestData = formFactory.form().bindFromRequest();

        User user;
        int nrOfRecipes;
        List<Recipe> removeRecipeList = new ArrayList<>();

        if (requestData.get("sex") != null) {  // If there exist input from client.


            //Change the format to suit user constructor variables.
            Integer age =
                requestData.get("age") != null ? Integer.parseInt(requestData.get("age")) : 25;
            String sex = requestData.get("sex"); // output: "women" or "male"
            User.Sex sexEnum = sex.equals("women") ? User.Sex.FEMALE : User.Sex.MALE;
            Double weight = Double.parseDouble(requestData.get("weight"));
            Double length = Double.parseDouble(requestData.get("length"));
            Double activityLevel = Double.parseDouble(requestData.get("activityLevel")); // low: 1.2
            String goal = requestData.get("goal");// low: 1
            User.Goal goalEnum = goal.equals("1") ?
                User.Goal.DECREASE :
                goal.equals("2") ? User.Goal.STAY : User.Goal.INCREASE;
            String allergy = requestData.get("allergy");// sträng
            ArrayList<String> allergyList = new ArrayList<>();
            for (String n : allergy.split(" ")) {
                allergyList.add(n);
            }

            user = new User(sexEnum, activityLevel, weight, length, age, goalEnum, allergyList);
            nrOfRecipes = Integer.parseInt(requestData.get("nrOfRecipe"));
            String recipe = requestData.get("removeRecipe");

            for (String n : recipe.split(" ")) {
                removeRecipeList.add(Recipe.find.where().contains("title", n).findUnique());
            }
        } else { // If we run it from the "Server"

            user = new User();
            nrOfRecipes = 3;

        }


        List<Recipe> allRecipes = Recipe.find.all();
        MenuAlgorithms menuAlgorithmsInstant = new MenuAlgorithms(user, allRecipes);
        menuAlgorithmsInstant.setNrOfRecipes(nrOfRecipes);

        Menu resultingWeekMenu = menuAlgorithmsInstant.calculateWeekMenu(removeRecipeList);

        if (resultingWeekMenu.getRecipeList().size() == menuAlgorithmsInstant.getNrOfRecipes())
            return ok(resultingWeekMenu.recipeListToString());
        return ok("nothing found!");

    }


}
