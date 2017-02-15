package tasks;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.ServerConfig;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import models.food.FoodItem;
import models.food.LangualTerm;
import org.avaje.datasource.DataSourceConfig;
import play.Logger;
import tools.CommonTools;

import java.io.*;
import java.util.*;

/**
 * Created by fredrikkindstrom on 2017-02-13.
 */
public class DatabaseGenerator {

	private static EbeanServer db;

	private static final String GREEN = "\u001B[32m";
	private static final String YELLOW = "\u001B[33m";
	private static final String CYAN = "\u001B[36m";
	private static final String PURPLE = "\u001B[35m";
	private static final String RESET = "\u001B[0m";

	public static void main(String[] args) {

		db = getDatabase();

		System.out.println("\n" + PURPLE + "--- (Generating database) ---\n" + RESET);

		System.out.println("Identifying unique LanguaL terms...");
		for (LangualTerm.Type type : LangualTerm.Type.values()) {
			if (type == LangualTerm.Type.NONE) continue;
			System.out.print("Identifying " + CYAN + type + RESET + "... ");
			List<String> uniqueLangualTerms = extractUniqueLangual(type);
			printToFile(uniqueLangualTerms, getLangualPath(type));
			printDone();
		}

		System.out.println("Linking LanguaL terms to food...");
		for (LangualTerm.Type type : LangualTerm.Type.values()) {
			if (type == LangualTerm.Type.NONE) continue;
			System.out.print("Linking " + CYAN + type.name() + RESET + "... ");
			linkFoodLangual(type);
			printDone();
		}

		System.out.println();
	}

	private static List<String> extractUniqueLangual(LangualTerm.Type type) {

		List<String> text = new LinkedList<>();
		int langualCol = getLangualColumn(type);

		Set<String> set = new TreeSet<>((o1, o2) -> {
			String[] s1 = o1.split("\\(");
			String[] s2 = o2.split("\\(");
			if (s1.length == 1 | s2.length == 1) {
				return o1.compareTo(o2);
			} else {
				return s1[1].compareTo(s2[1]);
			}
		});

		CsvParserSettings settings = new CsvParserSettings();
		settings.getFormat().setLineSeparator("\n");
		settings.getFormat().setDelimiter(',');
		settings.setNumberOfRowsToSkip(1);

		CsvParser parser = new CsvParser(settings);
		List<String[]> allRows = parser.parseAll(CommonTools.getReader(
			"resources/db/LivsmedelsDB_Meta_201702011104.csv"));

		for (String[] cols : allRows) {
			if (cols[langualCol] != null) {
				String[] metas = cols[langualCol].split(";");
				Collections.addAll(set, metas);
			}
		}

		text.addAll(set);

		return text;
	}

	private static void linkFoodLangual(LangualTerm.Type type) {

		int langualCol = getLangualColumn(type);

		CsvParserSettings settings = new CsvParserSettings();
		settings.getFormat().setLineSeparator("\n");
		settings.getFormat().setDelimiter(',');
		settings.setNumberOfRowsToSkip(1);

		CsvParser parser = new CsvParser(settings);
		List<String[]> allRows = parser.parseAll(CommonTools.getReader(
			"resources/db/LivsmedelsDB_Meta_201702011104.csv"));

		for (String[] row : allRows) {

			FoodItem item = db.find(FoodItem.class).where().eq("lmvFoodNumber", row[2]).findUnique();
			if (row[langualCol] != null) {
				String[] nameOrCode = CommonTools.extractNameAndCode(row[langualCol]);
				persistFoodLangual(item, type, nameOrCode);
			}
		}
	}

	private static void persistFoodLangual(FoodItem item, LangualTerm.Type type,
		String[] nameOrCode) {
		LangualTerm term;
		if (db.find(LangualTerm.class).where().eq("code", nameOrCode[1]).findCount() == 0) {

			if (nameOrCode[1].isEmpty()) {
				LangualTerm existing = db.find(LangualTerm.class).where().eq("name", nameOrCode[0]).findUnique();
				if (existing == null) {
					term = new LangualTerm(null, nameOrCode[0], LangualTerm.Type.NONE);
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
			case PART_OF_PLANT_OR_ANIMAL: item.partOfAnimalOrPlant = term; break;
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
		db.save(item);
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
	private static int getLangualColumn(LangualTerm.Type type) {
		switch (type) {
			case PART_OF_PLANT_OR_ANIMAL:
				return 6;
			case PHYSICAL_FORM:
				return 7;
			case HEAT_TREATMENT:
				return 8;
			case COOKING_METHOD:
				return 9;
			case INDUSTRIAL_PROCESS:
				return 10;
			case PRESERVATION_METHOD:
				return 11;
			case PACKING_MEDIUM:
				return 12;
			case PACKING_TYPE:
				return 13;
			case PACKING_MATERIAL:
				return 14;
			case LABEL_CLAIM:
				return 15;
			case GEOGRAPHIC_SOURCE:
				return 16;
			case DISTINCTIVE_FEATURES:
				return 17;
		}
		return -1;
	}
	private static String getLangualPath(LangualTerm.Type type) {
		return "resources/db/data/" + type.name().toLowerCase() + ".txt";
	}
	private static void printToFile(List<String> lines, String outputPath) {
		try {
			PrintStream printStream = new PrintStream(new File(outputPath));
			if (lines != null) lines.forEach(printStream::println);
		} catch (FileNotFoundException e) {
			Logger.error("ParseError: cant write to path " + outputPath);
		}
	}
	private static void printDone() {
		System.out.println(GREEN + "Done" + RESET);
	}
}
