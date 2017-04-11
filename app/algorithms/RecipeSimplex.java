package algorithms;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.*;
import models.recipe.Ingredient;
import models.food.fineli.Nutrient;

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

        for( int i=0; i<ingredients.size(); i++) {
            setEachConstraintNutrition(ingredients.get(i).getFood().getNutrient(models.food.fineli.Nutrient.KCAL), nutritionNeed.get(Nutrient.KCAL), ingredients.size());
            setEachConstraintNutrition(ingredients.get(i).getFood().getNutrient(models.food.fineli.Nutrient.CARBOHYDRATES), nutritionNeed.get(Nutrient.CARBOHYDRATES), ingredients.size());
            setEachConstraintNutrition(ingredients.get(i).getFood().getNutrient(models.food.fineli.Nutrient.FAT), nutritionNeed.get(Nutrient.FAT), ingredients.size());
            setEachConstraintNutrition(ingredients.get(i).getFood().getNutrient(models.food.fineli.Nutrient.PROTEIN), nutritionNeed.get(Nutrient.PROTEIN), ingredients.size());
            setEachConstraintNutrition(ingredients.get(i).getFood().getNutrient(Nutrient.FIBRE), nutritionNeed.get(Nutrient.FIBRE), ingredients.size());

            setEachConstraintNutrition(ingredients.get(i).getFood().getNutrient(Nutrient.VITAMIN_A), nutritionNeed.get(Nutrient.VITAMIN_A), ingredients.size());
            setEachConstraintNutrition(ingredients.get(i).getFood().getNutrient(Nutrient.VITAMIN_D), nutritionNeed.get(Nutrient.VITAMIN_D), ingredients.size());
            setEachConstraintNutrition(ingredients.get(i).getFood().getNutrient(Nutrient.VITAMIN_E), nutritionNeed.get(Nutrient.VITAMIN_E), ingredients.size());
            setEachConstraintNutrition(ingredients.get(i).getFood().getNutrient(Nutrient.THIAMINE), nutritionNeed.get(Nutrient.THIAMINE), ingredients.size());
            setEachConstraintNutrition(ingredients.get(i).getFood().getNutrient(Nutrient.RIBOFLAVIN), nutritionNeed.get(Nutrient.RIBOFLAVIN), ingredients.size());
            setEachConstraintNutrition(ingredients.get(i).getFood().getNutrient(Nutrient.NIACIN), nutritionNeed.get(Nutrient.NIACIN), ingredients.size());
            setEachConstraintNutrition(ingredients.get(i).getFood().getNutrient(Nutrient.VITAMIN_B6), nutritionNeed.get(Nutrient.VITAMIN_B6), ingredients.size());
            setEachConstraintNutrition(ingredients.get(i).getFood().getNutrient(Nutrient.FOLATE), nutritionNeed.get(Nutrient.FOLATE), ingredients.size());
            setEachConstraintNutrition(ingredients.get(i).getFood().getNutrient(Nutrient.VITAMIN_B12), nutritionNeed.get(Nutrient.VITAMIN_B12), ingredients.size());
            setEachConstraintNutrition(ingredients.get(i).getFood().getNutrient(Nutrient.VITAMIN_C), nutritionNeed.get(Nutrient.VITAMIN_C), ingredients.size());
            setEachConstraintNutrition(ingredients.get(i).getFood().getNutrient(Nutrient.CALCIUM), nutritionNeed.get(Nutrient.CALCIUM), ingredients.size());
            setEachConstraintNutrition(ingredients.get(i).getFood().getNutrient(Nutrient.PHOSPHORUS), nutritionNeed.get(Nutrient.PHOSPHORUS), ingredients.size());
            setEachConstraintNutrition(ingredients.get(i).getFood().getNutrient(Nutrient.POTASSIUM), nutritionNeed.get(Nutrient.POTASSIUM), ingredients.size());
            setEachConstraintNutrition(ingredients.get(i).getFood().getNutrient(Nutrient.MAGNESIUM), nutritionNeed.get(Nutrient.MAGNESIUM), ingredients.size());
            setEachConstraintNutrition(ingredients.get(i).getFood().getNutrient(Nutrient.IRON), nutritionNeed.get(Nutrient.IRON), ingredients.size());
            setEachConstraintNutrition(ingredients.get(i).getFood().getNutrient(Nutrient.ZINC), nutritionNeed.get(Nutrient.ZINC), ingredients.size());
            setEachConstraintNutrition(ingredients.get(i).getFood().getNutrient(Nutrient.IODINE), nutritionNeed.get(Nutrient.IODINE), ingredients.size());
            setEachConstraintNutrition(ingredients.get(i).getFood().getNutrient(Nutrient.SELENIUM), nutritionNeed.get(Nutrient.SELENIUM), ingredients.size());
        }

        constraints = new LinearConstraintSet(constraintsCollection);
    }

    private void setEachConstraintNutrition(Double per100grams, Double nutritionNeed, int nrOfIngredients) {
        double[] arr = new double[nrOfIngredients];
        for (int i = 0; i < nrOfIngredients; i++) {
            arr[i] = per100grams / 100;
        }
        constraintsCollection.add(new LinearConstraint(arr, Relationship.GEQ, nutritionNeed));
    }

    /*
    Sets linear objective function to minimize waste from each ingredient
     */
    public void setLinearObjectiveFunction(List<Ingredient> ingredients) {
        double[] objFcn = new double[ingredients.size()];

        for( int i=0; i<ingredients.size(); i++ ) {
            objFcn[i] = ingredients.get(i).getAlcohol()/100; // TODO Change getAlcohol to getWaste or getCO2
        }

        f = new LinearObjectiveFunction(objFcn,0);
    }

    public double[] optimize() {
        PointValuePair result = solver.optimize(f,constraints);
        return result.getPoint();
    }

}
