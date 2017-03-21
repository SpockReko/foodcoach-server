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
            sum += L2NormTerm(nutrientsContent.get(nutrient)/nutrientsNeed.get(nutrient));
        }
        return sum;
    }

    public HashMap<String,Double> nutrientsContent(Recipe recipe) {
        HashMap<String, Double> nutrientsContent = new HashMap<String, Double>();

        nutrientsContent.put("vitaminA", recipe.getVitaminA());
        nutrientsContent.put("vitaminB6", recipe.getVitaminB6());
        nutrientsContent.put("vitaminB12", recipe.getVitaminB12());
        nutrientsContent.put("vitaminC", recipe.getVitaminC());
        nutrientsContent.put("vitaminD", recipe.getVitaminD());
        nutrientsContent.put("thiamine", recipe.getThiamine());
        nutrientsContent.put("riboflavin", recipe.getRiboflavin());
        nutrientsContent.put("niacin", recipe.getNiacin());
        nutrientsContent.put("folate", recipe.getFolate());
        nutrientsContent.put("calcium", recipe.getCalcium());
        nutrientsContent.put("phosphorus", recipe.getPhosphorus());
        nutrientsContent.put("potassium", recipe.getPotassium());
        nutrientsContent.put("magnesium", recipe.getMagnesium());
        nutrientsContent.put("iron", recipe.getIron());
        nutrientsContent.put("zink", recipe.getZink());
        nutrientsContent.put("iodine", recipe.getIodine());
        nutrientsContent.put("selenium", recipe.getSelenium());
        return nutrientsContent;
    }

}
