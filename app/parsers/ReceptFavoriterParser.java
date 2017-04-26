package parsers;

import models.food.FoodGroup;
import models.recipe.Ingredient;
import models.recipe.Recipe;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import play.Logger;
import play.libs.ws.WSClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fredrikkindstrom on 2017-03-20.
 */
public class ReceptFavoriterParser implements RecipeParser {

    private IngredientStringParser stringParser;

    public ReceptFavoriterParser(WSClient wsClient) {
        List<FoodGroup> foodGroupList = FoodGroup.find.select("searchTags").findList();
        stringParser = new IngredientStringParser(wsClient, foodGroupList);
    }

    @Override
    public Recipe parseUrl(String url) throws IOException {
        String html = Jsoup.connect(url).get().body().html();
        Recipe recipe = parseHtml(html);
        recipe.sourceUrl = url;
        return recipe;
    }

    @Override
    public Recipe parseHtml(String html) throws IOException {
        Document doc = Jsoup.parse(html);

        String title = doc.select("h1[itemprop=name]").text();
        String portionsText = doc.select("h3[itemprop=recipeYield]").text();
        int portions = Integer.parseInt(portionsText.replaceAll("\\D+",""));

        Elements ingredientStrings = doc.select(".recipe-ingredients li");
        List<Ingredient> ingredients = new ArrayList<>();

        for (Element ingredientString : ingredientStrings) {
            Ingredient ingredient;
            String webString = ingredientString.text().toLowerCase().trim();
            ingredient = stringParser.parse(webString);

            if (ingredient != null) {
                ingredients.add(ingredient);
            } else {
                Logger.error("Couldn't parseHtml '" + webString + "' moving on...");
            }
        }

        return new Recipe(title, portions, ingredients);
    }
}
