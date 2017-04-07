package algorithms;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimplexOptimizer;
import org.apache.commons.math3.optimization.GoalType;
import models.recipe.Ingredient;

import javax.swing.*;
import java.util.*;

/**
 * Created by emmafahlen on 2017-03-24.
 */
public class RecipeSimplex {
    SimplexSolver solver = new SimplexSolver();
    LinearObjectiveFunction f;
    LinearConstraintSet constraints;

    public void addConstraint(List<Double> leastAmountOfIngredient) {
        for( int i=0; i<leastAmountOfIngredient.size(); i++ ) {
            double[] arr = new double[leastAmountOfIngredient.size()];
            arr[i] = 1;
            Collection<LinearConstraint> cs = constraints.getConstraints();
            cs.add(new LinearConstraint(arr, Relationship.GEQ, leastAmountOfIngredient.get(i)));
            constraints = new LinearConstraintSet(cs);
        }
    }

    public void addLinearObjectiveFunction(int numberOfIngredients, List<Ingredient> ingredients) {
        double[] objFcn = new double[numberOfIngredients];

        for( int i=0; i<ingredients.size(); i++ ) {
            objFcn[i] = ingredients.get(i).getWaste()/100;
        }

        f = new LinearObjectiveFunction(objFcn,0);
    }

    public double[] optimize(){
        PointValuePair result = solver.optimize(f,constraints);
        return result.getPoint();
    }
}
