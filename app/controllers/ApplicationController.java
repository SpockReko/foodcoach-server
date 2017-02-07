package controllers;

import helpers.LMVExcelLoader;
import play.mvc.Controller;
import play.mvc.Result;

public class ApplicationController extends Controller {

    public Result index() {

        return ok("It works!");
    }

    public Result parseCSV() {

		LMVExcelLoader loader = new LMVExcelLoader();

		return ok(loader.getSql());
	}
}
