package tasks;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import http.RecipeCrawler;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Created by fredrikkindstrom on 2017-03-20.
 */
public class RecipePageParser {

    private static final String RECIPES_URLS_PATH = "resources/recipe_urls/receptfavoriter_se.txt";
    private static final int RECIPES_TO_PARSE = 10;

    public static void main(String[] args) throws Exception {
        String crawlStorageFolder = "target/crawl-data";
        int numberOfCrawlers = 2;

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

        /*
         * Add pages from .txt file to be crawled.
         */
        controller.addSeed("http://receptfavoriter.se/recept/indisk-kycklinggryta-med-curry-och-graedde.html");
//        try (BufferedReader br = new BufferedReader(new FileReader(RECIPES_URLS_PATH))) {
//            String lineUrl;
//            for (int i = 0; i < RECIPES_TO_PARSE; i++) {
//                if ((lineUrl = br.readLine()) != null) {
//                    controller.addSeed(lineUrl);
//                } else {
//                    break;
//                }
//            }
//        }

        /*
         * Start the crawl. This is a blocking operation, meaning that your code
         * will reach the line after this only when crawling is finished.
         */
        controller.start(RecipeCrawler.class, numberOfCrawlers);

        // Send the shutdown request and then wait for finishing
        controller.shutdown();
        controller.waitUntilFinish();
    }
}
