package algorithms;
import java.util.*;

import models.food.Food;
import models.recipe.*;
import models.food.Nutrient;
import org.apache.commons.math3.util.Precision;

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
            menu.addComment("Överdosering av " + nutrient + ", innehåller " + percentageNutrient);
            System.out.print(menu.recipeListToString(new ShoppingList(menu)));
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
        HashMap<Nutrient, Double> nutrientsContent = new HashMap<>();

        for (Recipe recipe : menu.getRecipeList()) {
            addToHashMap(nutrientsContent, Nutrient.ENERGY_KCAL, recipe.getNutrientPerPortion(Nutrient.ENERGY_KCAL));
            addToHashMap(nutrientsContent, Nutrient.FAT, recipe.getNutrientPerPortion(Nutrient.FAT));
            addToHashMap(nutrientsContent, Nutrient.PROTEIN, recipe.getNutrientPerPortion(Nutrient.PROTEIN));
            addToHashMap(nutrientsContent, Nutrient.CARBOHYDRATES, recipe.getNutrientPerPortion(Nutrient.CARBOHYDRATES));
            addToHashMap(nutrientsContent, Nutrient.FIBRE, recipe.getNutrientPerPortion(Nutrient.FIBRE));
            addToHashMap(nutrientsContent, Nutrient.VITAMIN_A, recipe.getNutrientPerPortion(Nutrient.VITAMIN_A));
            addToHashMap(nutrientsContent, Nutrient.VITAMIN_B6, recipe.getNutrientPerPortion(Nutrient.VITAMIN_B6));
            addToHashMap(nutrientsContent, Nutrient.VITAMIN_B12, recipe.getNutrientPerPortion(Nutrient.VITAMIN_B12));
            addToHashMap(nutrientsContent, Nutrient.VITAMIN_C, recipe.getNutrientPerPortion(Nutrient.VITAMIN_C));
            addToHashMap(nutrientsContent, Nutrient.VITAMIN_D, recipe.getNutrientPerPortion(Nutrient.VITAMIN_D));
            addToHashMap(nutrientsContent, Nutrient.VITAMIN_E, recipe.getNutrientPerPortion(Nutrient.VITAMIN_E));
            addToHashMap(nutrientsContent, Nutrient.THIAMINE, recipe.getNutrientPerPortion(Nutrient.THIAMINE));
            addToHashMap(nutrientsContent, Nutrient.RIBOFLAVIN, recipe.getNutrientPerPortion(Nutrient.RIBOFLAVIN));
            addToHashMap(nutrientsContent, Nutrient.NIACIN, recipe.getNutrientPerPortion(Nutrient.NIACIN));
            addToHashMap(nutrientsContent, Nutrient.FOLATE, recipe.getNutrientPerPortion(Nutrient.FOLATE));
            addToHashMap(nutrientsContent, Nutrient.CALCIUM, recipe.getNutrientPerPortion(Nutrient.CARBOHYDRATES));
            addToHashMap(nutrientsContent, Nutrient.PHOSPHORUS, recipe.getNutrientPerPortion(Nutrient.PHOSPHORUS));
            addToHashMap(nutrientsContent, Nutrient.POTASSIUM, recipe.getNutrientPerPortion(Nutrient.POTASSIUM));
            addToHashMap(nutrientsContent, Nutrient.MAGNESIUM, recipe.getNutrientPerPortion(Nutrient.MAGNESIUM));
            addToHashMap(nutrientsContent, Nutrient.IRON, recipe.getNutrientPerPortion(Nutrient.IRON));
            addToHashMap(nutrientsContent, Nutrient.ZINC, recipe.getNutrientPerPortion(Nutrient.ZINC));
            addToHashMap(nutrientsContent, Nutrient.IODINE, recipe.getNutrientPerPortion(Nutrient.IODINE));
            addToHashMap(nutrientsContent, Nutrient.SELENIUM, recipe.getNutrientPerPortion(Nutrient.SELENIUM));
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
                    (int)Precision.round(100*percentageOfRDI, 0)+"%");
        }else{
            menu.addComment(nutrient + ":\t" +
                    (int)Precision.round(100*percentageOfRDI, 0)+"%");
        }
    }

    //TODO: Använd dessa metoderna någonstans!
    public static List<Food> SortByTheDifference(Ingredient ingredient){

        List<Food> foods = Food.find.all();
        foods = QuicksortFoodItem.sort(foods, ingredient.getFood());
        return foods;

    }

    public static Recipe changeIngredient(Recipe recipe, Ingredient ingredient, Food foodItem){

        List<Ingredient> recipeIngredient = recipe.ingredients;
        recipeIngredient.remove(ingredient);
        recipeIngredient.add(new Ingredient(foodItem,ingredient.getAmount()));
        return recipe;
        // TODO: Om inte ingredients listan ej har shared reference så måste vi köra koden här under.
        //return new Recipe(recipe.getTitle()+ " changed", recipe.getPortions(), recipeIngredient);

    }

}
