package controllers;

import algorithms.MenuAlgorithms;
import models.food.FoodItem;
import models.recipe.Amount;
import models.recipe.Menu;
import models.recipe.Recipe;
import models.recipe.ShoppingList;
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
            return ok(resultingWeekMenu.recipeListToString(resultingWeekMenu));
        return ok("nothing found!");

    }
    public Result weekmenu2(){
        List<Recipe> removeRecipeList = new ArrayList<>();
        int nrOfRecipes = 3;
        List<Recipe> allRecipes = Recipe.find.all();
        List<FoodItem> foods = new ArrayList<FoodItem>();
        List<Amount> amounts = new ArrayList<Amount>();

        foods.add(FoodItem.find.byId(120L));
        foods.add(FoodItem.find.byId(12L));
        amounts.add(new Amount(100, Amount.Unit.GRAM));
        amounts.add(new Amount(150, Amount.Unit.GRAM));

        MenuAlgorithms menuAlgorithmsInstant = new MenuAlgorithms(foods, amounts, allRecipes);
        menuAlgorithmsInstant.setNrOfRecipes(nrOfRecipes);
        Menu resultingWeekMenu = menuAlgorithmsInstant.weekMenuFromIngredientList(removeRecipeList);
        ShoppingList shoppingList=new ShoppingList(resultingWeekMenu, foods, amounts);

        if (resultingWeekMenu.getRecipeList().size() == menuAlgorithmsInstant.getNrOfRecipes())
            return ok(resultingWeekMenu.recipeListToString(resultingWeekMenu)+shoppingList.toString());
        return ok("nothing found!");
    }


}
