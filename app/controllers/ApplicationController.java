package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import tools.CsvReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.List;

public class ApplicationController extends Controller {

    public Result index() {

        return ok("It works!");
    }

	public Result parse(String operation) {

		List<String> lines = null;
		String outputPath = "";

		switch (operation.toLowerCase()) {
			case "update_links":
				lines = CsvReader.updateLinksFromCsv();
				outputPath = "resources/db/scripts/X_fooditems_meta_seed.sql";
				break;
			default:
				badRequest("No parse operation called '" + operation + "' found on server!");
		}

		try {
			PrintStream printStream = new PrintStream(new File(outputPath));
			if (lines != null) lines.forEach(printStream::println);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return ok("Done! Parsed " + lines.size() + " records into " + outputPath);
	}

}
