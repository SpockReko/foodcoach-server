package controllers;

import models.food.FoodGroup;
import models.food.LangualTerm;
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
			case "link_groups":
//				lines = CsvReader.linkFoods(FoodGroup.class);
//				outputPath = "resources/db/scripts/X_fooditems_foodgroups_seed.sql";
				break;
			case "langual":
//				for (LangualTerm.Type type : LangualTerm.Type.values()) {
//					System.out.println("Building " + type.name());
//					CsvReader.addTermToFoods(type);
//				}
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

		return ok("Done!");
	}

}
