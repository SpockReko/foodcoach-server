package controllers;

import com.avaje.ebean.Ebean;
import helpers.JsonHelper;
import models.food.Nutrient;
import models.user.User;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import java.util.ArrayList;


/**
 * Created by Stolof on 24/02/17.
 */


public class UserController extends Controller {


    // POST /user/add:form
    public Result addUser(){
        User user = Form.form(User.class).bindFromRequest().get();
        Ebean.save(user);
         if(user != null) return ok(JsonHelper.toJson(user));
        else{
            return badRequest("YOU FUCKED UP");
        }
    }

    // GET /user/name/:name
    public Result getUserByName(String name) {
        User user = User.find.where().eq("firstName", name).findUnique();
        User user2 = new User(user.firstName);
        if (user2 != null) {
            return ok(JsonHelper.toJson(user2));
        } else {
            return badRequest("User \"" + name + "\" does not exist");
        }
    }


    // GET /user/name/:id
    public Result getUserById(int id) {
        User user = User.find.where().eq("id", id).findUnique();
        User user2 = new User(user.firstName);
        if (user2 != null) {
            return ok(JsonHelper.toJson(user2));
        } else {
            return badRequest("User \"" + id + "\" does not exist");
        }
    }


}
