package algorithms;

import models.recipe.Recipe;
import org.apache.commons.math3.optim.OptimizationData;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.*;
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
    LinearConstraintSet constraints = new LinearConstraintSet();
    //Collection<LinearConstraint> constraints = new ArrayList();

    public void addConstraint(List<Double> leastAmountOfIngredient) {
        for( int i=0; i<leastAmountOfIngredient.size(); i++ ) {
            double[] arr = new double[leastAmountOfIngredient.size()];
            arr[i] = 1;
            Collection<LinearConstraint> cs = constraints.getConstraints();
            cs.add(new LinearConstraint(arr, Relationship.GEQ, leastAmountOfIngredient.get(i)));
            constraints = new LinearConstraintSet(cs);
            //constraints.add(new LinearConstraint(arr, Relationship.GEQ, leastAmountOfIngredient.get(i)));
        }
    }

    public void addLinearObjectiveFunction(List<Ingredient> ingredients) {
        double[] objFcn = new double[ingredients.size()];

        for( int i=0; i<ingredients.size(); i++ ) {
            objFcn[i] = ingredients.get(i).getWaste()/100;
        }

        f = new LinearObjectiveFunction(objFcn,0);
    }

    public double[] optimize() {
        //List<OptimizationData> optData = new ArrayList<>();
        //optData.add();
        PointValuePair result = solver.optimize(f,constraints);
        double[] optimalPoint = result.getPoint();
        return optimalPoint;
    }

}
