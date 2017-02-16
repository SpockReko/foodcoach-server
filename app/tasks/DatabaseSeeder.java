package tasks;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.ServerConfig;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import me.tongfei.progressbar.ProgressBar;
import models.food.*;
import org.avaje.datasource.DataSourceConfig;
import play.Logger;
import tools.CommonTools;

import java.util.*;

/**
 * Created by fredrikkindstrom on 2017-02-13.
 */
public class DatabaseSeeder {

	private static EbeanServer db;

	private static final String basicPath = "resources/db/LivsmedelsDB_201702061629.csv";
	private static final String metaPath = "resources/db/LivsmedelsDB_Meta_201702011104.csv";

	private static final String GREEN = "\u001B[32m";
	private static final String YELLOW = "\u001B[33m";
	private static final String CYAN = "\u001B[36m";
	private static final String PURPLE = "\u001B[35m";
	private static final String RESET = "\u001B[0m";

	public static void main(String[] args) {

		db = getDatabase();

		System.out.println("\n" + PURPLE + "--- (Generating database) ---\n" + RESET);

		System.out.println("Importing foods from Excel...");
		try {
			importFoods();
		} catch (DatabaseNotEmptyException e) {
			System.out.println(YELLOW + "Foods already imported, moving on..." + RESET);
		}

		System.out.println("\nImporting meta food information... ");
		importFoodsMeta();

		System.out.println();
	}

	private static void importFoods() throws DatabaseNotEmptyException {

		CsvParserSettings settings = new CsvParserSettings();
		settings.getFormat().setLineSeparator("\n");
		settings.getFormat().setDelimiter(';');
		settings.setNumberOfRowsToSkip(1);

		CsvParser parser = new CsvParser(settings);
		List<String[]> allRows = parser.parseAll(CommonTools.getReader(basicPath));

		if (db.find(FoodItem.class).where().findCount() > 0) {
			throw new DatabaseNotEmptyException();
		}

		ProgressBar pb = new ProgressBar("Importing", allRows.size()).start();

		for (String[] cols : allRows) {
			FoodItem item = new FoodItem();
			item.fats = new Fats();
			item.sugars = new Sugars();
			item.vitamins = new Vitamins();
			item.minerals = new Minerals();
			item.name = cols[0];
			item.lmvFoodNumber = Long.parseLong(cols[1]);
			item.energyKcal = toFloat(cols[2]);
			item.energyKj = toFloat(cols[3]);
			item.carbohydrates = toFloat(cols[4]);
			item.fats.fat = toFloat(cols[5]);
			item.protein = toFloat(cols[6]);
			item.fibre = toFloat(cols[7]);
			item.water = toFloat(cols[8]);
			item.alcohol = toFloat(cols[9]);
			item.ash = toFloat(cols[10]);
			item.sugars.monosaccharides = toFloat(cols[11]);
			item.sugars.disaccharides = toFloat(cols[12]);
			item.sugars.sucrose = toFloat(cols[13]);
			item.wholeGrain = toFloat(cols[14]);
			item.sugars.sugars = toFloat(cols[15]);
			item.fats.sumSaturatedFats = toFloat(cols[16]);
			item.fats.fattyAcid40100 = toFloat(cols[17]);
			item.fats.fattyAcid120 = toFloat(cols[18]);
			item.fats.fattyAcid140 = toFloat(cols[19]);
			item.fats.fattyAcid160 = toFloat(cols[20]);
			item.fats.fattyAcid180 = toFloat(cols[21]);
			item.fats.fattyAcid200 = toFloat(cols[22]);
			item.fats.sumMonounsaturatedFats = toFloat(cols[23]);
			item.fats.fattyAcid161 = toFloat(cols[24]);
			item.fats.fattyAcid181 = toFloat(cols[25]);
			item.fats.sumPolyunsaturatedFats = toFloat(cols[26]);
			item.fats.fattyAcid182 = toFloat(cols[27]);
			item.fats.fattyAcid183 = toFloat(cols[28]);
			item.fats.fattyAcid204 = toFloat(cols[29]);
			item.fats.epaFattyAcid205 = toFloat(cols[30]);
			item.fats.dpaFattyAcid225 = toFloat(cols[31]);
			item.fats.dhaFattyAcid226 = toFloat(cols[32]);
			item.cholesterol = toFloat(cols[33]);
			item.vitamins.retinol = toFloat(cols[34]);
			item.vitamins.vitaminA = toFloat(cols[35]);
			item.vitamins.betaKaroten = toFloat(cols[36]);
			item.vitamins.vitaminD = toFloat(cols[37]);
			item.vitamins.vitaminE = toFloat(cols[38]);
			item.vitamins.vitaminK = toFloat(cols[39]);
			item.vitamins.thiamine = toFloat(cols[40]);
			item.vitamins.riboflavin = toFloat(cols[41]);
			item.vitamins.vitaminC = toFloat(cols[42]);
			item.vitamins.niacin = toFloat(cols[43]);
			item.vitamins.niacinEquivalents = toFloat(cols[44]);
			item.vitamins.vitaminB6 = toFloat(cols[45]);
			item.vitamins.vitaminB12 = toFloat(cols[46]);
			item.minerals.folate = toFloat(cols[47]);
			item.minerals.phosphorus = toFloat(cols[48]);
			item.minerals.iodine = toFloat(cols[49]);
			item.minerals.iron = toFloat(cols[50]);
			item.minerals.calcium = toFloat(cols[51]);
			item.minerals.potassium = toFloat(cols[52]);
			item.minerals.magnesium = toFloat(cols[53]);
			item.minerals.sodium = toFloat(cols[54]);
			item.minerals.salt = toFloat(cols[55]);
			item.minerals.selenium = toFloat(cols[56]);
			item.minerals.zink = toFloat(cols[57]);
			item.waste = toFloat(cols[58]);
			db.save(item);
			pb.step();
		}
		pb.stop();
	}

