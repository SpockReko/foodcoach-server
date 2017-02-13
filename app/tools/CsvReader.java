package tools;

import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Contains logic for parsing CSV (Comma Separated Values) files.
 * @author Fredrik Kindstrom
 */
public class CsvReader {

	private static final String NULL = "NULL";
	private static final String FOOD_ITEMS = "FoodItems";
	private static final String[] BASIC_FOOD_COLS =
		{ "name", "lmv_food_number", "energy_kcal", "energy_kj", "carbohydrates_g", "fat_g",
			"protein_g", "fibre_g", "water_g", "alcohol_g", "ash_g", "monosaccharides_g",
			"disaccharides_g", "sucrose_g", "whole_grain_g", "sugars_g", "sum_saturated_fats_g",
			"fatty_acid_40_100_g", "fatty_acid_120_g", "fatty_acid_140_g", "fatty_acid_160_g",
			"fatty_acid_180_g", "fatty_acid_200_g", "sum_monounsaturated_fats_g",
			"fatty_acid_161_g", "fatty_acid_181_g", "sum_polyunsaturated_fats_g",
			"fatty_acid_182_g", "fatty_acid_183_g", "fatty_acid_204_g", "epa_fatty_acid_205_g",
			"dpa_fatty_acid_225_g", "dha_fatty_acid_226_g", "cholesterol_mg", "retinol_ug",
			"vitamin_a_ug", "beta_karoten_ug", "vitamin_d_ug", "vitamin_e_mg", "vitamin_k_ug",
			"thiamine_mg", "riboflavin_mg", "vitamin_c_mg", "niacin_mg", "niacin_equivalents_mg",
			"vitamin_b6_ug", "vitamin_b12_ug", "folate_ug", "phosphorus_mg", "iodine_ug", "iron_mg",
			"calcium_mg", "potassium_mg", "magnesium_mg", "sodium_mg", "salt_g", "selenium_ug",
			"zink_mg", "waste_percent" };
	private static final String[] META_FOOD_COLS = { "scientific_name", "lmv_project" };

	/**
	 * Parses Livsmedelsverkets provided Excel-sheet to SQL statements.
	 * @param path The path to the data as a csv file.
	 * @return A list of all the SQL INSERT rows. Every string corresponds to
	 * an insert statement for one record in the database.
	 */
	public static List<String> basicFoodToSql(String path) {

		List<String> text = new LinkedList<>();

		CsvParserSettings settings = new CsvParserSettings();
		settings.getFormat().setLineSeparator("\n");
		settings.getFormat().setDelimiter(';');
		settings.setNumberOfRowsToSkip(1);

		CsvParser parser = new CsvParser(settings);
		List<String[]> allRows = parser.parseAll(getReader(path));

		for (String[] cols : allRows) {
			String row = "";
			row += insertHeader(BASIC_FOOD_COLS);
			for (int i = 0; i < cols.length - 1; i++) {
				if (cols[i].equals(NULL)) {
					row += cols[i] + ", ";
				} else {
					row += "'" + cols[i] + "', ";
				}
			}
			if (cols[cols.length - 1].equals(NULL)) {
				row += cols[cols.length - 1] + ");";
			} else {
				row += "'" + cols[cols.length - 1] + "');";
			}
			text.add(row);
		}

		return text;
	}

	public static List<String> metaFoodToSql(String path) {

		List<String> text = new LinkedList<>();

		CsvParserSettings settings = new CsvParserSettings();
		settings.getFormat().setLineSeparator("\n");
		settings.getFormat().setDelimiter(',');
		settings.setNumberOfRowsToSkip(1);

		CsvParser parser = new CsvParser(settings);
		List<String[]> allRows = parser.parseAll(getReader(path));

		for (String[] cols : allRows) {
			String row = "";
			String[] data = { cols[3], cols[1] };
			row += update(META_FOOD_COLS, data, cols[2]);
			text.add(row);
		}

		return text;
	}

	private static String insertHeader(String[] tableColumns) {
		String statement = "";
		statement += "INSERT INTO " + FOOD_ITEMS + " (";
		for (int i = 0; i < tableColumns.length - 1; i++) {
			statement += tableColumns[i] + ", ";
		}
		statement += tableColumns[tableColumns.length - 1];
		statement += ") VALUES (";
		return statement;
	}

	private static String update(String[] tableColumns, String[] data,
		String lmvFoodNumber) {
		String statement = "";
		statement += "UPDATE " + FOOD_ITEMS + " SET ";
		for (int i = 0; i < tableColumns.length - 1; i++) {
			statement += tableColumns[i] + " = '" + data[i] + "', ";
		}
		statement += tableColumns[tableColumns.length - 1] + " = '" + data[data.length - 1] + "'";
		statement += " WHERE lmv_food_number = " + lmvFoodNumber;
		return statement;
	}

	private static Reader getReader(String path) {
		try {
			return new BufferedReader(new FileReader(path));
		} catch (FileNotFoundException e) {
			throw new IllegalStateException("Unable to read input", e);
		}
	}
}
