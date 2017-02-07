package helpers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by fredrikkindstrom on 2017-02-07.
 */
public class LMVExcelLoader {

	private static final String TABLE_NAME = "FoodItems";
	private static final String csvFile = "resources/LivsmedelsDB_201702061629.csv";
	private String sql;

	public String getSql() {

		sql = "";
		String line;

		try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

			while ((line = br.readLine()) != null) {

				String[] row = line.split(";");

				for (int i = 2; i < row.length; i++) {
					row[i] = row[i].replace(',', '.');
				}

				Map<String, String> text = readRowText(row);
				long lmvFoodNumber = readRowLmvNumber(row);
				Map<String, Float> numbers = readRowNumbers(row);

				generateInsertStatement();

				System.out.println(text.get("name"));

				sql += text.get("name") + ", ";
				sql += lmvFoodNumber + ", ";
				sql += numbers.get("energyKcal") + ", ";
				sql += numbers.get("energyKj") + ", ";
				sql += numbers.get("carbohydrates") + ", ";
				sql += numbers.get("protein") + ", ";
				sql += numbers.get("fibre") + ", ";
				sql += numbers.get("wholeGrain") + ", ";
				sql += numbers.get("cholesterol") + ", ";
				sql += numbers.get("water") + ", ";
				sql += numbers.get("alcohol") + ", ";
				sql += numbers.get("ash") + ", ";
				sql += numbers.get("wastePercent") + ", ";
				sql += numbers.get("sugars") + ", ";
				sql += numbers.get("monosaccharides") + ", ";
				sql += numbers.get("disaccharides") + ", ";
				sql += numbers.get("sucrose") + ", ";
				sql += numbers.get("fat") + ", ";
				sql += numbers.get("fattyAcid120") + ", ";
				sql += numbers.get("fattyAcid140") + ", ";
				sql += numbers.get("fattyAcid160") + ", ";
				sql += numbers.get("fattyAcid180") + ", ";
				sql += numbers.get("fattyAcid200") + ", ";
				sql += numbers.get("sumMonounsaturatedFats") + ", ";
				sql += numbers.get("fattyAcid161") + ", ";
				sql += numbers.get("fattyAcid181") + ", ";
				sql += numbers.get("sumPolyunsaturatedFats") + ", ";
				sql += numbers.get("fattyAcid182") + ", ";
				sql += numbers.get("fattyAcid183") + ", ";
				sql += numbers.get("fattyAcid204") + ", ";
				sql += numbers.get("epaFattyAcid205") + ", ";
				sql += numbers.get("dpaFattyAcid225") + ", ";
				sql += numbers.get("dhaFattyAcid226") + ", ";
				sql += numbers.get("retinol") + ", ";
				sql += numbers.get("betaKaroten") + ", ";
				sql += numbers.get("vitaminA") + ", ";
				sql += numbers.get("vitaminB6") + ", ";
				sql += numbers.get("vitaminB12") + ", ";
				sql += numbers.get("vitaminC") + ", ";
				sql += numbers.get("vitaminD") + ", ";
				sql += numbers.get("vitaminE") + ", ";
				sql += numbers.get("vitaminK") + ", ";
				sql += numbers.get("thiamine") + ", ";
				sql += numbers.get("riboflavin") + ", ";
				sql += numbers.get("niacin") + ", ";
				sql += numbers.get("niacinEquivalents") + ", ";
				sql += numbers.get("folate") + ", ";
				sql += numbers.get("folate") + ", ";
				sql += numbers.get("phosphorus") + ", ";
				sql += numbers.get("iodine") + ", ";
				sql += numbers.get("iron") + ", ";
				sql += numbers.get("calcium") + ", ";
				sql += numbers.get("potassium") + ", ";
				sql += numbers.get("magnesium") + ", ";
				sql += numbers.get("sodium") + ", ";
				sql += numbers.get("salt") + ", ";
				sql += numbers.get("selenium") + ", ";
				sql += numbers.get("zink") + ", ";
				sql += ");\n";
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return sql;
	}

	private Map<String, String> readRowText(String[] row) {
		Map<String, String> text = new HashMap<>();

		text.put("name", row[0].trim());

		return text;
	}

	private Long readRowLmvNumber(String[] row) {
		return Long.parseLong(row[1]);
	}

	private Map<String, Float> readRowNumbers(String[] row) {
		Map<String, Float> numbers = new HashMap<>();

		for (int i = 2; i < row.length; i++) {
			if (row[i].equals("")) {
				row[i] = "0";
			}
		}

		numbers.put("energyKcal", Float.parseFloat(row[2]));
		numbers.put("energyKj", Float.parseFloat(row[3]));
		numbers.put("carbohydrates", Float.parseFloat(row[4]));
		numbers.put("fat", Float.parseFloat(row[5]));
		numbers.put("protein", Float.parseFloat(row[6]));
		numbers.put("fibre", Float.parseFloat(row[7]));
		numbers.put("water", Float.parseFloat(row[8]));
		numbers.put("alcohol", Float.parseFloat(row[9]));
		numbers.put("ash", Float.parseFloat(row[10]));
		numbers.put("monosaccharides", Float.parseFloat(row[11]));
		numbers.put("disaccharides", Float.parseFloat(row[12]));
		numbers.put("sucrose", Float.parseFloat(row[13]));
		numbers.put("wholeGrain", Float.parseFloat(row[14]));
		numbers.put("sugars", Float.parseFloat(row[15]));
		numbers.put("sumSaturatedFats", Float.parseFloat(row[16]));
		numbers.put("fattyAcid40100", Float.parseFloat(row[17]));
		numbers.put("fattyAcid120", Float.parseFloat(row[18]));
		numbers.put("fattyAcid140", Float.parseFloat(row[19]));
		numbers.put("fattyAcid160", Float.parseFloat(row[20]));
		numbers.put("fattyAcid180", Float.parseFloat(row[21]));
		numbers.put("fattyAcid200", Float.parseFloat(row[22]));
		numbers.put("sumMonounsaturatedFats", Float.parseFloat(row[23]));
		numbers.put("fattyAcid161", Float.parseFloat(row[24]));
		numbers.put("fattyAcid181", Float.parseFloat(row[25]));
		numbers.put("sumPolyunsaturatedFats", Float.parseFloat(row[26]));
		numbers.put("fattyAcid182", Float.parseFloat(row[27]));
		numbers.put("fattyAcid183", Float.parseFloat(row[28]));
		numbers.put("fattyAcid204", Float.parseFloat(row[29]));
		numbers.put("epaFattyAcid205", Float.parseFloat(row[30]));
		numbers.put("dpaFattyAcid225", Float.parseFloat(row[31]));
		numbers.put("dhaFattyAcid226", Float.parseFloat(row[32]));
		numbers.put("cholesterol", Float.parseFloat(row[33]));
		numbers.put("retinol", Float.parseFloat(row[34]));
		numbers.put("vitaminA", Float.parseFloat(row[35]));
		numbers.put("betaKaroten", Float.parseFloat(row[36]));
		numbers.put("vitaminD", Float.parseFloat(row[37]));
		numbers.put("vitaminE", Float.parseFloat(row[38]));
		numbers.put("vitaminK", Float.parseFloat(row[39]));
		numbers.put("thiamine", Float.parseFloat(row[40]));
		numbers.put("riboflavin", Float.parseFloat(row[41]));
		numbers.put("vitaminC", Float.parseFloat(row[42]));
		numbers.put("niacin", Float.parseFloat(row[43]));
		numbers.put("niacinEquivalents", Float.parseFloat(row[44]));
		numbers.put("vitaminB6", Float.parseFloat(row[45]));
		numbers.put("vitaminB12", Float.parseFloat(row[46]));
		numbers.put("folate", Float.parseFloat(row[47]));
		numbers.put("phosphorus", Float.parseFloat(row[48]));
		numbers.put("iodine", Float.parseFloat(row[49]));
		numbers.put("iron", Float.parseFloat(row[50]));
		numbers.put("calcium", Float.parseFloat(row[51]));
		numbers.put("potassium", Float.parseFloat(row[52]));
		numbers.put("magnesium", Float.parseFloat(row[53]));
		numbers.put("sodium", Float.parseFloat(row[54]));
		numbers.put("salt", Float.parseFloat(row[55]));
		numbers.put("selenium", Float.parseFloat(row[56]));
		numbers.put("zink", Float.parseFloat(row[57]));
		if (row.length > 58) {
			numbers.put("wastePercent", Float.parseFloat(row[58]));
		}

		return numbers;
	}

	private void generateInsertStatement() {
		sql += "INSERT INTO " + TABLE_NAME + " (";
		sql += "name, lmv_food_number, energy_kcal, energy_kj, ";
		sql += "carbohydrates_g, protein_g, fibre_g, whole_grain_g, ";
		sql += "cholesterol_mg, water_g, alcohol_g, ash_g, waste_percent";
		sql += "sugars_g, monosaccharides_g, disaccharides_g, sucrose_g, fat_g";
		sql += "sum_saturated_fats_g, fatty_acid_40_100_g, fatty_acid_120_g, ";
		sql += "fatty_acid_140_g, fatty_acid_160_g, fatty_acid_180_g, ";
		sql += "fatty_acid_200_g, sum_monounsaturated_fats_g, fatty_acid_161_g, ";
		sql += "fatty_acid_181_g, sum_polyunsaturated_fats_g, fatty_acid_182_g, ";
		sql += "fatty_acid_183_g, fatty_acid_204_g, epa_fatty_acid_205_g, ";
		sql += "dpa_fatty_acid_225_g, dha_fatty_acid_226_g, ";
		sql += "retinol_ug, beta_karoten_ug, vitamin_a_ug, vitamin_b6_ug, vitamin_b12_ug";
		sql += "vitamin_c_mg, vitamin_d_ug, vitamin_e_mg, vitamin_k_ug, thiamine_mg";
		sql += "riboflavin_mg, niacin_mg, niacin_equivalents_mg, folate_ug, phosphorus_mg";
		sql += "iodine_ug, iron_mg, calcium_mg, potassium_mg, magnesium_mg";
		sql += "sodium_mg, salt_g, selenium_ug, zink_mg";
		sql += ")\n";
		sql += "VALUES (";
	}
}