	private static void importFoodsMeta() {

		CsvParserSettings settings = new CsvParserSettings();
		settings.getFormat().setLineSeparator("\n");
		settings.getFormat().setDelimiter(',');
		settings.setNumberOfRowsToSkip(1);

		CsvParser parser = new CsvParser(settings);
		List<String[]> allRows = parser.parseAll(CommonTools.getReader(metaPath));

		ProgressBar pb = new ProgressBar("Importing", allRows.size(), 100).start();

		for (String[] row : allRows) {

			FoodItem item = db.find(FoodItem.class).where().eq("lmvFoodNumber", row[2]).findUnique();

			if (item == null) {
				Logger.warn("Found food in meta table not in database!");
				continue;
			}

			for (int i = 1; i < row.length; i++) {
				if (row[i] != null) {
					switch (i) {
						case 1: item.lmvProject = row[i]; break;
						case 2: break; //lmvFoodNumber
						case 3: item.scientificName = row[i]; break;
						case 4:
							String[] groups = row[i].split(";");
							for (String group : groups) {
								linkFoodGroup(item, CommonTools.extractNameAndCode(group));
							}
							break;
						case 5:
							String[] species = row[i].split(";");
							for (String s : species) {
								linkFoodSources(item, CommonTools.extractNameAndCode(s));
							}
							break;
						default:
							String[] nameOrCode = CommonTools.extractNameAndCode(row[i]);
							linkFoodLangual(item, getLangualType(i), nameOrCode);
					}
				}
			}

			db.save(item);
			pb.step();
		}
		pb.stop();
	}

	private static void linkFoodGroup(FoodItem item, String[] nameOrCode) {
		FoodGroup group;
		if (db.find(FoodGroup.class).where().eq("langualCode", nameOrCode[1]).findCount() == 0) {

			if (nameOrCode[1].isEmpty()) {
				FoodGroup existing = db.find(FoodGroup.class).where().eq("name", nameOrCode[0]).findUnique();
				if (existing == null) {
					group = new FoodGroup(nameOrCode[0], null);
				} else {
					group = existing;
				}
			} else {
				group = new FoodGroup(nameOrCode[0], nameOrCode[1]);
			}

			db.save(group);
		} else {
			group = db.find(FoodGroup.class).where().eq("langualCode", nameOrCode[1]).findUnique();
		}
		item.groups.add(group);
	}

