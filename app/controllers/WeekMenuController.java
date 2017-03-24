package controllers;

import algorithms.WeekMenu;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.recipe.Recipe;
import models.user.User;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by fredrikkindstrom on 2017-03-15.
 */
public class WeekMenuController extends Controller {

    @Inject FormFactory formFactory;

    // POST /weekmenu
    public Result weekMenu() {

        DynamicForm requestData = formFactory.form().bindFromRequest();

        User user;
        int nrOfRecipes;

        if(requestData.get("sex") != null) {
            Integer age = requestData.get("age") != null ? Integer.parseInt(requestData.get("age")) : 25;

            String sex = requestData.get("sex"); // output: "women" or "male"
            User.Sex sexEnum = sex.equals("women") ? User.Sex.FEMALE : User.Sex.MALE;
            Double weight = Double.parseDouble(requestData.get("weight"));
            Double length = Double.parseDouble(requestData.get("length"));
            Double activityLevel = Double.parseDouble(requestData.get("activityLevel")); // low: 1.2
            String goal = requestData.get("goal");// low: 1
            User.Goal goalEnum = goal.equals("1") ? User.Goal.DECREASE : goal.equals("2") ? User.Goal.STAY : User.Goal.INCREASE;
            String allergy = requestData.get("allergy");// sträng
            ArrayList<String> allergyList = new ArrayList<>();
            for (String n : allergy.split(" ")) {
                allergyList.add(n);
            }
            nrOfRecipes = Integer.parseInt(requestData.get("nrOfRecipe"));

            user = new User(sexEnum, activityLevel, weight, length, age, goalEnum, allergyList);

        } else {

            user = new User();
            nrOfRecipes = 3;
        }

        List<Recipe> allRecipes = Recipe.find.all();

        //List<Recipe> filteredRecipes =
        //        somewhere.removeRecipesFromListContainsGivenIngredients(
        //                allRecipes,
        //                allergyList);
        // weekmenu(user)?


        WeekMenu weekMenuInstant = new WeekMenu(user);
        weekMenuInstant.setNrOfRecipes(nrOfRecipes);

        //return ok(Json.toJson(map));
        return weekMenuTest();

    }


    //Testa week menu
    public Result weekMenuTest(){
        List<Recipe> allRecipes = Recipe.find.all();
        List<Recipe> chosenRecipes = new ArrayList<Recipe>();

        WeekMenu weekMenu = new WeekMenu(new User());
        //TODO: Få följande värden ifrån användaren genom client
        weekMenu.setNrOfRecipes(3);
        weekMenu.setAllRecipes(allRecipes);
        List<Recipe> resultingWeekMenu = weekMenu.calculateWeekMenu();
/*
        System.out.println(resultingWeekMenu.size());
        ObjectNode json = Json.newObject();
        ArrayNode array = json.putArray("recipe");
        for (Recipe recipe : resultingWeekMenu) {
            System.out.println(recipe.getTitle());
            ObjectNode node = Json.newObject();
            node.put("titel",recipe.getTitle());
            array.add(node);
        }
        return ok(json);
*/
        if(resultingWeekMenu.size() == weekMenu.getNrOfRecipes())
            return ok(resultingWeekMenu.get(0).getTitle()+"\n"+resultingWeekMenu.get(1).getTitle()+"\n"+resultingWeekMenu.get(2).getTitle());
        return ok("nothing found!");
    }
}
