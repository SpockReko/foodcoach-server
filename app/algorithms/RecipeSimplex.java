package algorithms;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.*;
import models.recipe.Ingredient;
<<<<<<< HEAD
import models.food.fineli.Nutrient;

=======
>>>>>>> master
import java.util.*;

/**
 * Created by emmafahlen on 2017-03-24.
 */
public class RecipeSimplex {
    private SimplexSolver solver = new SimplexSolver();
    private LinearObjectiveFunction f; // objective function, to be minimized
    private Collection<LinearConstraint> constraintsCollection = new ArrayList();
    private LinearConstraintSet constraints;

    /*
    Sets limits of least amount of each ingredient
     */
    public void setConstraintsIngredients(List<Double> leastAmountOfIngredient) {
        for( int i=0; i<leastAmountOfIngredient.size(); i++ ) {
            double[] arr = new double[leastAmountOfIngredient.size()];
            arr[i] = 1;
            constraintsCollection.add(new LinearConstraint(arr, Relationship.GEQ, leastAmountOfIngredient.get(i)));
        }
        constraints = new LinearConstraintSet(constraintsCollection);
    }

    public void setConstraintsNutrition(List<Ingredient> ingredients, HashMap<Nutrient,Double> nutritionNeed) {
        List<Double> kcalPer100grams = Arrays.asList(new Double[ingredients.size()]);
        List<Double> proteinPer100grams = Arrays.asList(new Double[ingredients.size()]);
        List<Double> fatPer100grams = Arrays.asList(new Double[ingredients.size()]);
        List<Double> carbohydratesPer100grams = Arrays.asList(new Double[ingredients.size()]);
        /*List<Double> vitaminAPer100grams = Arrays.asList(new Double[ingredients.size()]);
        List<Double> vitaminDPer100grams = Arrays.asList(new Double[ingredients.size()]);
        List<Double> vitaminCPer100grams = Arrays.asList(new Double[ingredients.size()]);*/

        for( int i=0; i<ingredients.size(); i++) {
            if(kcalPer100grams.get(i)==null){
                kcalPer100grams.set(i,0D);
                proteinPer100grams.set(i,0D);
                fatPer100grams.set(i,0D);
                carbohydratesPer100grams.set(i,0D);
                /*vitaminAPer100grams.set(i,0D);
                vitaminDPer100grams.set(i,0D);
                vitaminCPer100grams.set(i,0D);*/
            }
            kcalPer100grams.set(i, ingredients.get(i).getFoodItem().getEnergyKcal().doubleValue());
            proteinPer100grams.set(i, ingredients.get(i).getFoodItem().getProtein().doubleValue());
            fatPer100grams.set(i, ingredients.get(i).getFoodItem().getFats().getFat().doubleValue());
            carbohydratesPer100grams.set(i, ingredients.get(i).getFoodItem().getCarbohydrates().doubleValue());
            /*vitaminAPer100grams.set(i, ingredients.get(i).getFoodItem().getVitamins().getVitaminA().doubleValue());
            vitaminDPer100grams.set(i, ingredients.get(i).getFoodItem().getVitamins().getVitaminD().doubleValue());
            vitaminCPer100grams.set(i, ingredients.get(i).getFoodItem().getVitamins().getVitaminC().doubleValue());*/
        }

        setEachConstraintNutrition(kcalPer100grams, nutritionNeed.get(Nutrient.CaloriKcal), ingredients.size());
        setEachConstraintNutrition(proteinPer100grams, nutritionNeed.get(Nutrient.Protein), ingredients.size());
        setEachConstraintNutrition(fatPer100grams, nutritionNeed.get(Nutrient.Fat), ingredients.size());
        setEachConstraintNutrition(carbohydratesPer100grams, nutritionNeed.get(Nutrient.Carbohydrates), ingredients.size());
        /*setEachConstraintNutrition(vitaminAPer100grams, nutritionNeed.get(Nutrient.VitaminAUG), ingredients.size());
        setEachConstraintNutrition(vitaminDPer100grams, nutritionNeed.get(Nutrient.VitaminDUG), ingredients.size());
        setEachConstraintNutrition(vitaminCPer100grams, nutritionNeed.get(Nutrient.VitaminCMG), ingredients.size());
            /*setEachConstraintNutrition(ingredients.get(i).getFoodItem().getEnergyKcal().doubleValue(), nutritionNeed.get(Nutrient.CaloriKcal), ingredients.size());
            setEachConstraintNutrition(ingredients.get(i).getFoodItem().getCarbohydrates().doubleValue(), nutritionNeed.get(Nutrient.Carbohydrates), ingredients.size());
            setEachConstraintNutrition(ingredients.get(i).getFoodItem().getFats().getFat().doubleValue(), nutritionNeed.get(Nutrient.Fat), ingredients.size());
            setEachConstraintNutrition(ingredients.get(i).getFoodItem().getProtein().doubleValue(), nutritionNeed.get(Nutrient.Protein), ingredients.size());
            setEachConstraintNutrition(ingredients.get(i).getFoodItem().getFibre().doubleValue(), nutritionNeed.get(Nutrient.Fibre), ingredients.size());
            setEachConstraintNutrition(ingredients.get(i).getFoodItem().getVitamins().getVitaminA().doubleValue(), nutritionNeed.get(Nutrient.VitaminAUG), ingredients.size());
            setEachConstraintNutrition(ingredients.get(i).getFoodItem().getVitamins().getVitaminD().doubleValue(), nutritionNeed.get(Nutrient.VitaminDUG), ingredients.size());
            setEachConstraintNutrition(ingredients.get(i).getFoodItem().getVitamins().getVitaminE().doubleValue(), nutritionNeed.get(Nutrient.VitaminEMG), ingredients.size());
            setEachConstraintNutrition(ingredients.get(i).getFoodItem().getVitamins().getThiamine().doubleValue(), nutritionNeed.get(Nutrient.ThiamineMG), ingredients.size());
            setEachConstraintNutrition(ingredients.get(i).getFoodItem().getVitamins().getRiboflavin().doubleValue(), nutritionNeed.get(Nutrient.RiboflavinMG), ingredients.size());
            setEachConstraintNutrition(ingredients.get(i).getFoodItem().getVitamins().getNiacin().doubleValue(), nutritionNeed.get(Nutrient.NiacinMG), ingredients.size());
            setEachConstraintNutrition(ingredients.get(i).getFoodItem().getVitamins().getVitaminB6().doubleValue(), nutritionNeed.get(Nutrient.VitaminB6MG), ingredients.size());
            setEachConstraintNutrition(ingredients.get(i).getFoodItem().getMinerals().getFolate().doubleValue(), nutritionNeed.get(Nutrient.FolateUG), ingredients.size());
            setEachConstraintNutrition(ingredients.get(i).getFoodItem().getVitamins().getVitaminB12().doubleValue(), nutritionNeed.get(Nutrient.VitaminB12UG), ingredients.size());
            setEachConstraintNutrition(ingredients.get(i).getFoodItem().getVitamins().getVitaminC().doubleValue(), nutritionNeed.get(Nutrient.VitaminCMG), ingredients.size());
            setEachConstraintNutrition(ingredients.get(i).getFoodItem().getMinerals().getCalcium().doubleValue(), nutritionNeed.get(Nutrient.CalciumMG), ingredients.size());
            setEachConstraintNutrition(ingredients.get(i).getFoodItem().getMinerals().getPhosphorus().doubleValue(), nutritionNeed.get(Nutrient.PhosphorusMG), ingredients.size());
            setEachConstraintNutrition(ingredients.get(i).getFoodItem().getMinerals().getPotassium().doubleValue(), nutritionNeed.get(Nutrient.PotassiumMG), ingredients.size());
            setEachConstraintNutrition(ingredients.get(i).getFoodItem().getMinerals().getMagnesium().doubleValue(), nutritionNeed.get(Nutrient.MagnesiumMG), ingredients.size());
            setEachConstraintNutrition(ingredients.get(i).getFoodItem().getMinerals().getIron().doubleValue(), nutritionNeed.get(Nutrient.IronMG), ingredients.size());
            setEachConstraintNutrition(ingredients.get(i).getFoodItem().getMinerals().getZink().doubleValue(), nutritionNeed.get(Nutrient.ZinkMG), ingredients.size());
            setEachConstraintNutrition(ingredients.get(i).getFoodItem().getMinerals().getIodine().doubleValue(), nutritionNeed.get(Nutrient.IodineUG), ingredients.size());
            setEachConstraintNutrition(ingredients.get(i).getFoodItem().getMinerals().getSelenium().doubleValue(), nutritionNeed.get(Nutrient.SeleniumUG), ingredients.size());
            */

        constraints = new LinearConstraintSet(constraintsCollection);
    }

