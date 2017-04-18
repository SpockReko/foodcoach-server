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

    public String recipeListToString(){
        String text = "";
        for(Recipe r : this.getRecipeList()){
            text = text + r.getTitle() + "\n";
        }
        text = text + "\n\nNäringsvärden i procent av användarens behov! (1 betyder näringsvärdet är uppfyllt)\n\n";
        for(String comment : this.getCommentList()){
            text = text + comment + "\n";
        }
        text = text + "\n\nInköpslista till menyerna:\n\n";
        ShoppingList shop = new ShoppingList(this);
        text = text + shop.toString();

        text = text + "\n\nNågot värde, troligtvis totalta koldioxidutsläppet för din meny! Just nu totalt matsvinn\n\n";

        text = text + shop.getTotalWaste();

        return text;
    }

}
