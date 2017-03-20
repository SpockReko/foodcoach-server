package algorithms;
import java.util.*;

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

}
