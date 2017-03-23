package http;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import models.recipe.NotLinkedRecipe;
import models.recipe.Recipe;
import parsers.RecipeParser;
import parsers.ReceptFavoriterParser;

/**
 * Created by fredrikkindstrom on 2017-03-20.
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

            if (url.startsWith("http://receptfavoriter")) {
                parser = new ReceptFavoriterParser();
            } else {
                throw new IllegalStateException("No parser for site: " + url);
            }

            NotLinkedRecipe parsedRecipe = parser.parseWithoutLinking(html);

            System.out.println(parsedRecipe.getTitle());
        }
    }
}