    private void setEachConstraintNutrition(List<Double> per100grams, Double nutritionNeed, int nrOfIngredients) {
        double[] arr = new double[nrOfIngredients];
        for (int i = 0; i < nrOfIngredients; i++) {
            arr[i] = per100grams.get(i) / 100D;
        }
        for(int i =0;i<arr.length;i++)
        {
            System.out.println(arr[i]);
        }
        System.out.println();

        if( !isZero(arr) ) {
            constraintsCollection.add(new LinearConstraint(arr, Relationship.GEQ, nutritionNeed));
        }
    }

    private boolean isZero(double[] arr) {
        for(int i=0; i<arr.length; i++) {
            if(arr[i]!=0) {
                return false;
            }
        }
        return true;
    }

    /*
    Sets linear objective function to minimize total amount of waste
     */
    public void setLinearObjectiveFunction(List<Ingredient> ingredients) {
        double[] objFcn = new double[ingredients.size()];

        for (int i = 0; i < ingredients.size(); i++) {
            if (ingredients.get(i).getFoodItem().getWaste() != null) {
                objFcn[i] = ingredients.get(i).getWaste() / 100;
            } else {
                objFcn[i] = 0;
            }
        }

        f = new LinearObjectiveFunction(objFcn,0);
    }

    public double[] optimize() {
        PointValuePair result = solver.optimize(f,constraints);
        return result.getPoint();
    }

}
