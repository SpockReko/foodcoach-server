package parsers;

import models.recipe.NotLinkedRecipe;
import models.recipe.Recipe;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by fredrikkindstrom on 2017-03-20.
 */
public class ReceptFavoriterParser implements RecipeParser {

    @Override
    public NotLinkedRecipe parseWithoutLinking(String html) {
        Document doc = Jsoup.parse(html);

        String title = doc.select("h1[itemprop=name]").text();
        String portionsText = doc.select("h3[itemprop=recipeYield]").text();
        int portions = Integer.parseInt(portionsText.replaceAll("\\D+",""));

        Elements ingredients = doc.select(".recipe-ingredients li");

        System.out.println(title);
        System.out.println(portions);
        for (Element ingredient : ingredients) {
            System.out.println(ingredient.text());
        }
        return null;
    }
}
