package parsers;

import models.recipe.NotLinkedRecipe;

/**
 * Created by fredrikkindstrom on 2017-03-20.
 */
public interface RecipeParser {

    NotLinkedRecipe parseWithoutLinking(String html);
}
