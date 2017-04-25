package parsers;

import models.recipe.Recipe;

import java.io.IOException;

/**
 * Created by fredrikkindstrom on 2017-03-20.
 */
public interface RecipeParser {

    Recipe parse(String html) throws IOException;
}
