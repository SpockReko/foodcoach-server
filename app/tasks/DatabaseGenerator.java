package tasks;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.Model;
import com.avaje.ebean.config.ServerConfig;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import models.food.FoodGroup;
import models.food.FoodItem;
import models.food.LangualTerm;
import org.avaje.datasource.DataSourceConfig;
import play.Logger;
import tools.CommonTools;
import tools.CsvReader;
import tools.TxtReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

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

	private static final Class[] entities = { FoodGroup.class };
	private static final String[] txtPaths = { "resources/db/data/foodgroups.txt" };
	private static final String[] sqlPaths = { "resources/db/scripts/2_foodgroups_seed.sql" };

	public static void main(String[] args) {

		db = getDatabase();

		System.out.println("\n" + PURPLE + "--- (Generating database scripts) ---\n" + RESET);

		System.out.print("Parsing Livsmedelsverkets Excel into SQL... ");
		printToFile(CsvReader.foodBasicToSql(), "resources/db/scripts/1_fooditems_seed.sql");
		printDone();

		System.out.println("Begin parsing meta information...");
		System.out.println("Identifying unique entities...");
		for (int i = 0; i < entities.length; i++) {
			System.out.print("Identifying " + CYAN + entities[i].getSimpleName() + "s" + RESET + "... ");
			printToFile(CsvReader.uniqueTermToTxt(entities[i]), txtPaths[i]);
			printDone();
		}

		System.out.println("Writing SQL for entities");
		for (int i = 0; i < entities.length; i++) {
			System.out.print("Writing " + CYAN + entities[i].getSimpleName() + "s" + RESET + "... ");
			printToFile(TxtReader.foodMetaToSql(entities[i]), sqlPaths[i]);
			printDone();
		}

		System.out.println("Adding LanguaL terms to food...");
		for (LangualTerm.Type type : LangualTerm.Type.values()) {
			System.out.print("Adding " + CYAN + type.name() + RESET + "... ");
			addTermToFoods(type);
			printDone();
		}

		System.out.println();
	}

	public static List<String> linkFoods(Class<? extends Model> entity) {

		List<String> text = new LinkedList<>();
		int entityCol;

		if (entity.equals(FoodGroup.class)) {
			entityCol = 4;
		} else {
			throw new IllegalArgumentException("Not a valid model!");
		}

		CsvParserSettings settings = new CsvParserSettings();
		settings.getFormat().setLineSeparator("\n");
		settings.getFormat().setDelimiter(',');
		settings.setNumberOfRowsToSkip(1);

		CsvParser parser = new CsvParser(settings);
		List<String[]> allRows = parser.parseAll(CommonTools.getReader(
			"resources/db/LivsmedelsDB_Meta_201702011104.csv"));

		for (String[] row : allRows) {
			FoodItem item = db.find(FoodItem.class).where().eq("lmvFoodNumber", row[2]).findUnique();
			if (row[entityCol] != null) {
				String[] values = row[entityCol].split(";");
				for (String value : values) {
					String[] nameOrCode = CommonTools.extractNameAndCode(value);
					Model link = db.find(entity).where().eq("code", nameOrCode[1]).findUnique();
					if (link != null) text.add(insertLink(item, link));
				}
			}
		}

		return text;
	}

	private static void addTermToFoods(LangualTerm.Type langualType) {

		int entityCol = getLangualColumn(langualType);

		CsvParserSettings settings = new CsvParserSettings();
		settings.getFormat().setLineSeparator("\n");
		settings.getFormat().setDelimiter(',');
		settings.setNumberOfRowsToSkip(1);

		CsvParser parser = new CsvParser(settings);
		List<String[]> allRows = parser.parseAll(CommonTools.getReader(
			"resources/db/LivsmedelsDB_Meta_201702011104.csv"));

		for (String[] row : allRows) {
			FoodItem item = db.find(FoodItem.class).where().eq("lmvFoodNumber", row[2]).findUnique();
			if (row[entityCol] != null) {
				String[] nameOrCode = CommonTools.extractNameAndCode(row[entityCol]);
				if (!nameOrCode[1].isEmpty()) saveLangual(item, langualType, nameOrCode);
			}
		}
	}

	private static String insertLink(FoodItem item, Model link) {
		String table = "fooditems_";
		String linkName = "";
		long linkId = 0;
		if (link instanceof FoodGroup) {
			linkId = ((FoodGroup) link).id;
			table += "foodgroups";
			linkName = "food_groups_id";
		}
		String statement = "";
		statement += "INSERT INTO " + table + " (food_items_id, ";
		statement += linkName + ") VALUES (";
		statement += item.id + ", " + linkId + ");";
		return statement;
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

	private static void saveLangual(FoodItem item, LangualTerm.Type langualType,
		String[] nameOrCode) {
		LangualTerm term;
		if (db.find(LangualTerm.class).where().eq("code", nameOrCode[1]).findCount() == 0) {
			term = new LangualTerm(nameOrCode[1], nameOrCode[0], langualType);
			db.save(term);
		} else {
			term = db.find(LangualTerm.class).where().eq("code", nameOrCode[1]).findUnique();
		}
		switch (langualType) {
			case PART_OF_PLANT_OR_ANIMAL: item.partOfAnimalOrPlant = term; break;
			case PHYSICAL_FORM: item.physicalForm = term; break;
			case HEAT_TREATMENT:
			case COOKING_METHOD:
			case INDUSTRIAL_PROCESS:
			case PRESERVATION_METHOD:
			case PACKING_MEDIUM:
			case PACKING_TYPE:
			case PACKING_MATERIAL:
			case LABEL_CLAIM:
			case GEOGRAPHIC_SOURCE:
			case DISTINCTIVE_FEATURES:
		}
		db.save(item);
	}

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
		//System.out.print(YELLOW + lines.size() + RESET);
		//System.out.println(" records into " + outputPath);
	}

}
