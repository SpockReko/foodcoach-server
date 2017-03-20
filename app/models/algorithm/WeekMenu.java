package models.algorithm;

import models.food.FoodItem;
import models.recipe.Recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by stefa on 2017-02-28.
 */
public class WeekMenu {

    private Double optimalMenuNutions = 0.0;
    private List optimalMenu = new ArrayList<Recipe>();

    private Double desiredValue = 0.0;

    private int nrOfRecept;
    private List<Recipe> allRecepie = new ArrayList<>();
    public Double calculateWeekMenu(int indexOfRecepieList, List<Recipe> choicenRecepies){

        Double firstValue = 1.0;
        Double secondValue = 1.0;
        if(optimalMenuNutions < desiredValue ){
            List<Recipe> newChoicenRecepies = choicenRecepies;

            if(indexOfRecepieList > 0){
                if(newChoicenRecepies.size() < nrOfRecept){
                    newChoicenRecepies.add(allRecepie.get(indexOfRecepieList));
                    firstValue = calculateWeekMenu(indexOfRecepieList-1, newChoicenRecepies);
                    secondValue = calculateWeekMenu(indexOfRecepieList-1, choicenRecepies);
                }
            }
            Double menuNutions = 0.0;
            if(Math.max(firstValue, secondValue ) == firstValue){
                menuNutions = nutionValueCalculation(newChoicenRecepies);
                choicenRecepies = newChoicenRecepies;
            }else{
                menuNutions =  nutionValueCalculation(choicenRecepies);
            }
            if(optimalMenuNutions <= menuNutions){
                optimalMenuNutions = menuNutions;
                optimalMenu = choicenRecepies;
            }
        }
        return optimalMenuNutions;
    }

    public double nutionValueCalculation(List<Recipe> choicenRecepies){
        Random r = new Random();
        return (0.01*r.nextInt(100));
    }


    public int getNrOfRecept() {
        return nrOfRecept;
    }


    public List<Recipe> getAllRecepie() {
        return allRecepie;
    }

    public List getOptimalMenu() {
        return optimalMenu;
    }

    // Exemple of how to get values..
    private void exemple() {
        // N är värdet av av aska i första råvaran!
        FoodItem food = FoodItem.find.where().eq("lmv_food_number", 1).findUnique();
        float n = food.getAsh();
    }


    public void setDesiredValue(Double desiredValue) {
        this.desiredValue = desiredValue;
    }

    public void setNrOfRecept(int nrOfRecept) {
        this.nrOfRecept = nrOfRecept;
    }

    public void setAllRecepie(List<Recipe> allRecepie) {
        this.allRecepie = allRecepie;
    }

}
