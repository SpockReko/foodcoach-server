package http;

import models.recipe.Recipe;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by fredrikkindstrom on 2017-03-20.
 */
public class ReceptFavoriterParser implements IRecipeParser {

    @Override
    public Recipe parse(String html) {
        Document doc = Jsoup.parse(html);

        String title = doc.select("h1[itemprop=name]").text();

        Elements ingredients = doc.select(".recipe-ingredients li");

        for (Element ingredient : ingredients) {
            System.out.println(ingredient.text());
        }
        return new Recipe(title, 4, null);
    }
}
