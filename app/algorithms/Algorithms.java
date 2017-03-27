package algorithms;
import java.util.*;
import models.recipe.*;
import models.user.RDI;

/**
 * Created by louiserost on 2017-03-06.
 */
public class Algorithms {

    public static Double L2NormTerm(Double procentNutrient) {
        return Math.pow(1 - procentNutrient, 2);
    }

    public static Double L2Norm(HashMap<RDI,Double> nutrientsNeedPerDay, HashMap<RDI,Double> nutrientsContent, Menu recipes) {
        HashMap<RDI,Double> nutrientsNeed = nutrientsNeedScaled(nutrientsNeedPerDay,recipes.getRecipeList().size());
        Double sum = 0D;
        RDI nutrient;
        for( Map.Entry<RDI,Double> entry : nutrientsNeed.entrySet() ) {
            nutrient = entry.getKey();
            if(nutrientsNeed.containsKey(nutrient) && nutrientsContent.containsKey(nutrient)) {
                double l2NormResult = L2NormTerm(nutrientsContent.get(nutrient) / nutrientsNeed.get(nutrient));
                sum += l2NormResult > Math.pow(10,-8) ? l2NormResult : 0.0; // 10^-8 = (0.0000 0001)
            }
        }
        return Math.sqrt(sum);
    }

    /*
    Returns total amount of nutrients for several recipes
     */
    public static HashMap<RDI,Double> nutrientsContent(Menu recipes) {
        HashMap<RDI, Double> nutrientsContent = new HashMap<RDI, Double>();

        for( Recipe recipe : recipes.getRecipeList() ) {
            addToHashMap(nutrientsContent,RDI.CaloriKcal,recipe.getEnergyKcal()/recipe.getPortions());
            addToHashMap(nutrientsContent,RDI.Fat,recipe.getFat()/recipe.getPortions());
            addToHashMap(nutrientsContent,RDI.Protein,recipe.getProtein()/recipe.getPortions());
            addToHashMap(nutrientsContent,RDI.Carbohydrates,recipe.getCarbohydrates()/recipe.getPortions());

            addToHashMap(nutrientsContent,RDI.VitaminAUG, recipe.getVitaminA()/recipe.getPortions());
            addToHashMap(nutrientsContent,RDI.VitaminB6MG, recipe.getVitaminB6()/recipe.getPortions());
            addToHashMap(nutrientsContent,RDI.VitaminB12UG, recipe.getVitaminB12()/recipe.getPortions());
            addToHashMap(nutrientsContent,RDI.VitaminCMG, recipe.getVitaminC()/recipe.getPortions());
            addToHashMap(nutrientsContent,RDI.VitaminDUG, recipe.getVitaminD()/recipe.getPortions());
            addToHashMap(nutrientsContent,RDI.VitaminEMG, recipe.getVitaminE()/recipe.getPortions());
            addToHashMap(nutrientsContent,RDI.ThiamineMG, recipe.getThiamine()/recipe.getPortions());
            addToHashMap(nutrientsContent,RDI.RiboflavinMG, recipe.getRiboflavin()/recipe.getPortions());
            addToHashMap(nutrientsContent,RDI.NiacinMG, recipe.getNiacin()/recipe.getPortions());
            addToHashMap(nutrientsContent,RDI.FolateUG, recipe.getFolate()/recipe.getPortions());
            addToHashMap(nutrientsContent,RDI.CalciumMG, recipe.getCalcium()/recipe.getPortions());
            addToHashMap(nutrientsContent,RDI.PhosphorusMG, recipe.getPhosphorus()/recipe.getPortions());
            addToHashMap(nutrientsContent,RDI.PotassiumMG, recipe.getPotassium()/recipe.getPortions());
            addToHashMap(nutrientsContent,RDI.Magnesium, recipe.getMagnesium()/recipe.getPortions());
            addToHashMap(nutrientsContent,RDI.IronMG, recipe.getIron()/recipe.getPortions());
            addToHashMap(nutrientsContent,RDI.ZinkMG, recipe.getZink()/recipe.getPortions());
            addToHashMap(nutrientsContent,RDI.IodineUG, recipe.getIodine()/recipe.getPortions());
            addToHashMap(nutrientsContent,RDI.SeleniumUG, recipe.getSelenium()/recipe.getPortions());
        }

        return nutrientsContent;
    }


    /*
    Adds term to current value for nutrient in HashMap
     */
    public static HashMap<RDI,Double> addToHashMap(HashMap<RDI,Double> hmap, RDI nutrient, Double additionalTerm) {
        if( !hmap.containsKey(nutrient) ) {
            hmap.put(nutrient,0D);
        }
        hmap.put(nutrient,hmap.get(nutrient)+additionalTerm);
        return hmap;
    }


    /*
    Scales need of nutrients according to number of recipes
     */
    public static HashMap<RDI,Double> nutrientsNeedScaled (HashMap<RDI,Double> nutrientsNeedPerDay, int nrOfRecipes) {
        HashMap<RDI,Double> nutrientsNeedScaled = new HashMap<>();
        for( RDI nutrient : nutrientsNeedPerDay.keySet() ) {
            nutrientsNeedScaled.put(nutrient, 0.3*nrOfRecipes*nutrientsNeedPerDay.get(nutrient)); // assumes each meal is 30% of daily intake
        }
        return nutrientsNeedScaled;
    }

}
