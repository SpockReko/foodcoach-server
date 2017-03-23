package tasks;

import com.avaje.ebean.EbeanServer;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import http.RecipeCrawler;

/**
 * Created by fredrikkindstrom on 2017-03-20.
 */
public class RecipePageParser {

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
         * These pages will get crawled.
         */
        controller.addSeed("http://receptfavoriter.se/recept/carbonara-med-creme-fraiche.html");

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
