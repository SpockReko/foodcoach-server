package http;

import models.recipe.Recipe;

/**
 * Created by fredrikkindstrom on 2017-03-20.
 */
public interface IRecipeParser {

    Recipe parse(String html);
}
