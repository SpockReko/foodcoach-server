package algorithms;

import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.Relationship;
import org.apache.commons.math3.optim.linear.SimplexSolver;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimplexOptimizer;
import org.apache.commons.math3.optimization.GoalType;
import models.recipe.Ingredient;
import java.util.*;

/**
 * Created by emmafahlen on 2017-03-24.
 */
public class RecipeSimplex {
    SimplexSolver solver = new SimplexSolver();
    LinearObjectiveFunction f;
    Collection<LinearConstraint> constraints = new ArrayList();

    public void addConstraint(List<Double> leastAmountOfIngredient) {
        for( int i=0; i<leastAmountOfIngredient.size(); i++ ) {
            double[] arr = new double[leastAmountOfIngredient.size()];
            arr[i] = 1;
            constraints.add(new LinearConstraint(arr, Relationship.GEQ, leastAmountOfIngredient.get(i)));
        }
    }

    public void addLinearObjectiveFunction(int numberOfIngredients, List<Ingredient> ingredients) {
        double[] objFcn = new double[numberOfIngredients];

        for( int i=0; i<ingredients.size(); i++ ) {
            objFcn[i] = ingredients.get(i).getWaste()/100;
        }

        f = new LinearObjectiveFunction(objFcn,0);
    }

}
