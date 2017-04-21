package parsers;

import models.recipe.Ingredient;
import models.recipe.NotLinkedRecipe;
import models.recipe.Recipe;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import play.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fredrikkindstrom on 2017-03-20.
 */
public class ReceptFavoriterParser implements RecipeParser {

    @Override
    public Recipe parse(String html) {
        IngredientStringParser stringParser = new IngredientStringParser();
        Document doc = Jsoup.parse(html);

        String title = doc.select("h1[itemprop=name]").text();
        String portionsText = doc.select("h3[itemprop=recipeYield]").text();
        int portions = Integer.parseInt(portionsText.replaceAll("\\D+",""));

        Elements ingredientStrings = doc.select(".recipe-ingredients li");
        List<Ingredient> ingredients = new ArrayList<>();

        for (Element ingredientString : ingredientStrings) {
            List<Ingredient> newIngredients;
            String webString = ingredientString.text().toLowerCase().trim();
            newIngredients = stringParser.parse(webString);

            for (Ingredient newIngredient : newIngredients) {
                if (newIngredient != null) {
                    ingredients.add(newIngredient);
                } else {
                    Logger.error("Couldn't parse '" + webString + "' moving on...");
                }
            }
        }

        return new Recipe(title, portions, ingredients);
    }

    @Override
    public NotLinkedRecipe parseWithoutLinking(String html) {
        Document doc = Jsoup.parse(html);

        String title = doc.select("h1[itemprop=name]").text();
        String portionsText = doc.select("h3[itemprop=recipeYield]").text().split(" ")[0];
        int portions = Integer.parseInt(portionsText.replaceAll("\\D+",""));

        Elements ingredients = doc.select(".recipe-ingredients li");

        List<String> textIngredients = new ArrayList<>();
        ingredients.forEach(elem -> textIngredients.add(elem.text()));

        return new NotLinkedRecipe(title, portions, textIngredients);
    }
}
