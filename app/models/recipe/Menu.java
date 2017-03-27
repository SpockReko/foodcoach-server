package models.recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stefa on 2017-03-27.
 */
public class Menu {

    private final List<Recipe> recipeList;
    private List<String> commentList = new ArrayList<>();

    public Menu(List<Recipe> recipes){
        this.recipeList = recipes;
    }

    public List<Recipe> getRecipeList(){
        return new ArrayList<>(recipeList);
    }
    public void addComment(String comment){
        commentList.add(comment);
    }

}
