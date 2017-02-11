package parsers;

import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by fredrikkindstrom on 2017-02-07.
 */
public class Csv {

	private static final String FOOD_ITEM_TABLE = "FoodItems";

	public static List<String> lmvToSql(String path) {

		List<String> text = new LinkedList<>();

		CsvParserSettings settings = new CsvParserSettings();
		settings.getFormat().setLineSeparator("\n");
		settings.getFormat().setDelimiter(';');

		CsvParser parser = new CsvParser(settings);

		List<String[]> allRows = parser.parseAll(getReader(path));

		for (String[] cols : allRows) {
			String row = "";
			row += getInsert();
			for (int i = 0; i < cols.length - 1; i++) {
				if (cols[i].equals("NULL")) {
					row += cols[i] + ", ";
				} else {
					row += "'" + cols[i] + "', ";
				}
			}
			if (cols[cols.length - 1].equals("NULL")) {
				row += cols[cols.length - 1] + ");";
			} else {
				row += "'" + cols[cols.length - 1] + "');";
			}
			text.add(row);
		}

		return text;
	}

	/**
	 * Creates a reader for a resource in the path.
	 * @param path path of the resource to be read.
	 * @return a reader of the resource
	 */
	private static Reader getReader(String path) {
		try {
			return new BufferedReader(new FileReader(path));
		} catch (FileNotFoundException e) {
			throw new IllegalStateException("Unable to read input", e);
		}
	}

	private static String getInsert() {
		String insert = "";
		insert += "INSERT INTO " + FOOD_ITEM_TABLE + " (";
		insert += "name, lmv_food_number, energy_kcal, energy_kj, ";
		insert += "carbohydrates_g, fat_g, protein_g, fibre_g, water_g, ";
		insert += "alcohol_g, ash_g, monosaccharides_g, disaccharides_g, ";
		insert += "sucrose_g, whole_grain_g, sugars_g, ";
		insert += "sum_saturated_fats_g, fatty_acid_40_100_g, fatty_acid_120_g, ";
		insert += "fatty_acid_140_g, fatty_acid_160_g, fatty_acid_180_g, ";
		insert += "fatty_acid_200_g, sum_monounsaturated_fats_g, fatty_acid_161_g, ";
		insert += "fatty_acid_181_g, sum_polyunsaturated_fats_g, fatty_acid_182_g, ";
		insert += "fatty_acid_183_g, fatty_acid_204_g, epa_fatty_acid_205_g, ";
		insert += "dpa_fatty_acid_225_g, dha_fatty_acid_226_g, ";
		insert += "cholesterol_mg, ";
		insert += "retinol_ug, vitamin_a_ug, beta_karoten_ug, vitamin_d_ug, ";
		insert += "vitamin_e_mg, vitamin_k_ug, thiamine_mg, ";
		insert += "riboflavin_mg, vitamin_c_mg, niacin_mg, niacin_equivalents_mg, ";
		insert += "vitamin_b6_ug, vitamin_b12_ug, ";
		insert += "folate_ug, phosphorus_mg, ";
		insert += "iodine_ug, iron_mg, calcium_mg, potassium_mg, magnesium_mg, ";
		insert += "sodium_mg, salt_g, selenium_ug, zink_mg, waste_percent";
		insert += ") VALUES (";
		return insert;
	}
}
