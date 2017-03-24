package controllers;

import play.mvc.Controller;
import play.mvc.Result;

/**
 * HTTP controller that handles all general requests to the server.
 */
public class ApplicationController extends Controller {

    public Result index() {

        return ok("It works!");
    }
}
