package algorithms;
import java.util.*;
import models.recipe.*;

/**
 * Created by louiserost on 2017-03-06.
 */
public class Algorithms {

    public static Double L2NormTerm(Double procentNutrient) {
        return Math.pow(1 - procentNutrient, 2);
    }

    public static Double L2Norm(HashMap<String,Double> nutrientsNeed, HashMap<String,Double> nutrientsContent) {
        Double sum = 0D;
        String nutrient;
        for( Map.Entry<String,Double> entry : nutrientsNeed.entrySet() ) {
            nutrient = entry.getKey();
            if(nutrientsNeed.containsKey(nutrient) && nutrientsContent.containsKey(nutrient)) {
                sum += L2NormTerm(nutrientsContent.get(nutrient) / nutrientsNeed.get(nutrient));
            }
        }

        return sum;
    }

    /*
    Returns total amount of nutrients for several recipes
     */
    public static HashMap<String,Double> nutrientsContent(List<Recipe> recipes) {
        HashMap<String, Double> nutrientsContent = new HashMap<String, Double>();

        for( Recipe recipe : recipes ) {
            addToHashMap(nutrientsContent,"bmr",recipe.getEnergyKcal());
            addToHashMap(nutrientsContent,"fat",recipe.getFat());
            addToHashMap(nutrientsContent,"protein",recipe.getProtein());
            addToHashMap(nutrientsContent,"carbohydrates",recipe.getCarbohydrates());

            addToHashMap(nutrientsContent,"vitaminA", recipe.getVitaminA());
            addToHashMap(nutrientsContent,"vitaminB6", recipe.getVitaminB6());
            addToHashMap(nutrientsContent,"vitaminB12", recipe.getVitaminB12());
            addToHashMap(nutrientsContent,"vitaminC", recipe.getVitaminC());
            addToHashMap(nutrientsContent,"vitaminD", recipe.getVitaminD());
            addToHashMap(nutrientsContent,"vitaminE", recipe.getVitaminE());
            addToHashMap(nutrientsContent,"thiamine", recipe.getThiamine());
            addToHashMap(nutrientsContent,"riboflavin", recipe.getRiboflavin());
            addToHashMap(nutrientsContent,"niacin", recipe.getNiacin());
            addToHashMap(nutrientsContent,"folate", recipe.getFolate());
            addToHashMap(nutrientsContent,"calcium", recipe.getCalcium());
            addToHashMap(nutrientsContent,"phosphorus", recipe.getPhosphorus());
            addToHashMap(nutrientsContent,"potassium", recipe.getPotassium());
            addToHashMap(nutrientsContent,"magnesium", recipe.getMagnesium());
            addToHashMap(nutrientsContent,"iron", recipe.getIron());
            addToHashMap(nutrientsContent,"zink", recipe.getZink());
            addToHashMap(nutrientsContent,"iodine", recipe.getIodine());
            addToHashMap(nutrientsContent,"selenium", recipe.getSelenium());
        }

        return nutrientsContent;
    }


    public static HashMap<String,Double> addToHashMap(HashMap<String,Double> hmap, String string, Double additionalTerm) {
        if( !hmap.containsKey(string) ) {
            hmap.put(string,0D);
        }
        hmap.put(string,hmap.get(string)+additionalTerm);
        return hmap;
    }

}
