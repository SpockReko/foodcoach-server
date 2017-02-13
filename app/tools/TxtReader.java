package tools;

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

	public static List<String> foodGroupsToSql() {

		List<String> text = new LinkedList<>();

		try (BufferedReader br = new BufferedReader(
			new FileReader("resources/db/foodgroups.txt"))) {

			String line;
			while ((line = br.readLine()) != null) {
				String sql = "";

				String[] parts = line.split("\\(");

				parts[0] = parts[0].trim();
				parts[1] = parts[1].substring(0, parts[1].length()-1);

				sql += insertHeader(GROUPS_COLS);
				sql += "'" + parts[0] + "', '" + parts[1] + "');";

				text.add(sql);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return text;
	}

	private static String insertHeader(String[] tableColumns) {
		String statement = "";
		statement += "INSERT INTO " + FOOD_GROUPS + " (";
		for (int i = 0; i < tableColumns.length - 1; i++) {
			statement += tableColumns[i] + ", ";
		}
		statement += tableColumns[tableColumns.length - 1];
		statement += ") VALUES (";
		return statement;
	}
}
