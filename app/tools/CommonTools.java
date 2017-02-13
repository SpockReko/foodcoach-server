package tools;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

/**
 * Created by fredrikkindstrom on 2017-02-13.
 */
class CommonTools {

	static Reader getReader(String path) {
		try {
			return new BufferedReader(new FileReader(path));
		} catch (FileNotFoundException e) {
			throw new IllegalStateException("Unable to read input", e);
		}
	}

	static String insertHeader(String table, String[] columns) {
		String statement = "";
		statement += "INSERT INTO " + table + " (";
		for (int i = 0; i < columns.length - 1; i++) {
			statement += columns[i] + ", ";
		}
		statement += columns[columns.length - 1];
		statement += ") VALUES (";
		return statement;
	}
}
