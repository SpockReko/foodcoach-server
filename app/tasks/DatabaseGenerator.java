package tasks;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.ServerConfig;
import models.food.FoodGroup;
import models.food.LangualTerm;
import org.avaje.datasource.DataSourceConfig;
import play.Logger;
import tools.CsvReader;
import tools.TxtReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.List;

/**
 * Created by fredrikkindstrom on 2017-02-13.
 */
public class DatabaseGenerator {

	private static final EbeanServer db = getDatabase();

	private static final String GREEN = "\u001B[32m";
	private static final String YELLOW = "\u001B[33m";
	private static final String CYAN = "\u001B[36m";
	private static final String PURPLE = "\u001B[35m";
	private static final String RESET = "\u001B[0m";

	private static List<String> lines;
	private static String outputPath;

	private static final Class[] entities = { FoodGroup.class };
	private static final String[] txtPaths = { "resources/db/foodgroups.txt" };
	private static final String[] sqlPaths = { "resources/db/scripts/2_foodgroups_seed.sql" };

	public static void main(String[] args) {
		System.out.println("\n" + PURPLE + "--- (Generating database scripts) ---\n" + RESET);

		System.out.print("Parsing Livsmedelsverkets Excel into SQL... ");
		lines = CsvReader.foodBasicToSql();
		outputPath = "resources/db/scripts/1_fooditems_seed.sql";
		printToFile();
		printDone();

		System.out.println("Begin parsing meta information...");
		System.out.println("Identifying unique entities...");
		for (int i = 0; i < entities.length; i++) {
			System.out.print("Identifying " + CYAN + entities[i].getSimpleName() + "s" + RESET + "... ");
			lines = CsvReader.uniqueTermToTxt(entities[i]);
			outputPath = txtPaths[i];
			printToFile();
			printDone();
		}

		System.out.println("Writing SQL for entities");
		for (int i = 0; i < entities.length; i++) {
			System.out.print("Writing " + CYAN + entities[i].getSimpleName() + "s" + RESET + "... ");
			lines = TxtReader.foodMetaToSql(entities[i]);
			outputPath = sqlPaths[i];
			printToFile();
			printDone();
		}

		System.out.println();

		CsvReader.addTermToFoods(LangualTerm.Type.PACKING_TYPE);
	}

	private static EbeanServer getDatabase() {
		String username = "root";
		String connectionString = "jdbc:mysql://localhost/foodcoach?verifyServerCertificate=false&useSSL=true";
		String dbDriver = "com.mysql.jdbc.Driver";
		// You can use ebean.properties instead of programatically configure it.
		DataSourceConfig foodDB = new DataSourceConfig();
		ServerConfig config = new ServerConfig();
		config.setName("mysql");
		foodDB.setDriver(dbDriver);
		foodDB.setUsername(username);
		foodDB.setPassword("");
		foodDB.setUrl(connectionString);

		config.setDataSourceConfig(foodDB);

		config.setDefaultServer(false);
		config.setRegister(false);
		return EbeanServerFactory.create(config);
	}

	private static void printToFile() {
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
