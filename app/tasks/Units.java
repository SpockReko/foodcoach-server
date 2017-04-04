package tasks;

import models.food.FoodItem;
import models.recipe.Amount;
import models.recipe.Ingredient;
import models.recipe.Menu;
import models.recipe.Recipe;
import models.user.Nutrient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by stefa on 2017-04-04.
 */
public class Units {
    /*
   Returns the total amount of nutrients per portion in several recipes
    */
    public static HashMap<Nutrient,Double> nutrientsContent(Menu menu) {
        HashMap<Nutrient, Double> nutrientsContent = new HashMap<Nutrient, Double>();

        for( Recipe recipe : menu.getRecipeList() ) {
            addToHashMap(nutrientsContent, Nutrient.CaloriKcal, recipe.getEnergyKcal() / recipe.getPortions());
            addToHashMap(nutrientsContent, Nutrient.Fat, recipe.getFat() / recipe.getPortions());
            addToHashMap(nutrientsContent, Nutrient.Protein, recipe.getProtein() / recipe.getPortions());
            addToHashMap(nutrientsContent, Nutrient.Carbohydrates, recipe.getCarbohydrates() / recipe.getPortions());
            addToHashMap(nutrientsContent, Nutrient.Fibre, recipe.getFibre() / recipe.getPortions());

            addToHashMap(nutrientsContent, Nutrient.VitaminAUG, recipe.getVitaminA() / recipe.getPortions());
            addToHashMap(nutrientsContent, Nutrient.VitaminB6MG, recipe.getVitaminB6() / recipe.getPortions());
            addToHashMap(nutrientsContent, Nutrient.VitaminB12UG, recipe.getVitaminB12() / recipe.getPortions());
            addToHashMap(nutrientsContent, Nutrient.VitaminCMG, recipe.getVitaminC() / recipe.getPortions());
            addToHashMap(nutrientsContent, Nutrient.VitaminDUG, recipe.getVitaminD() / recipe.getPortions());
            addToHashMap(nutrientsContent, Nutrient.VitaminEMG, recipe.getVitaminE() / recipe.getPortions());
            addToHashMap(nutrientsContent, Nutrient.ThiamineMG, recipe.getThiamine() / recipe.getPortions());
            addToHashMap(nutrientsContent, Nutrient.RiboflavinMG, recipe.getRiboflavin() / recipe.getPortions());
            addToHashMap(nutrientsContent, Nutrient.NiacinMG, recipe.getNiacin() / recipe.getPortions());
            addToHashMap(nutrientsContent, Nutrient.FolateUG, recipe.getFolate() / recipe.getPortions());
            addToHashMap(nutrientsContent, Nutrient.CalciumMG, recipe.getCalcium() / recipe.getPortions());
            addToHashMap(nutrientsContent, Nutrient.PhosphorusMG, recipe.getPhosphorus() / recipe.getPortions());
            addToHashMap(nutrientsContent, Nutrient.PotassiumMG, recipe.getPotassium() / recipe.getPortions());
            addToHashMap(nutrientsContent, Nutrient.MagnesiumMG, recipe.getMagnesium() / recipe.getPortions());
            addToHashMap(nutrientsContent, Nutrient.IronMG, recipe.getIron() / recipe.getPortions());
            addToHashMap(nutrientsContent, Nutrient.ZinkMG, recipe.getZink() / recipe.getPortions());
            addToHashMap(nutrientsContent, Nutrient.IodineUG, recipe.getIodine() / recipe.getPortions());
            addToHashMap(nutrientsContent, Nutrient.SeleniumUG, recipe.getSelenium() / recipe.getPortions());
        }

        return nutrientsContent;
    }
    /*
Adds term to current value for nutrient in HashMap
 */
    public static HashMap<Nutrient,Double> addToHashMap(HashMap<Nutrient,Double> hmap, Nutrient nutrient, Double additionalTerm) {
        if( !hmap.containsKey(nutrient) ) {
            hmap.put(nutrient,0D);
        }
        hmap.put(nutrient, hmap.get(nutrient) + additionalTerm);
        return hmap;
    }

