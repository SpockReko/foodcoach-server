package tools;

import models.food.FoodItem;

import java.util.List;

/**
 * Created by emmafahlen on 2017-02-14.
 */
public class IngredientToFood {

    static List<FoodItem> list;
    static int inAuto = 0;

    public static FoodItem ingToFood (String ing){
        try {
            list = FoodItem.find.where().eq("name", ing).findList();
            if (list.isEmpty()) {
                //kollar särskrivningar
                if (ing.contains(" ")) {
                    String newString = eraseSpace(ing);
                    System.out.println(newString);
                    return ingToFood(newString);
                }
                //kolla om ing är felstavad
                else if (!autoCorrect(ing).equals(ing) && inAuto < 2) {
                    String auto = autoCorrect(ing);
                    return ingToFood(auto);

                } else {
                    list = FoodItem.find.where().contains("name", ing).findList();
                    if (!list.isEmpty()) {
                        //gör ett nytt foodItem med de genomsnittliga värdena
                        System.out.print("Det finns som en substring någonstans");
                        System.out.print(list.size());
                        return null;
                    } else {
                        System.out.print("Det finns inte med alls");
                        //det finns inte med i livsmedelsdatabasen, vi kan endast använda strängen och göra om den till ett FoodItem
                    }
                }
            }
            if (list.size() == 1) {
                return list.get(0);
            }

        }catch (Exception ex){
            System.out.println("gick inte så bra");
        }
        return null;
    }

    public static String autoCorrect(String ing) {
        // hämta alla strings som är +- 1 längre
        // gå igenom alla strings och titta
        String fst;
        String snd;
        String letter;
        String alphabet = "abcdefghijklmnopqrstuvxyz";
        inAuto++;
        for (int i = 0; i<ing.length()-1; i++) {
            fst = ing.substring(0, i);
            snd = ing.substring(i);
            for (int j = 0; j<alphabet.length(); j++){
                letter = alphabet.substring(j,j+1);
                ingToFood(fst+letter+snd);
            }
        }
        return ing;
    }

    public static String eraseSpace (String ing) {
        int index = ing.indexOf(" ");
        if (index == 0){
            return ing.substring(1);
        }
        if (index == ing.length()+1){
            return ing.substring(0, ing.length());
        }
        String fst = ing.substring(0,index);
        String snd = ing.substring(index+1);
        return fst+snd;
    }
}
