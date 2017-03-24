package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.food.FoodItem;
import parsers.IngredientParser;
import parsers.IngredientSplitter;
import parsers.WordGetters;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

/**
 * HTTP controller that handles all general requests to the server.
 */
public class ParseController extends Controller {

    @Inject
    WSClient ws;

    public Result parseIngredient(String str) {
        FoodItem item = IngredientParser.findMatch(str);
        if (item.example != null) {
            return ok("<font size=\"4\" color=\"blue\">"
                + "#" + item.getLmvFoodNumber() + " - "
                + item.screenName + " (exempelvis "
                + item.example + ")</font>")
                .as("text/html");
        } else if (item.screenName != null) {
            return ok("<font size=\"4\" color=\"green\">"
                + "#" + item.getLmvFoodNumber() + " - "
                + item.screenName + "</font>")
                .as("text/html");
        } else {
            return ok("<font size=\"4\" color=\"red\">"
                + item.getName() + "</font>")
                .as("text/html");
        }
    }


    public JsonNode parseRecipie (String recipe){
        JsonNode node = null;
        CompletionStage<WSResponse> request = ws.url("http://json-tagger.herokuapp.com/tag")
                .setContentType("application/x-www-form-urlencoded")
                .post(recipe);
        CompletionStage<JsonNode> jsonPromise = request.thenApply(WSResponse::asJson);
        try {
            JsonNode jsonNode = jsonPromise.toCompletableFuture().get();
            node = jsonNode;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return node;
    }

    public Result recipeInfo (String recipe){

        JsonNode node = parseRecipie(recipe);

        IngredientSplitter splitter = new IngredientSplitter(recipe, node);
        System.out.println("DIVIDED = " + splitter.divided());
        System.out.println("INGREDIENTS = " + splitter.extractIngeridents());
        System.out.println("ADJEKTIV = " + splitter.extractAdjektiv());
        System.out.println("NUMERIC = " + splitter.extractNumeric());
        System.out.println("UNITS = " + splitter.extractUnits());
        System.out.println("OTHER = " + splitter.extractOther());



        return ok(node);
    }
}