	private static void linkFoodSources(FoodItem item, String[] nameOrCode) {
		FoodSource species;
		if (db.find(FoodSource.class).where().eq("langualCode", nameOrCode[1]).findCount() == 0) {

			if (nameOrCode[1].isEmpty()) {
				FoodSource existing = db.find(FoodSource.class).where().eq("name", nameOrCode[0]).findUnique();
				if (existing == null) {
					species = new FoodSource(nameOrCode[0], null);
				} else {
					species = existing;
				}
			} else {
				species = new FoodSource(nameOrCode[0], nameOrCode[1]);
			}

			db.save(species);
		} else {
			species = db.find(FoodSource.class).where().eq("langualCode", nameOrCode[1]).findUnique();
		}
		item.sources.add(species);
	}

	private static void linkFoodLangual(FoodItem item, LangualTerm.Type type,
		String[] nameOrCode) {
		LangualTerm term;
		if (db.find(LangualTerm.class).where().eq("code", nameOrCode[1]).findCount() == 0) {

			if (nameOrCode[1].isEmpty()) {
				LangualTerm existing = db.find(LangualTerm.class).where().eq("name", nameOrCode[0]).findUnique();
				if (existing == null) {
					term = new LangualTerm(null, nameOrCode[0], type);
				} else {
					term = existing;
				}
			} else {
				term = new LangualTerm(nameOrCode[1], nameOrCode[0], type);
			}

			db.save(term);
		} else {
			term = db.find(LangualTerm.class).where().eq("code", nameOrCode[1]).findUnique();
		}
		switch (type) {
			case PART_OF_PLANT_OR_ANIMAL: item.partOfPlantOrAnimal = term; break;
			case PHYSICAL_FORM: item.physicalForm = term; break;
			case HEAT_TREATMENT: item.heatTreatment = term; break;
			case COOKING_METHOD: item.cookingMethod = term; break;
 			case INDUSTRIAL_PROCESS: item.industrialProcess = term; break;
			case PRESERVATION_METHOD: item.preservationMethod = term; break;
			case PACKING_MEDIUM: item.packingMedium = term; break;
			case PACKING_TYPE: item.packingType = term; break;
			case PACKING_MATERIAL: item.packingMaterial = term; break;
			case LABEL_CLAIM: item.labelClaim = term; break;
			case GEOGRAPHIC_SOURCE: item.geographicSource = term; break;
			case DISTINCTIVE_FEATURES: item.distinctiveFeatures = term; break;
		}
	}
	
	/*
	Helper methods
	 */

	private static EbeanServer getDatabase() {
		String username = "root";
		String connectionString = "jdbc:mysql://localhost/foodcoach?verifyServerCertificate=false&useSSL=true";
		String dbDriver = "com.mysql.jdbc.Driver";
		DataSourceConfig foodDB = new DataSourceConfig();
		ServerConfig config = new ServerConfig();

		config.setName("mysql");
		foodDB.setDriver(dbDriver);
		foodDB.setUsername(username);
		foodDB.setPassword("");
		foodDB.setUrl(connectionString);
		config.setDataSourceConfig(foodDB);
		config.setDefaultServer(true);
		config.setRegister(false);

		return EbeanServerFactory.create(config);
	}
	private static LangualTerm.Type getLangualType(int column) {
		switch (column) {
			case 6:
				return LangualTerm.Type.PART_OF_PLANT_OR_ANIMAL;
			case 7:
				return LangualTerm.Type.PHYSICAL_FORM;
			case 8:
				return LangualTerm.Type.HEAT_TREATMENT;
			case 9:
				return LangualTerm.Type.COOKING_METHOD;
			case 10:
				return LangualTerm.Type.INDUSTRIAL_PROCESS;
			case 11:
				return LangualTerm.Type.PRESERVATION_METHOD;
			case 12:
				return LangualTerm.Type.PACKING_MEDIUM;
			case 13:
				return LangualTerm.Type.PACKING_TYPE;
			case 14:
				return LangualTerm.Type.PACKING_MATERIAL;
			case 15:
				return LangualTerm.Type.LABEL_CLAIM;
			case 16:
				return LangualTerm.Type.GEOGRAPHIC_SOURCE;
			case 17:
				return LangualTerm.Type.DISTINCTIVE_FEATURES;
			default: throw new IllegalArgumentException("Not a valid column!");
		}
	}
	private static Float toFloat(String col) {
		if (!col.equals("NULL")) {
			return Float.parseFloat(col);
		} else {
			return null;
		}
	}
	private static void printDone() {
		System.out.println(GREEN + "Done" + RESET);
	}

	private static class DatabaseNotEmptyException extends Exception {}
}
