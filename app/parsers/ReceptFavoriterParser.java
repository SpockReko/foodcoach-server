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

    private List<String> afterEller = new ArrayList<>();
    private List<String> afterOch = new ArrayList<>();
    private String beforeColon = "";

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
            String webString = ingredientString.text().toLowerCase().trim();
            webString = handleEdgeCases(webString);
            ingredient = ingredientParser.parse(webString);
            if (ingredient != null) {
                ingredients.add(ingredient);
            } else {
                Logger.error("Couldn't parse '" + webString + "' moving on...");
            }

            if (!afterOch.isEmpty()){
                if (ingredient != null) {
                    ingredients.add(ingredient);
                } else {
                    Logger.error("Couldn't parse '" + webString + "' moving on...");
                }
            }
        }

        return new Recipe(title, portions, ingredients);
    }

    private String handleEdgeCases(String webString) {
        //Handle ":"
        if (webString.contains(":")){
            String[] split = webString.toLowerCase().split(":");
            webString = split[1];
            beforeColon = split[0];
            System.out.println("AFTER COLON: " + split[1]);
            System.out.println("BEFORE COLON: " + split[0]);
        }

        //Handle "eller"
        if (webString.toLowerCase().contains(" " + "eller" + " ") ||
                webString.toLowerCase().contains(" " + "eller" + ",") ||
                webString.toLowerCase().contains(" " + "eller" + ".")) {
            String[] split = webString.toLowerCase().split(" eller");
            webString = split[0];
            for (int i = 1; i<split.length; i++) {
                afterEller.add(split[i]);
                System.out.println("AFTER ELLER: " + split[i]);
            }
            System.out.println("BEFORE ELLER: " + split[0]);
        }

        //Handle "och"
        if (webString.toLowerCase().contains(" " + "och" + " ") ||
                webString.toLowerCase().contains(" " + "och" + ",") ||
                webString.toLowerCase().contains(" " + "och" + ".")) {
            String[] split = webString.toLowerCase().split(" och");
            webString = split[0];
            for (int i = 1; i<split.length; i++) {
                afterOch.add(split[i]);
                System.out.println("AFTER OCH: " + split[i]);
            }
            System.out.println("BEFORE OCH: " + split[0]);
        }

        System.out.println("WEBSTRING: " + webString);
        return webString;
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
