package controllers;

import algorithms.WeekMenu;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.recipe.Recipe;
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
        String dateBorn = requestData.get("dateBorn");
        String sex = requestData.get("sex");
        String weight = requestData.get("weight");
        String length = requestData.get("length");
        String activityLevel = requestData.get("activityLevel");
        String goal = requestData.get("goal");
        String allergy = requestData.get("allergy");

        Map<String, String> map = new HashMap<>();
        map.put("dateBorn", dateBorn);
        map.put("sex", sex);
        map.put("weight", weight);
        map.put("length", length);
        map.put("activityLevel", activityLevel);
        map.put("goal", goal);
        map.put("allergy", allergy);

        //return ok(Json.toJson(map));
        return weekMenuTest();
    }

    //Testa week menu
    public Result weekMenuTest(){
        List<Recipe> allRecipes = Recipe.find.all();
        List<Recipe> chosenRecipes = new ArrayList<Recipe>();

        WeekMenu weekMenu = new WeekMenu();
        //TODO: Få följande värden ifrån användaren genom client
        weekMenu.setNrOfRecipes(3);
        weekMenu.setAllRecipes(allRecipes);
        weekMenu.setDesiredValue(0.0D);
        weekMenu.calculateWeekMenu(allRecipes.size()-1,chosenRecipes);
        List<Recipe> resultingWeekMenu = weekMenu.getOptimalMenu();
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
        return ok(resultingWeekMenu.get(0).getTitle()+"\n"+resultingWeekMenu.get(1).getTitle());
    }
}
