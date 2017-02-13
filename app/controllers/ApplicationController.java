package controllers;

import tools.CsvReader;
import play.mvc.Controller;
import play.mvc.Result;
import tools.TxtReader;

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
			case "basic":
				lines =  CsvReader.basicFoodToSql();
				outputPath = "resources/db/scripts/1_fooditems_seed.sql";
				break;
			case "extract_groups":
				lines = CsvReader.foodGroupsToTxt();
				outputPath = "resources/db/foodgroups.txt";
				break;
			case "groups":
				lines = TxtReader.foodGroupsToSql();
				outputPath = "resources/db/scripts/2_foodgroups_seed.sql";
				break;
			case "meta":
				lines = CsvReader.metaFoodToSql();
				outputPath = "resources/db/scripts/4_fooditems_meta_seed.sql";
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
