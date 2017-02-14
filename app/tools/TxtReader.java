package tools;

import com.avaje.ebean.Model;
import models.food.FoodGroup;
import models.food.Part;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Contains logic for parsing CSV (Comma Separated Values) files.
 * @author Fredrik Kindstrom
 */
public class TxtReader {

	private static final String FOOD_GROUPS = "FoodGroups";
	private static final String PARTS = "Parts";
	private static final String[] COLS = { "name", "langual_code" };

	public static List<String> foodMetaToSql(Class<? extends Model> entity) {

		List<String> text = new LinkedList<>();
		String table = "";
		String path = "";

		if (entity.equals(FoodGroup.class)) {
			table = FOOD_GROUPS;
			path = "resources/db/foodgroups.txt";
		} else if (entity.equals(Part.class)) {
			table = PARTS;
			path = "resources/db/foodparts.txt";
		}

		try (BufferedReader br = new BufferedReader(
			new FileReader(path))) {

			String line;
			while ((line = br.readLine()) != null) {
				String sql = "";

				String[] nameOrCode = CommonTools.extractNameAndCode(line);

				sql += CommonTools.insertHeader(table, COLS);
				if (nameOrCode[1].isEmpty()) {
					sql += "'" + nameOrCode[0] + "', NULL);";
				} else {
					sql += "'" + nameOrCode[0] + "', '" + nameOrCode[1] + "');";
				}

				text.add(sql);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return text;
	}
}
