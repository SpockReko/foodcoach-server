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

    public static Double L2Norm(HashMap<RDI,Double> nutrientsNeed, HashMap<RDI,Double> nutrientsContent) {
        Double sum = 0D;
        RDI nutrient;
        for( Map.Entry<RDI,Double> entry : nutrientsNeed.entrySet() ) {
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
    public static HashMap<RDI,Double> nutrientsContent(List<Recipe> recipes) {
        HashMap<RDI, Double> nutrientsContent = new HashMap<RDI, Double>();

        for( Recipe recipe : recipes ) {
            addToHashMap(nutrientsContent,RDI.CaloriKcal,recipe.getEnergyKcal());
            addToHashMap(nutrientsContent,RDI.Fat,recipe.getFat());
            addToHashMap(nutrientsContent,RDI.Protein,recipe.getProtein());
            addToHashMap(nutrientsContent,RDI.Carbohydrates,recipe.getCarbohydrates());

            addToHashMap(nutrientsContent,RDI.VitaminAUG, recipe.getVitaminA());
            addToHashMap(nutrientsContent,RDI.VitaminB6MG, recipe.getVitaminB6());
            addToHashMap(nutrientsContent,RDI.VitaminB12UG, recipe.getVitaminB12());
            addToHashMap(nutrientsContent,RDI.VitaminCMG, recipe.getVitaminC());
            addToHashMap(nutrientsContent,RDI.VitaminDUG, recipe.getVitaminD());
            addToHashMap(nutrientsContent,RDI.VitaminEMG, recipe.getVitaminE());
            addToHashMap(nutrientsContent,RDI.ThiamineMG, recipe.getThiamine());
            addToHashMap(nutrientsContent,RDI.RiboflavinMG, recipe.getRiboflavin());
            addToHashMap(nutrientsContent,RDI.NiacinMG, recipe.getNiacin());
            addToHashMap(nutrientsContent,RDI.FolateUG, recipe.getFolate());
            addToHashMap(nutrientsContent,RDI.CalciumMG, recipe.getCalcium());
            addToHashMap(nutrientsContent,RDI.PhosphorusMG, recipe.getPhosphorus());
            addToHashMap(nutrientsContent,RDI.PotassiumG, recipe.getPotassium());
            addToHashMap(nutrientsContent,RDI.Magnesium, recipe.getMagnesium());
            addToHashMap(nutrientsContent,RDI.IronMG, recipe.getIron());
            addToHashMap(nutrientsContent,RDI.ZinkMG, recipe.getZink());
            addToHashMap(nutrientsContent,RDI.IodineUG, recipe.getIodine());
            addToHashMap(nutrientsContent,RDI.SeleniumUG, recipe.getSelenium());
        }

        return nutrientsContent;
    }


    public static HashMap<RDI,Double> addToHashMap(HashMap<RDI,Double> hmap, RDI nutrient, Double additionalTerm) {
        if( !hmap.containsKey(nutrient) ) {
            hmap.put(nutrient,0D);
        }
        hmap.put(nutrient,hmap.get(nutrient)+additionalTerm);
        return hmap;
    }

}
