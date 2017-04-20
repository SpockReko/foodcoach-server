package controllers;

import algorithms.MenuAlgorithms;
import com.fasterxml.jackson.databind.JsonNode;
import models.food.FoodItem;
import models.food.fineli.Food;
import models.recipe.Amount;
import models.recipe.Menu;
import models.recipe.Recipe;
import models.recipe.ShoppingList;
import models.user.User;
import play.api.libs.json.Json;
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

    // POST /menu
    // GET  /menu
    public Result menu() {

        // TODO: Refractor: Logic should not be in a controller

        DynamicForm requestData = formFactory.form().bindFromRequest();

        User user;
        int nrOfRecipes;
        List<Recipe> removeRecipeList = new ArrayList<>();

        if (requestData.data().size() != 0) {  // If there exist input from client.

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
            //for (String n : allergy.split(" ")) {
            //    allergyList.add(n);
            //}

            user = new User(sexEnum, activityLevel, weight, length, age, goalEnum, allergyList);
            nrOfRecipes = Integer.parseInt(requestData.get("nrOfRecipe"));
            //String recipe = requestData.get("removeRecipe");

            //for (String n : recipe.split(" ")) {
            //    removeRecipeList.add(Recipe.find.where().contains("title", n).findUnique());
            //}
            List<Recipe> allRecipes = Recipe.find.all();
            MenuAlgorithms menuAlgorithmsInstant = new MenuAlgorithms(allRecipes, removeRecipeList, nrOfRecipes);
            menuAlgorithmsInstant.setNrOfRecipes(nrOfRecipes);

            Menu resultingMenu = menuAlgorithmsInstant.calculateWeekMenu(user);

            return ok((JsonNode) Json.parse(resultingMenu.recipeListToString()));

        } else { // If we run it from the "Server"

            user = new User();
            nrOfRecipes = 3;

            List<Recipe> allRecipes = Recipe.find.all();
            MenuAlgorithms menuAlgorithmsInstant = new MenuAlgorithms(allRecipes, removeRecipeList, nrOfRecipes);
            menuAlgorithmsInstant.setNrOfRecipes(nrOfRecipes);

            Menu resultingMenu = menuAlgorithmsInstant.calculateWeekMenu(user);

            if (resultingMenu.getRecipeList().size() == menuAlgorithmsInstant.getNrOfRecipes())
                return ok(resultingMenu.recipeListToString());
            return ok("nothing found!");

        }
    }

    // GET     /menu2
    public Result menu2(){

        List<Recipe> removeRecipeList = new ArrayList<>();
        int nrOfRecipes = 3;
        List<Recipe> allRecipes = Recipe.find.all();
        List<Food> foods = new ArrayList<>();
        List<Amount> amounts = new ArrayList<Amount>();

        foods.add(Food.find.byId(120L));
        foods.add(Food.find.byId(12L));
        amounts.add(new Amount(100, Amount.Unit.GRAM));
        amounts.add(new Amount(150, Amount.Unit.GRAM));

        MenuAlgorithms menuAlgorithmsInstant = new MenuAlgorithms(allRecipes, removeRecipeList, nrOfRecipes);
        Menu resultingMenu = menuAlgorithmsInstant.calculateWeekMenu(foods, amounts);
        ShoppingList shoppingList=new ShoppingList(resultingMenu, foods, amounts);

        if (resultingMenu.getRecipeList().size() == menuAlgorithmsInstant.getNrOfRecipes())
            return ok(resultingMenu.recipeListToString());
        return ok("nothing found!");
    }
}
