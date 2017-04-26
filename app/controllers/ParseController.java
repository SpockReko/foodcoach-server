package controllers;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import helpers.JsonHelper;
import http.RecipeCrawler;
import models.recipe.Ingredient;
import parsers.IngredientStringParser;
import play.libs.ws.WSClient;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * HTTP controller that handles all group requests to the server.
 */
public class ParseController extends Controller {

    @Inject WSClient wsClient;
    private static final String RECIPES_URLS_PATH = "resources/recipe_urls/receptfavoriter_se.txt";
    private static final int RECIPES_TO_PARSE = 20;

    public Result parseLine(String input) {
        IngredientStringParser parser = new IngredientStringParser(wsClient);
        Ingredient ingredient = null;
        try {
            ingredient = parser.parse(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (ingredient != null) {
            return ok(JsonHelper.toJson(ingredient));
        } else {
            return ok("Did not find ingredient");
        }
    }

    public Result runParse() throws Exception {
        String crawlStorageFolder = "target/crawl-data";
        int numberOfCrawlers = 7;

        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(crawlStorageFolder);
        config.setMaxDepthOfCrawling(0);

        /*
         * Instantiate the controller for this crawl.
         */
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
        controller.setCustomData(wsClient);

        /*
         * Add pages from .txt file to be crawled.
         */
        try (BufferedReader br = new BufferedReader(new FileReader(RECIPES_URLS_PATH))) {
            String lineUrl;
            for (int i = 0; i < RECIPES_TO_PARSE; i++) {
                if ((lineUrl = br.readLine()) != null) {
                    controller.addSeed(lineUrl);
                } else {
                    break;
                }
            }
        }

        /*
         * Start the crawl. This is a blocking operation, meaning that your code
         * will reach the line after this only when crawling is finished.
         */
        controller.start(RecipeCrawler.class, numberOfCrawlers);

        // Send the shutdown request and then wait for finishing
        controller.shutdown();
        controller.waitUntilFinish();

        return ok("Done!");
    }
}
