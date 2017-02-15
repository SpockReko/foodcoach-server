package controllers;

import models.food.FoodItem;
import play.mvc.Controller;
import play.mvc.Result;

import tools.IngredientToFood;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.List;

import tools.IngredientToFood;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.List;

/**
 * HTTP controller that handles all general requests to the server.
 */
public class ApplicationController extends Controller {

    public Result index() {

        return ok("It works!");
    }

    public Result ingToFood (String str){
		FoodItem item = IngredientToFood.IngToFood(str);
		return ok(item.name);
	}
}
