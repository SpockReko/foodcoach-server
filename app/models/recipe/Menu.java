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
        return new ArrayList<Recipe>(recipeList); //Return a copy so the class is unmodifiable
    }

    public List<String> getCommentList(){
        return new ArrayList<String>(commentList);
    }

    public void addComment(String comment){
        commentList.add(comment);
    }

    public String recipeListToString(Menu menu){
        String text = "Menu:\n";
        for(Recipe r : menu.getRecipeList()){
            text = text + r.getTitle() + "\n";
        }
        text = text + "\n\n";
        for(String comment : menu.getCommentList()){
            text = text + comment + "\n";
        }
        return text;
    }

}