    /*
Scales the need of nutrients according to number of recipes, assuming each meal is 30% of the daily intake
 */
    public static HashMap<Nutrient,Double> nutrientsNeedScaled (HashMap<Nutrient,Double> nutrientsNeedPerDay, int nrOfRecipes) {
        HashMap<Nutrient,Double> nutrientsNeedScaled = new HashMap<>();
        for( Nutrient nutrient : nutrientsNeedPerDay.keySet() ) {
            nutrientsNeedScaled.put(nutrient, 0.3*nrOfRecipes*nutrientsNeedPerDay.get(nutrient));
        }
        return nutrientsNeedScaled;
    }

    public static Float diff(FoodItem foodItem1, FoodItem fooditem2) {

        float sum = 0f;

        sum += Math.abs(foodItem1.getEnergyKcal() - fooditem2.getEnergyKcal());
        sum += Math.abs(foodItem1.getCarbohydrates() - fooditem2.getCarbohydrates())*20;
        sum += Math.abs(foodItem1.getFats().getFat() - fooditem2.getFats().getFat())*20;
        sum += Math.abs(foodItem1.getProtein() - fooditem2.getProtein())*20;
        sum += Math.abs(foodItem1.getFibre() - fooditem2.getFibre());

        sum += Math.abs(foodItem1.getVitamins().getVitaminA() - fooditem2.getVitamins().getVitaminA());
        sum += Math.abs(foodItem1.getVitamins().getVitaminD() - fooditem2.getVitamins().getVitaminD());
        sum += Math.abs(foodItem1.getVitamins().getVitaminE() - fooditem2.getVitamins().getVitaminE());
        sum += Math.abs(foodItem1.getVitamins().getVitaminB6() - fooditem2.getVitamins().getVitaminB6());
        sum += Math.abs(foodItem1.getVitamins().getVitaminB12() - fooditem2.getVitamins().getVitaminB12());
        sum += Math.abs(foodItem1.getVitamins().getVitaminC() - fooditem2.getVitamins().getVitaminC());
        sum += Math.abs(foodItem1.getVitamins().getVitaminK() - fooditem2.getVitamins().getVitaminK());

        sum += Math.abs(foodItem1.getMinerals().getCalcium() - fooditem2.getMinerals().getCalcium());
        sum += Math.abs(foodItem1.getMinerals().getFolate() - fooditem2.getMinerals().getCalcium());
        sum += Math.abs(foodItem1.getMinerals().getIodine() - fooditem2.getMinerals().getCalcium());
        sum += Math.abs(foodItem1.getMinerals().getIron() - fooditem2.getMinerals().getCalcium());
        sum += Math.abs(foodItem1.getMinerals().getMagnesium() - fooditem2.getMinerals().getCalcium());
        sum += Math.abs(foodItem1.getMinerals().getPhosphorus() - fooditem2.getMinerals().getCalcium());
        sum += Math.abs(foodItem1.getMinerals().getPotassium() - fooditem2.getMinerals().getCalcium());
        sum += Math.abs(foodItem1.getMinerals().getSalt() - fooditem2.getMinerals().getCalcium());
        sum += Math.abs(foodItem1.getMinerals().getSelenium() - fooditem2.getMinerals().getCalcium());
        sum += Math.abs(foodItem1.getMinerals().getSodium() - fooditem2.getMinerals().getCalcium());
        sum += Math.abs(foodItem1.getMinerals().getZink() - fooditem2.getMinerals().getCalcium());
        sum += Math.abs(foodItem1.getSugars().getSugars() - fooditem2.getSugars().getSugars());

        return sum;
    }

    private List<Ingredient> getIngredientsFromString(List<String> stringList) {
        List<Ingredient> ingredientList = new ArrayList<>();

        for (String name: stringList) {
            //TODO: Fix the error of the MYSQL call!
            List<FoodItem> foods;
            try {
                foods = FoodItem.find.where().contains("name", name).findList();
            }catch(Exception e){
                foods = null;
            }
            if( foods != null){
                for (FoodItem fi: foods) {
                    ingredientList.add(new Ingredient(fi,new Amount(100.0, Amount.Unit.GRAM)));
                }
            }
        }
        return ingredientList;
    }

}
