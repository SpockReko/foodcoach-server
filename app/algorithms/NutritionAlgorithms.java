package algorithms;
import java.util.*;

import models.food.FoodItem;
import models.recipe.*;
import models.user.Nutrient;

/**
 * Created by louiserost on 2017-03-06.
 */
public class NutritionAlgorithms {

    public static Double L2NormTerm(Double percentageOfRDI) {
        return Math.pow(1 - percentageOfRDI, 2);
    }

    /*
    Returns the distance from the optimal point
     */
    public static Double L2Norm(HashMap<Nutrient,Double> nutrientsNeedPerDay, HashMap<Nutrient,Double> nutrientsContent, HashMap<Nutrient,Double> overdoseValues, Menu menu) {
        HashMap<Nutrient,Double> nutrientsNeed = nutrientsNeedScaled(nutrientsNeedPerDay,menu.getRecipeList().size());
        Nutrient nutrient;
        Double sum = 0D;

        for( Map.Entry<Nutrient,Double> entry : nutrientsNeed.entrySet() ) {
            nutrient = entry.getKey();
            if(nutrientsNeed.containsKey(nutrient) && nutrientsContent.containsKey(nutrient) && overdoseValues.containsKey(nutrient)) {
                Double percentageOfRDI = filterLimit(
                                        nutrient,
                                        nutrientsContent.get(nutrient),
                                        nutrientsNeed.get(nutrient),
                                        overdoseValues.get(nutrient), menu);
                sum += L2NormTerm(percentageOfRDI);
                addNutrionInfoToMenu(menu, nutrient, percentageOfRDI);
            }
        }
        return Math.sqrt(sum);
    }

    /*
    If RDI of the nutrient is fulfilled, percentage is set to optimal value (100%). Also informs if the nutrient is overdosed.
     */
    public static Double filterLimit (Nutrient nutrient, Double nutrientContent, Double nutrientNeed, Double overdose, Menu menu) {
        Double percentageNutrient = nutrientContent/nutrientNeed;

        if( percentageNutrient > 1D && nutrientContent < overdose ) {
            percentageNutrient = 1D;
        } else if (nutrientContent > overdose ){
            // TODO lägga till meddelande om att det är för mycket av näringsämnet
            menu.addComment("Overdose on " + nutrient + ", contains " + percentageNutrient);
            System.out.print(menu.recipeListToString());
            System.out.print("\nOBS!!! Överdosering av ");
            System.out.print(nutrient);
            System.out.print("\nInnehåll: "+nutrientContent+". Behov: "+nutrientNeed+"\n\n");

        }
        return percentageNutrient;
    }


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


    private static void addNutrionInfoToMenu(Menu menu, Nutrient nutrient, Double percentageOfRDI) {
        if((nutrient + "").length() < 7) {
            menu.addComment(nutrient + ":\t\t" +
                    percentageOfRDI);
        }else{
            menu.addComment(nutrient + ":\t" +
                    percentageOfRDI);
        }
    }

    //TODO: Använd dessa metoderna någonstans!
    public static List<FoodItem> SortByTheDifference(Ingredient ingredient){

        List<FoodItem> foods = FoodItem.find.all();
        foods = QuicksortFoodItem.sort(foods, ingredient.getFoodItem());
        return foods;

    }

    public static Recipe changeIngredient(Recipe recipe, Ingredient ingredient, FoodItem foodItem){

        List<Ingredient> recipeIngredient = recipe.ingredients;
        recipeIngredient.remove(ingredient);
        recipeIngredient.add(new Ingredient(foodItem,ingredient.getAmount()));
        return recipe;
        // TODO: Om inte ingredients listan ej har shared reference så måste vi köra koden här under.
        //return new Recipe(recipe.getTitle()+ " changed", recipe.getPortions(), recipeIngredient);

    }

}
