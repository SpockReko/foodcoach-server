package controllers;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import helpers.JsonHelper;
import models.user.User;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;


/**
 * Created by Stolof on 24/02/17.
 */


public class UserController extends Controller {


    // POST /user/add:form
    public Result addUser(){
        User user = Form.form(User.class).bindFromRequest().get();
        if (User.find.where().eq("firstName", user.firstName).findCount() == 0){
            Ebean.save(user);
            return ok(user.firstName);
        } else {
            return badRequest(user.firstName);

        }
        //if (user != null){ JsonHelper.toJson(user)

        //} else{
         //    return badRequest("Form input incorrect or"+ user.firstName + "aldready exist" );
       // }
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
