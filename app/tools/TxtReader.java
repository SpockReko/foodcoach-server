package tools;

import models.food.FoodGroup;

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
	private static final String[] GROUPS_COLS = { "name", "langual_code" };

	public static List<String> foodAllMetaToSql() {

		List<String> text = new LinkedList<>();

		text.addAll(foodMetaToSql(FoodGroup.class));

		return text;
	}

	public static List<String> foodMetaToSql(Class entity) {

		List<String> text = new LinkedList<>();
		String table = "";
		String[] columns = {};
		String path = "";

		if (entity.equals(FoodGroup.class)) {
			table = FOOD_GROUPS;
			columns = GROUPS_COLS;
			path = "resources/db/foodgroups.txt";
		}

		try (BufferedReader br = new BufferedReader(
			new FileReader(path))) {

			String line;
			while ((line = br.readLine()) != null) {
				String sql = "";

				String[] parts = line.split("\\(");

				parts[0] = parts[0].trim();
				parts[1] = parts[1].substring(0, parts[1].length()-1);

				sql += CommonTools.insertHeader(table, columns);
				sql += "'" + parts[0] + "', '" + parts[1] + "');";

				text.add(sql);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return text;
	}
}
