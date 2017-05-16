package http;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import models.recipe.Recipe;
import parsers.RecipeParser;
import parsers.ReceptFavoriterParser;
import play.Logger;
import play.libs.ws.WSClient;

import java.io.IOException;

/**
 * Used along with jCrawler when parsing a number of url's.
 */
public class RecipeCrawler extends WebCrawler {

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

            if (url.contains("receptfavoriter.se")) {
                WSClient wsClient = (WSClient) getMyController().getCustomData();
                parser = new ReceptFavoriterParser(wsClient);
            } else {
                throw new IllegalStateException("No parser for site: " + url);
            }

            parseRecipe(url, html, parser);
        }
    }

    private void parseRecipe(String url, String html, RecipeParser parser) {
        Recipe parsedRecipe = null;
        try {
            parsedRecipe = parser.parseHtml(html);
        } catch (IOException e) {
            e.printStackTrace();
        }
        parsedRecipe.sourceUrl = url;
        if (Recipe.find.where().eq("title", parsedRecipe.getTitle()).findCount() == 0) {
            parsedRecipe.save();
            Logger.info("Saved Recipe " + parsedRecipe.getTitle());
        } else {
            Logger.info("Recipe " + parsedRecipe.getTitle() + " already exists...");
        }
    }
}
