package parsers;

import models.recipe.Recipe;

import java.io.IOException;

/**
 * Created by fredrikkindstrom on 2017-03-20.
 */
public interface RecipeParser {

    Recipe parseUrl(String url) throws IOException;
    Recipe parseHtml(String html) throws IOException;
}
