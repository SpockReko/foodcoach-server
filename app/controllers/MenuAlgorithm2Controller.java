package controllers;

import algorithms.MenuAlgorithm2;
import algorithms.MenuAlgorithms;
import models.food.FoodItem;
import models.recipe.Menu;
import models.recipe.Recipe;
import models.user.User;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by macbookl on 11/04/17.
 */
public class MenuAlgorithm2Controller extends Controller {

        @Inject
        FormFactory formFactory;

        // POST /weekmenu2
        public Result weekmenu2() {

            DynamicForm requestData = formFactory.form().bindFromRequest();

            List<Recipe> allRecipes = Recipe.find.all();
            List<FoodItem> foods = new ArrayList<FoodItem>();
            foods.add(FoodItem.find.byId(120L));
            foods.add(FoodItem.find.byId(150L));
            foods.add(FoodItem.find.byId(10L));
            MenuAlgorithm2 menuAlgorithm = new MenuAlgorithm2(foods, allRecipes);
            Menu resultingWeekMenu = menuAlgorithm.calculateWeekMenu();

            if (resultingWeekMenu.getRecipeList().size() == 3)
                return ok(resultingWeekMenu.recipeListToString(resultingWeekMenu));
            return ok("nothing found!");

        }
}
