package controllers;

import play.data.DynamicForm;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fredrikkindstrom on 2017-03-15.
 */
public class WeekMenuController extends Controller {

    @Inject FormFactory formFactory;

    // POST /weekmenu
    public Result weekMenu() {
        DynamicForm requestData = formFactory.form().bindFromRequest();
        String dateBorn = requestData.get("dateBorn");
        String sex = requestData.get("sex");
        String weight = requestData.get("weight");
        String length = requestData.get("length");
        String activityLevel = requestData.get("activityLevel");
        String goal = requestData.get("goal");
        String allergy = requestData.get("allergy");

        Map<String, String> map = new HashMap<>();
        map.put("dateBorn", dateBorn);
        map.put("sex", sex);
        map.put("weight", weight);
        map.put("length", length);
        map.put("activityLevel", activityLevel);
        map.put("goal", goal);
        map.put("allergy", allergy);

        return ok(Json.toJson(map));
    }
}
