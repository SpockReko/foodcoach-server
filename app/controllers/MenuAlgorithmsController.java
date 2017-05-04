package controllers;

import algorithms.MenuAlgorithms;
import com.fasterxml.jackson.databind.JsonNode;
import models.food.Food;
import models.recipe.*;
import models.user.User;
import play.api.libs.json.Json;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.List;
import java.util.ArrayList;

import static models.recipe.Amount.Unit.GRAM;

/**
 * Created by fredrikkindstrom on 2017-03-15.
 */
public class MenuAlgorithmsController extends Controller {

    @Inject
    FormFactory formFactory;

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

            Menu resultingMenu = menuAlgorithmsInstant.calculateMenuNutrition(user);

            return ok((JsonNode) Json.parse(resultingMenu.recipeListToString(new ShoppingList(resultingMenu))));

        } else { // If we run it from the "Server"

            user = new User();
            nrOfRecipes = 3;

            List<Recipe> allRecipes = Recipe.find.all();
            MenuAlgorithms menuAlgorithmsInstant = new MenuAlgorithms(allRecipes, removeRecipeList, nrOfRecipes);
            menuAlgorithmsInstant.setNrOfRecipes(nrOfRecipes);

            Menu resultingMenu = menuAlgorithmsInstant.calculateMenuNutrition(user);

            if (resultingMenu.getRecipeList().size() == menuAlgorithmsInstant.getNrOfRecipes())
                return ok(resultingMenu.recipeListToString(new ShoppingList(resultingMenu)));
            return ok("nothing found!");

        }
    }

    /**
     * Dessa tre användare vill Olof ta bort. men får använda users ifrån databaen
     */

    /*

    public Result menuStefan() {
        User user;
        int nrOfRecipes;
        List<Recipe> removeRecipeList = new ArrayList<>();
        user = new User("Stefan");
        nrOfRecipes = 3;

        List<Recipe> allRecipes = Recipe.find.all().subList(0,900);
        //int[] intArr = {10,20,30,40,50,60,100,300,450,900};
        //String allText = "";
        //String allText2 = "";
        MenuAlgorithms menuAlgorithmsInstant = new MenuAlgorithms(allRecipes, removeRecipeList, nrOfRecipes);
        menuAlgorithmsInstant.setNrOfRecipes(nrOfRecipes);
        Menu resultingMenu = menuAlgorithmsInstant.calculateMenuNutrition(user);
        int all = menuAlgorithmsInstant.getAllRecepie();
        for (int a = 0; a < intArr.length; a++) {

            int nr = intArr[a];
            int turns = all / nr;
            long biggestDiff = 0;
            for (int i = 0; i < turns; i++) {
                long before = System.currentTimeMillis();
                List<Recipe> allRecipesSub = allRecipes.subList(i * nr, i * nr + nr);
                menuAlgorithmsInstant = new MenuAlgorithms(allRecipesSub, removeRecipeList, 10);
                resultingMenu = menuAlgorithmsInstant.calculateMenuNutrition(user);
                long diffAfter = System.currentTimeMillis() - before;
                if (diffAfter > biggestDiff) {
                    biggestDiff = diffAfter;
                }
            }
            String text = "("+ intArr[a] + "," + biggestDiff + "),";
            allText = allText + text;

            String text2 = "{"+ intArr[a] + "," + biggestDiff + "},";
            allText2 = allText2 + text2;

        }

        if (resultingMenu.getRecipeList().size() == menuAlgorithmsInstant.getNrOfRecipes())
            return ok(all + " nr of recepies\n" + resultingMenu.recipeListToString(new ShoppingList(resultingMenu)));
            //return ok(print(differens));
            //return ok(allText + "\n" + allText2);
        return ok("nothing found!");
    }


    public String print(long[] arr){
        String result = "";
        int i = 0;
        for (long l : arr){
            i++;
            result = result + "(" + i + "," + l + "),";
        }
        return result;
    }
/*
    // GET  /bob/menu/nutrient
    public Result menuBob() {
        User user;
        int nrOfRecipes;
        List<Recipe> removeRecipeList = new ArrayList<>();

        user = new User("Bob");
        nrOfRecipes = 3;

        List<Recipe> allRecipes = Recipe.find.all();
        MenuAlgorithms menuAlgorithmsInstant = new MenuAlgorithms(allRecipes, removeRecipeList, nrOfRecipes);
        menuAlgorithmsInstant.setNrOfRecipes(nrOfRecipes);
        Menu resultingMenu = menuAlgorithmsInstant.calculateMenuNutrition(user);

        if (resultingMenu.getRecipeList().size() == menuAlgorithmsInstant.getNrOfRecipes())
            return ok(resultingMenu.recipeListToString(new ShoppingList(resultingMenu)));
        return ok("nothing found!");


    }


    // GET  /alice/menu/nutrient
    public Result menuAlice() {

        User user;
        int nrOfRecipes;
        List<Recipe> removeRecipeList = new ArrayList<>();

        user = new User("Alice");
        nrOfRecipes = 3;

        List<Recipe> allRecipes = Recipe.find.all();
        MenuAlgorithms menuAlgorithmsInstant = new MenuAlgorithms(allRecipes, removeRecipeList, nrOfRecipes);
        menuAlgorithmsInstant.setNrOfRecipes(nrOfRecipes);
        Menu resultingMenu = menuAlgorithmsInstant.calculateMenuNutrition(user);

        if (resultingMenu.getRecipeList().size() == menuAlgorithmsInstant.getNrOfRecipes())
            return ok(resultingMenu.recipeListToString(new ShoppingList(resultingMenu)));
        return ok("nothing found!");
    } */

    // GET /menu/nutrient/name/:name/:nrOfRecipes
    public Result menuByName(String userName, int nrOfRecipes) {

        User user = new User(userName);
        List<Recipe> removeRecipeList = new ArrayList<>();

        List<Recipe> allRecipes = Recipe.find.all();
        MenuAlgorithms menuAlgorithmsInstant = new MenuAlgorithms(allRecipes, removeRecipeList, nrOfRecipes);
        Menu resultingMenu = menuAlgorithmsInstant.calculateMenuNutrition(user);

        /* TODO: Används till komplexitets analys av algorithmen! //Stefan
        */
        /*if(userName.equals("Test")) {
            int[] intArr = {10};//, 20, 30, 40, 50, 60, 100, 300, 450};
            String allText = "";
            String allText2 = "";
            int all = menuAlgorithmsInstant.getAllRecepie();
            for (int a = 0; a < intArr.length; a++) {

                int nr = intArr[a];
                int turns = all / nr;
                long biggestDiff = 0;
                for (int i = 0; i < turns; i++) {
                    long before = System.currentTimeMillis();
                    List<Recipe> allRecipesSub = allRecipes.subList(i * nr, i * nr + nr);
                    menuAlgorithmsInstant = new MenuAlgorithms(allRecipesSub, removeRecipeList, 1);
                    resultingMenu = menuAlgorithmsInstant.calculateMenuNutrition(user);
                    long diffAfter = System.currentTimeMillis() - before;
                    if (diffAfter > biggestDiff) {
                        biggestDiff = diffAfter;
                    }
                }
                String text = "(" + intArr[a] + "," + biggestDiff + "),";
                allText = allText + text;

                String text2 = "{" + intArr[a] + "," + biggestDiff + "},";
                allText2 = allText2 + text2;

            }
            return ok(menuAlgorithmsInstant.getAllRecepie() + allText + "\n" + allText2);
        }
*/

        if (resultingMenu.getRecipeList().size() == menuAlgorithmsInstant.getNrOfRecipes())
            return ok(user.firstName + " " + resultingMenu.recipeListToString(new ShoppingList(resultingMenu)));
        return ok("Hittade ingen meny!");
    }


    // GET     /menu/economi/;nrOfRecipes
    public Result menuEconomi(int nrOfRecipes) {
        List<Recipe> removeRecipeList = new ArrayList<>();
        List<Recipe> allRecipes = Recipe.find.all();
        List<Ingredient> ingredientsAtHome = new ArrayList<>();

        ingredientsAtHome.add(new Ingredient(Food.find.byId(528L),new Amount(200, GRAM)));
        ingredientsAtHome.add(new Ingredient(Food.find.byId(436L),new Amount(100, GRAM)));
        ingredientsAtHome.add(new Ingredient(Food.find.byId(822L),new Amount(100, GRAM)));

        MenuAlgorithms menuAlgorithmsInstant = new MenuAlgorithms(allRecipes, removeRecipeList, nrOfRecipes);
        Menu resultingMenu = menuAlgorithmsInstant.calculateWeekMenu(ingredientsAtHome);
        ShoppingList shoppingList = new ShoppingList(resultingMenu, ingredientsAtHome);

        if (resultingMenu.getRecipeList().size() == menuAlgorithmsInstant.getNrOfRecipes())
            return ok(resultingMenu.recipeListToString(shoppingList));
            //return menuByName("Test", nrOfRecipes);
        return ok("Hittade ingen meny!");
    }

    // GET     /menu/CO2/;nrOfRecipes
    public Result menuCO2(int nrOfRecipes) {

        List<Recipe> removeRecipeList = new ArrayList<>();
        List<Recipe> allRecipes = Recipe.find.all();
        List<Ingredient> ingredientsAtHome = new ArrayList<>();

        ingredientsAtHome.add(new Ingredient(Food.find.byId(528L),new Amount(200, GRAM)));
        ingredientsAtHome.add(new Ingredient(Food.find.byId(436L),new Amount(100, GRAM)));
        ingredientsAtHome.add(new Ingredient(Food.find.byId(822L),new Amount(100, GRAM)));


        MenuAlgorithms menuAlgorithmsInstant = new MenuAlgorithms(allRecipes, removeRecipeList, nrOfRecipes);
        Menu resultingMenu = menuAlgorithmsInstant.calculateWeekMenuMinimalCO2(ingredientsAtHome);
        ShoppingList shoppingList = new ShoppingList(resultingMenu, ingredientsAtHome);

        if (resultingMenu.getRecipeList().size() == menuAlgorithmsInstant.getNrOfRecipes())
            return ok(resultingMenu.recipeListToString(shoppingList));
        return ok("Hittade ingen meny!");
    }



    public Result menuShopinglist(int nrOfRecipes) {
        List<Recipe> removeRecipeList = new ArrayList<>();
        List<Recipe> allRecipes = Recipe.find.all();
        List<Ingredient> ingredientsAtHome = new ArrayList<>();

        ingredientsAtHome.add(new Ingredient(Food.find.byId(528L), new Amount(200, GRAM)));
        ingredientsAtHome.add(new Ingredient(Food.find.byId(436L), new Amount(100, GRAM)));
        ingredientsAtHome.add(new Ingredient(Food.find.byId(822L), new Amount(100, GRAM)));

        MenuAlgorithms menuAlgorithmsInstant = new MenuAlgorithms(allRecipes, removeRecipeList, nrOfRecipes);
        Menu resultingMenu = menuAlgorithmsInstant.calculateWeekMenuMinimalShoppingList(ingredientsAtHome);
        ShoppingList shoppingList = new ShoppingList(resultingMenu, ingredientsAtHome);

        if (resultingMenu.getRecipeList().size() == menuAlgorithmsInstant.getNrOfRecipes())
            return ok(resultingMenu.recipeListToString(shoppingList));
        //return menuByName("Test", nrOfRecipes);
        return ok("Hittade ingen meny!");
    }
}