package controllers;

import tools.CsvReader;
import play.mvc.Controller;
import play.mvc.Result;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.List;

public class ApplicationController extends Controller {

    public Result index() {

        return ok("It works!");
    }

	public Result parseBasicFood() {

		List<String> lines = CsvReader.basicFoodToSql("resources/db/LivsmedelsDB_201702061629.csv");

		try {
			PrintStream printStream = new PrintStream(new File("resources/db/scripts/fooditems_seed.sql"));
			lines.forEach(printStream::println);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return ok("Done! Parsed " + lines.size() + " records into resources/db/scripts/fooditems_seed.sql");
	}

	public Result parseMetaFood() {

		List<String> lines = CsvReader.metaFoodToSql("resources/db/LivsmedelsDB_Meta_201702011104.csv");

		try {
			PrintStream printStream = new PrintStream(new File("resources/db/scripts/fooditems_meta_seed.sql"));
			lines.forEach(printStream::println);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return ok("Done! Parsed " + lines.size() + " records into resources/db/scripts/fooditems_meta_seed.sql");
	}
}
