package controllers;

import models.food.Nutrient;
import models.user.User;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.ArrayList;


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
        User.Goal mal2 = User.Goal.INCREASE;
        double mal = 500;
        double vikt = 86;
        double langd = 177;
        double aktivitet = 1.5;
        User.Sex kon = User.Sex.MALE;
        ArrayList<String> allergi = new ArrayList<String>();
        allergi.add("svamp");
        allergi.add("Fisk");

        User newuser = new User(kon, aktivitet, vikt, langd, age, mal2, allergi);

        double kalori = newuser.hmap.get(Nutrient.KCAL);


        return ok("Du bränner " + String.valueOf(newuser.hmap.get(Nutrient.KCAL)) + " kalorier. Akta dig för " + newuser.allergier.get(1));
    }

}
