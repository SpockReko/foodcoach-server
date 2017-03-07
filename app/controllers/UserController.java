package controllers;


import models.user.User;
import play.mvc.Controller;
import play.mvc.Result;



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
        double vikt = 88;
        double langd = 177;
        double aktivitet = 1;
        User.Sex kon = User.Sex.FEMALE;

        User newuser = new User(kon, aktivitet, vikt, langd, age, mal2);

        newuser.dailyCalori();

        double kalori = newuser.hmap.get("bmr");


        return ok("Du bränner " + String.valueOf(newuser.hmap.get("vitaminANeedug") + " kalorier"));
    }

}
