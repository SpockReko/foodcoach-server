package parsers;

import models.recipe.NotLinkedRecipe;
import models.recipe.Recipe;

/**
 * Created by fredrikkindstrom on 2017-03-20.
 */
public interface RecipeParser {

    Recipe parse(String html);
    NotLinkedRecipe parseWithoutLinking(String html);
}
