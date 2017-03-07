package controllers;

import models.user.User;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import scala.tools.nsc.doc.model.Public;

import javax.persistence.PersistenceException;
import javax.sound.sampled.Control;
import java.util.List;

import static play.mvc.Results.ok;


/**
 * Created by Stolof on 24/02/17.
 */


public class UserController extends Controller {

/*    final static Form<User> userForm = form(User.class);

    public static Result Index() {
        return ok(index.render(userForm));
    }
    */

    public Result getRDI(int age) {
        return ok ( "your age is " + age);
    }



}
