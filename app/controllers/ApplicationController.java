package controllers;

import parsers.Csv;
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

	public Result parseCsv() {

		List<String> lines = Csv.lmvToSql("resources/db/LivsmedelsDB_201702061629.csv");

		try {
			PrintStream printStream = new PrintStream(new File("resources/db/scripts/fooditems_seed.sql"));
			lines.forEach(printStream::println);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return ok("Done!");
	}
}
