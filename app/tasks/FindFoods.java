package tasks;

import play.Logger;
import tools.CsvReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.List;

/**
 * Created by fredrikkindstrom on 2017-02-15.
 */
public class FindFoods {

	private static final String GREEN = "\u001B[32m";
	private static final String YELLOW = "\u001B[33m";
	private static final String CYAN = "\u001B[36m";
	private static final String PURPLE = "\u001B[35m";
	private static final String RESET = "\u001B[0m";

	private static final String outputPath = "resources/db/scripts/1_fooditems_seed.sql";

	public static void main(String[] args) {

		System.out.println("\n" + PURPLE + "--- (Parsing Livsmedelsverkets Excel into SQL) ---\n" + RESET);

		System.out.print("Parsing... ");
		printToFile(CsvReader.foodBasicToSql(), outputPath);
		printDone();

		System.out.println("\nSQL file saved to " + YELLOW + outputPath + RESET);
		System.out.println("\nPlease run it on your MySQL server using this command:\n");
		System.out.println(CYAN + "mysql -u root foodcoach < resources/db/scripts/1_fooditems_seed.sql\n" + RESET);
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
