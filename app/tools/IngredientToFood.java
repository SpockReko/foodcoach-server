package tools;

import models.food.FoodItem;

import java.util.ArrayList;
import java.util.List;
import info.debatty.java.stringsimilarity.*;

/**
 * Created by emmafahlen on 2017-02-14.
 */
public class IngredientToFood {


    public static FoodItem ingToFood (String ing){
        try {
            FoodItem food= FoodItem.find.where().eq("name", ing).findUnique();
            if (food == null) {
                //kolla om ing är felstavad
                String corrected = autoCorrect(ing);
                if (!corrected.equals(ing.toLowerCase())) {
                    System.out.println("HAR RÄTTAT RÄTT JAJJAMEN!");
                    return FoodItem.find.where().eq("name", corrected).findList().get(0);
                } else {
                    System.out.println("NYTT FOODITEM! hejejhekjgekl");
                    return new FoodItem(ing, FoodItem.find.findCount()*ing.hashCode());
                }
            }
            else {return food;}

        }catch (Exception ex){
            System.out.println("gick inte så bra");
        }
        return null;
    }

    public static String autoCorrect(String ing) {
        System.out.println("I AUTOCORRECT");
        int max = 3;
        Levenshtein l = new Levenshtein();
        List<FoodItem> allIng = FoodItem.find.all();
        FoodItem matchIng = allIng.get(0);
        double tempDist;
        double dist = 5;
        for (FoodItem food : allIng){
            String foo = food.getName().toLowerCase();
            tempDist = l.distance(foo , ing.toLowerCase());
            if (tempDist <= max){
                if (dist != 5) {
                    if ( tempDist < dist ) {
                        matchIng = food;
                        dist = tempDist;
                    }
                }
                else {
                        matchIng = food;
                        dist = tempDist;
                }
                //HÄR SKA VI SE OM VI FÅR FLER OCH HANTERA DET
                //else if (matchIng != null && dist == l.distance(matchIng.get(0).getName(), ing)){
                //    matchIng.add(food);
                //}
            }
        }
        if (matchIng != allIng.get(0)){
            return matchIng.getName();
        }
        return ing;
    }
}
