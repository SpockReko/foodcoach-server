package tools;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fredrikkindstrom on 2017-02-13.
 */
public class CommonTools {

	public static Reader getReader(String path) {
		try {
			return new BufferedReader(new FileReader(path));
		} catch (FileNotFoundException e) {
			throw new IllegalStateException("Unable to read input", e);
		}
	}

	public static String insertHeader(String table, String[] columns) {
		String statement = "";
		statement += "INSERT INTO " + table + " (";
		for (int i = 0; i < columns.length - 1; i++) {
			statement += columns[i] + ", ";
		}
		statement += columns[columns.length - 1];
		statement += ") VALUES (";
		return statement;
	}

	public static String[] extractNameAndCode(String line) {
		String code = "";
		Pattern pattern = Pattern.compile("[A-Z]\\d{4}");
		Matcher matcher = pattern.matcher(line);
		if (matcher.find()) code = matcher.group(0);
		String name = line.split("[A-Z]\\d{4}")[0];
		if (name.contains("()")) {
			name = name.substring(0, name.length()-1);
		}
		name = name.substring(0, name.length()-1);

		return new String[] { name.trim(), code };
	}
}
