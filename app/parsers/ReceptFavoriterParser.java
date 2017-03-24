package parsers;

import models.recipe.Ingredient;
import models.recipe.NotLinkedRecipe;
import models.recipe.Recipe;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fredrikkindstrom on 2017-03-20.
 */
public class ReceptFavoriterParser implements RecipeParser {

    @Override
    public Recipe parse(String html) {
        IngredientParser ingredientParser = new IngredientParser();
        Document doc = Jsoup.parse(html);

        String title = doc.select("h1[itemprop=name]").text();
        String portionsText = doc.select("h3[itemprop=recipeYield]").text();
        int portions = Integer.parseInt(portionsText.replaceAll("\\D+",""));

        Elements ingredientStrings = doc.select(".recipe-ingredients li");
        List<Ingredient> ingredients = new ArrayList<>();

        for (Element ingredientString : ingredientStrings) {
            Ingredient ingredient;
            System.out.println(ingredientString.text());
            ingredient = ingredientParser.parse(ingredientString.text());
            if (ingredient != null) {
                ingredients.add(ingredient);
            }
        }

        return new Recipe(title, portions, ingredients);
    }

    @Override
    public NotLinkedRecipe parseWithoutLinking(String html) {
        Document doc = Jsoup.parse(html);

        String title = doc.select("h1[itemprop=name]").text();
        String portionsText = doc.select("h3[itemprop=recipeYield]").text();
        int portions = Integer.parseInt(portionsText.replaceAll("\\D+",""));

        Elements ingredients = doc.select(".recipe-ingredients li");

        List<String> textIngredients = new ArrayList<>();
        ingredients.forEach(elem -> textIngredients.add(elem.text()));

        return new NotLinkedRecipe(title, portions, textIngredients);
    }
}
