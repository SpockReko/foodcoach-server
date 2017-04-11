package algorithms;

import models.food.FoodItem;
import models.recipe.Ingredient;
import models.recipe.Menu;
import models.recipe.Recipe;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by macbookl on 11/04/17.
 */
public class MenuAlgorithm2 {

    private List<FoodItem> foodItemList;
    private List<Recipe> allRecipes;
    private Menu optimalMenu;
    private List<Ingredient> notThisIngredients = new ArrayList<>();
    private List<Menu> weekMenuList = new ArrayList<>();
    private int nbrOfRecipes=3;
    private int optimalValue;

    public MenuAlgorithm2(List<FoodItem> foodItemList, List<Recipe> recipeList){
        this.foodItemList=foodItemList;
        this.allRecipes=recipeList;
    }
    public void addAllergies(Ingredient ingredient) {
        notThisIngredients.add(ingredient);
    }
    private void reset(){
        optimalMenu = new Menu(new ArrayList<>());
        weekMenuList = new ArrayList<>();
        optimalValue=0;
    }

    public Menu calculateWeekMenu() {
        reset();
        //filterRecipes(notThisIngredients,notThisRecipes);
        returnAllWeekMenus(allRecipes.size()-1,new ArrayList<>());
        for(Menu menu : weekMenuList){
            int value = nbrOfFoodsUsed(menu);
            if(value > optimalValue){
                optimalValue = value;
                optimalMenu = menu;
            }
        }
       // System.out.println(MenuAlgorithms.recipeListToString(optimalMenu));
        return optimalMenu;
    }

    public int returnAllWeekMenus(int indexOfRecipes, List<Recipe> currentList){
        if (currentList.size() == nbrOfRecipes){
            weekMenuList.add(new Menu(currentList));
            return 1;
        }else if(indexOfRecipes < 0){
            return 0;
        }else{
            List<Recipe> newList = new ArrayList<>(currentList);
            currentList.add(allRecipes.get(indexOfRecipes));
            return returnAllWeekMenus(indexOfRecipes-1, currentList) +
                    returnAllWeekMenus(indexOfRecipes-1, newList);
        }

    }


    private int nbrOfFoodsUsed(Menu menu){
        List<Ingredient> ingredientsInMenu =new ArrayList<Ingredient>();
        List<FoodItem> foodItemsLeft=foodItemList;
        List<FoodItem> usedFood=new ArrayList<FoodItem>();
        for( Recipe recipe : menu.getRecipeList() ) {
            for(int i=0; i<recipe.ingredients.size(); i++){
                FoodItem food=recipe.ingredients.get(i).getFoodItem();
                if(foodItemsLeft.contains(food)){
                    foodItemsLeft.remove(food);
                    usedFood.add(food);
                }
            }
        }
        return usedFood.size();
    }

}
