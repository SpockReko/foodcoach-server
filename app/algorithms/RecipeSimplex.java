package algorithms;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.*;
import models.recipe.Ingredient;
import models.food.Nutrient;
import java.util.*;

/**
 * Created by emmafahlen on 2017-03-24.
 */
public class RecipeSimplex {
    private SimplexSolver solver = new SimplexSolver();
    private LinearObjectiveFunction f; // objective function, to be minimized
    private Collection<LinearConstraint> constraintsCollection = new ArrayList();
    private LinearConstraintSet constraints;
    private List<Ingredient> ingredients;
    private HashMap<Nutrient,Double> nutritionNeed;
    private List<Double> leastAmountOfIngredient;
    private boolean exceedsCalorie=false;

    /*
    Sets limits of least amount of each ingredient
     */
    public void setConstraintsIngredients(List<Double> leastAmountOfIngredient) {
        this.leastAmountOfIngredient=leastAmountOfIngredient;
        for( int i=0; i<leastAmountOfIngredient.size(); i++ ) {
            double[] arr = new double[leastAmountOfIngredient.size()];
            arr[i] = 1;
            constraintsCollection.add(new LinearConstraint(arr, Relationship.GEQ, leastAmountOfIngredient.get(i)));
        }
        constraints = new LinearConstraintSet(constraintsCollection);
    }

    public void setConstraintsNutrition(List<Ingredient> ingredients, HashMap<Nutrient,Double> nutritionNeed, boolean maxCalorie) {
        this.ingredients=ingredients;
        this.nutritionNeed=nutritionNeed;

        setConstraint(ingredients, Nutrient.KCAL, nutritionNeed.get(Nutrient.KCAL), true);
        setConstraint(ingredients, Nutrient.PROTEIN, nutritionNeed.get(Nutrient.PROTEIN), true);
        setConstraint(ingredients, Nutrient.FAT, nutritionNeed.get(Nutrient.FAT), true);
        setConstraint(ingredients, Nutrient.CARBOHYDRATES, nutritionNeed.get(Nutrient.CARBOHYDRATES), true);

        if (maxCalorie) {
            setConstraint(ingredients,Nutrient.KCAL, nutritionNeed.get(Nutrient.KCAL)*1.2, false);
        }

        constraints = new LinearConstraintSet(constraintsCollection);
    }

    private void setConstraint(List<Ingredient> ingredients, Nutrient nutrient, Double nutritionNeed, boolean lower){
        List<Double> per100grams = Arrays.asList(new Double[ingredients.size()]);
        for( int i=0; i<ingredients.size(); i++) {
            if(per100grams.get(i)==null) {
                per100grams.set(i, 0D);
            }
            per100grams.set(i, ingredients.get(i).getFood().getNutrient(nutrient));
        }
        setEachConstraint(per100grams, nutritionNeed, ingredients.size(), lower);
    }


    private void setEachConstraint(List<Double> per100grams, Double nutritionNeed, int nrOfIngredients, boolean lower) {
        double[] arr = new double[nrOfIngredients];
        for (int i = 0; i < nrOfIngredients; i++) {
            arr[i] = per100grams.get(i) / 100D;
        }

        if( !isZero(arr) ) {
            if(lower) constraintsCollection.add(new LinearConstraint(arr, Relationship.GEQ, nutritionNeed));
            else  constraintsCollection.add(new LinearConstraint(arr, Relationship.LEQ, nutritionNeed));
        }
    }


    private void setMaxConstraintNutrition(List<Double> per100grams, Double nutritionNeed, int nrOfIngredients) {
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
            constraintsCollection.add(new LinearConstraint(arr, Relationship.LEQ, nutritionNeed*1.2));
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

    public boolean exceedsCalorie(){
        return exceedsCalorie;
    }

    /*
    Sets linear objective function to minimize total amount of waste
     */
    public void setLinearObjectiveFunction(List<Ingredient> ingredients) {
        double[] objFcn = new double[ingredients.size()];

        for (int i = 0; i < ingredients.size(); i++) {
            objFcn[i] = ingredients.get(i).getFood().getCO2() / 100;
        }

        f = new LinearObjectiveFunction(objFcn,0);
    }

    public double[] optimize() {
        try {
            PointValuePair result = solver.optimize(f, constraints);
            return result.getPoint();
        }catch(NoFeasibleSolutionException e){
            exceedsCalorie=true;
            constraintsCollection = new ArrayList();
            setConstraintsIngredients(leastAmountOfIngredient);
            setConstraintsNutrition(ingredients, nutritionNeed, false);
            PointValuePair result = solver.optimize(f, constraints);
            return result.getPoint();
        }
    }

}
