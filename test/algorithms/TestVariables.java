package algorithms;

import models.food.Fats;
import models.food.FoodItem;
import models.food.Sugars;
import models.recipe.Amount;
import models.recipe.Ingredient;
import models.recipe.Recipe;
import models.user.User;

import java.util.ArrayList;
import java.util.List;

import static models.recipe.Amount.Unit.GRAM;
import static models.user.User.Sex.FEMALE;

/**
 * Created by macbookl on 10/04/17.
 */
public class TestVariables {
    public static User user1=new User(FEMALE, 1.5, 52, 160, 21, User.Goal.STAY, null);
    public static List<Ingredient> ingrList1=new ArrayList<Ingredient>();
//    public static Ingredient ingredient1=new Ingredient(FoodItem.find.byId(130L), new Amount(100, GRAM));
//    public static Ingredient ingredient1=new Ingredient(new Amount(100, GRAM),
//    new FoodItem("name1", 30L, "project", 2000F, 100F, 50F,
//    10F, 30F, 20F, 40F, 0F, 5F, new Sugars(1F, 1F, 1F, 1F),
//    new Fats(), null, null ));
    public static Recipe recipe1=new Recipe("Recipe1", 4, ingrList1);
    public static Recipe rec=Recipe.find.byId(20L);

}
