package http;

import com.avaje.ebean.EbeanServer;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import models.recipe.NotLinkedRecipe;
import models.recipe.Recipe;
import parsers.RecipeParser;
import parsers.ReceptFavoriterParser;
import play.Logger;
import tasks.CommonTools;

/**
 * Created by fredrikkindstrom on 2017-03-20.
 */
public class RecipeCrawler extends WebCrawler {

    private static EbeanServer db = CommonTools.getDatabase();

    /**
     * This function is called when a page is fetched and ready
     * to be processed by your program.
     */
    @Override
    public void visit(Page page) {
        String url = page.getWebURL().getURL();

        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String html = htmlParseData.getHtml();
            RecipeParser parser;

            if (url.startsWith("http://receptfavoriter")) {
                parser = new ReceptFavoriterParser();
            } else {
                throw new IllegalStateException("No parser for site: " + url);
            }

            parseRecipe(url, html, parser);
        }
    }

    private void parseRecipe(String url, String html, RecipeParser parser) {
        Recipe parsedRecipe = parser.parse(html);
        parsedRecipe.sourceUrl = url;
        if (db.find(Recipe.class)
                .where()
                .eq("title", parsedRecipe.getTitle())
                .findCount() == 0) {
            db.save(parsedRecipe);
            Logger.info("Saved Recipe " + parsedRecipe.getTitle());
        } else {
            Logger.info("Recipe " + parsedRecipe.getTitle() + " already exists...");
        }
    }

    private void parseNotLinkedRecipe(String url, String html, RecipeParser parser) {
        NotLinkedRecipe parsedRecipe = parser.parseWithoutLinking(html);
        parsedRecipe.sourceUrl = url;
        if (db.find(NotLinkedRecipe.class)
                .where()
                .eq("title", parsedRecipe.getTitle())
                .findCount() == 0) {
            db.save(parsedRecipe);
            Logger.info("Saved NotLinkedRecipe " + parsedRecipe.getTitle());
        } else {
            Logger.info("NotLinkedRecipe " + parsedRecipe.getTitle() + " already exists...");
        }
    }
}
