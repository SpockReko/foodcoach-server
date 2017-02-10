package controllers;

import parsers.LMVParser;
import play.mvc.Controller;
import play.mvc.Result;

public class ApplicationController extends Controller {

    public Result index() {

        return ok("It works!");
    }

    public Result parseCSV() {

		LMVParser loader = new LMVParser();

		return ok(loader.getSql());
	}
}
