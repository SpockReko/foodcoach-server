package tasks;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import play.libs.Json;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fredrikkindstrom on 2017-03-20.
 */
public class FineliParser {

    public static void main(String[] args) throws Exception {

        PrintWriter out = new PrintWriter("resources/out.txt");

        try (BufferedReader br = new BufferedReader(new FileReader("resources/ids.txt"))) {
            String line;
            int index = 0;
            while ((line = br.readLine()) != null) {
                System.out.println(index);
                Document doc = Jsoup.connect("https://fineli.fi/fineli/sv/elintarvikkeet/" + line).get();
                StringBuilder outLine = new StringBuilder();

                outLine.append(line).append("\t");
                outLine.append(doc.select(".scientific-name").text()).append("\t");

                Elements infos = doc.select(".food-info > li");
                for (Element info : infos) {
                    if (info.select(".property").text().equals("Tillredningssätt:")) {
                        outLine.append(info.select(".value").text()).append("\t");
                    }
                    if (info.select(".property").text().equals("Råvaror (kategorisering):")) {
                        outLine.append(info.select(".value").text()).append("\t");
                    }
                    if (info.select(".property").text().equals("Ätlig del:")) {
                        outLine.append(info.select(".value").text()).append("\t");
                    }
                }

                Elements classes = doc.select(".ingredient-list > li");
                StringBuilder c = new StringBuilder();
                for (Element elem : classes) {
                    String link = elem.select("[href^=/fineli/sv/erityisruokavaliot#]").attr("href");
                    if (link.contains("#")) {
                        link = link.substring(link.indexOf("#")+1, link.length());
                        c.append(link).append(", ");
                    }
                }
                if (classes.size() > 2) {
                    c = new StringBuilder(c.substring(0, c.length() - 2));
                }
                outLine.append(c);
                out.println(outLine);
                index++;
            }
        }

        out.close();
    }
}
