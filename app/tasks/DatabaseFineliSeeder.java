package tasks;

import com.avaje.ebean.EbeanServer;
import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;
import me.tongfei.progressbar.ProgressBar;
import models.food.*;
import models.food.fineli.GeneralFood;
import models.food.fineli.Processing;
import models.food.fineli.SpecialDiet;
import models.food.fineli.SpecificFood;

import javax.persistence.PersistenceException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.*;

/**
 * Parses through the provided CSV file given by Livsmedelsverket containing foods and their
 * basic data. Sets the correct values to the Java objects and persists them to the database
 * using the Ebean ORM included with the application. Also parses through and insert the
 * meta information provided by Livsmedelsverket for each food.
 * This has been scraped from their website and put into another CSV file.
 * <p>
 * TL;DR Puts all the Livsmedelsverket food data into the database.
 *
 * @author Fredrik Kindstrom
 */
public class DatabaseFineliSeeder {

    private static EbeanServer db;

    private static final String GENERAL_TSV = "resources/fineli_food/Fineli_GeneralFoods_Complete.tsv";
    private static final String SPECIFIC_TSV = "resources/fineli_food/Fineli_SpecificFoods.tsv";

    private static final int GEN_ID = 0;
    private static final int GEN_NAME = 1;
    private static final int GEN_DEFAULT = 2;
    private static final int GEN_SPECIFIC_TAGS = 3;
    private static final int GEN_DISPLAY_NAME = 4;
    private static final int GEN_EXTRA_TAGS = 5;
    private static final int GEN_PIECE_WEIGHT = 6;
    private static final int GEN_DENSITY_CONSTANT = 7;
    private static final int GEN_EXAMPLE_BRANDS = 8;
    private static final int GEN_SCIENTIFIC_NAME = 9;
    private static final int GEN_PROCESSING = 10;
    private static final int GEN_SPECIAL_DIETS = 13;

    private static Set<GeneralFood> generalFoods = new HashSet<>();
    private static List<SpecificFood> specificFoods = new LinkedList<>();

    public static void main(String[] args) {

        db = CommonTools.getDatabase();

        System.out.println(
            "\n" + CommonTools.PURPLE + "--- (Seeding database) ---\n" + CommonTools.RESET);

        try {
            db.find(FoodItem.class).where().eq("id", "1").findUnique();
        } catch (PersistenceException e) {
            System.out.println(CommonTools.YELLOW
                + "No database tables present. Please start server and run evolution script first!\n"
                + CommonTools.RESET);
            return;
        }

        System.out.println(CommonTools.CYAN + "Importing foods from TSV..." + CommonTools.RESET);
        try {
            importFoods();
        } catch (DatabaseNotEmptyException e) {
            System.out.println(
                CommonTools.YELLOW + "Foods already imported, moving on..." + CommonTools.RESET);
        }

        System.out.println();
    }

    private static void importFoods() throws DatabaseNotEmptyException {

        TsvParserSettings settings = new TsvParserSettings();
        settings.getFormat().setLineSeparator("\n");
        settings.setNumberOfRowsToSkip(1);

        TsvParser parser = new TsvParser(settings);
        List<String[]> generalRows = parser.parseAll(getReader(GENERAL_TSV));
        List<String[]> specificRows = parser.parseAll(getReader(SPECIFIC_TSV));

        if (db.find(SpecificFood.class).where().findCount() > 0) {
            throw new DatabaseNotEmptyException();
        }

        ProgressBar pb = new ProgressBar("Importing", generalRows.size(), 50).start();

        for (String[] cols : generalRows) {
            readGeneralRow(cols);
            pb.step();
        }
        db.saveAll(generalFoods);


        pb.stop();
    }

    private static void readGeneralRow(String[] cols) {
        GeneralFood generalFood;
        if (generalFoods.stream().anyMatch(g -> g.name.equals(cols[GEN_NAME]))) {
            generalFood = generalFoods.stream()
                .filter(g -> g.name.equals(cols[GEN_NAME])).findFirst().get();
        } else {
            generalFood = new GeneralFood(cols[GEN_NAME]);
        }

        String name = cols[GEN_DISPLAY_NAME];
        int fineliId = Integer.parseInt(cols[GEN_ID]);
        SpecificFood specificFood = new SpecificFood(name, fineliId);

        if (cols[GEN_SPECIFIC_TAGS] != null) {
            String[] tags = cols[GEN_SPECIFIC_TAGS].split(",");
            Arrays.setAll(tags, i -> tags[i].trim());
            Collections.addAll(specificFood.tags, tags);
        }
        if (cols[GEN_PIECE_WEIGHT] != null) {
            specificFood.pieceWeightGrams = Integer.parseInt(cols[GEN_PIECE_WEIGHT]);
        }
        if (cols[GEN_DENSITY_CONSTANT] != null) {
            specificFood.densityConstant = Double.parseDouble(cols[GEN_DENSITY_CONSTANT]);
        }
        if (cols[GEN_EXAMPLE_BRANDS] != null) {
            specificFood.exampleBrands = cols[GEN_EXAMPLE_BRANDS].trim();
        }
        if (cols[GEN_SCIENTIFIC_NAME] != null) {
            specificFood.scientificName = cols[GEN_SCIENTIFIC_NAME].trim();
        }
        if (cols[GEN_PROCESSING] != null) {
            specificFood.processing = getProcessing(cols[GEN_PROCESSING].trim());
        }
        if (cols[GEN_SPECIAL_DIETS] != null) {
            String[] diets = cols[GEN_SPECIAL_DIETS].split(",");
            for (String diet : diets) {
                specificFood.specialDiets.add(getSpecialDiet(diet.trim()));
            }
        }

        if (cols[GEN_DEFAULT] != null && cols[GEN_EXTRA_TAGS] != null) {
            String[] tags = cols[GEN_EXTRA_TAGS].split(",");
            for (String tag : tags) {
                generalFood.searchTags.add(tag.trim());
            }
            generalFood.defaultSpecificFood = specificFood;
        } else {
            generalFood.specificFoods.add(specificFood);
        }

        specificFood.generalFood = generalFood;
        generalFoods.add(generalFood);
        specificFoods.add(specificFood);
    }

	/*
    Helper methods
	 */

    private static Processing getProcessing(String str) {
        switch (str) {
            case "Torkat":
                return Processing.DRIED;
            case "Industriellt":
                return Processing.INDUSTRIAL;
            case "Obehandlat":
                return Processing.NO_TREATMENT;
            case "Konserverat":
                return Processing.CANNED;
            case "Mosat":
                return Processing.MASHED;
            case "Rökt":
                return Processing.SMOKED;
            case "Djupfryst":
                return Processing.FROZEN;
            case "Kokt":
                return Processing.BOILED;
            case "Ugnsstekt":
                return Processing.BAKED_IN_OVEN;
            case "Icke-definierat":
                return Processing.NOT_SPECIFIED;
            case "Blandat":
                return Processing.MIXED;
            case "Stekt":
                return Processing.FRIED;
            case "Grillat":
                return Processing.GRILLED_BROILED;
            case "Rostat":
                return Processing.ROASTED;
            case "Saltat":
                return Processing.SALTED;
            case "Syrat":
                return Processing.SOURED;
            default:
                System.out.println("No processing found: " + str);
                return null;
        }
    }

    private static SpecialDiet getSpecialDiet(String str) {
        switch (str) {
            case "CHOLFREE":
                return SpecialDiet.CHOLESTEROL_FREE;
            case "EGGFREE":
                return SpecialDiet.EGG_FREE;
            case "FATFREE":
                return SpecialDiet.FAT_FREE;
            case "GLUTFREE":
                return SpecialDiet.GLUTEN_FREE;
            case "HIGHFIBR":
                return SpecialDiet.HIGH_FIBRE;
            case "HIGHSALT":
                return SpecialDiet.STRONGLY_SALTED;
            case "LACOVEGE":
                return SpecialDiet.LACTO_OVO_VEG;
            case "LACSFREE":
                return SpecialDiet.LASTOSE_FREE;
            case "LACVEGE":
                return SpecialDiet.LACTO_VEG;
            case "LOWCHOL":
                return SpecialDiet.LOW_CHOLESTEROL;
            case "LOWFAT":
                return SpecialDiet.LOW_FAT;
            case "LOWLACS":
                return SpecialDiet.LOW_LACTOSE;
            case "LOWPROT":
                return SpecialDiet.LOW_PROTEIN;
            case "LOWSALT":
                return SpecialDiet.REDUCED_SALT;
            case "MILKFREE":
                return SpecialDiet.MILK_FREE;
            case "NAGLUFRE":
                return SpecialDiet.NATURALLY_GLUTEN_FREE;
            case "SALTFREE":
                return SpecialDiet.SALT_FREE;
            case "SOYAFREE":
                return SpecialDiet.SOY_FREE;
            case "UNSWEET":
                return SpecialDiet.UNSWEETENED;
            case "VEGAN":
                return SpecialDiet.VEGAN;
            case "VITAMADD":
                return SpecialDiet.ADDED_VITAMINS;
            default:
                return null;
        }
    }

    private static Float toFloat(String col) {
        if (!col.equals("NULL")) {
            return Float.parseFloat(col);
        } else {
            return null;
        }
    }

    private static Reader getReader(String path) {
        try {
            return new BufferedReader(new FileReader(path));
        } catch (FileNotFoundException e) {
            throw new IllegalStateException("Unable to read input", e);
        }
    }

    private static void printDone() {
        System.out.println(CommonTools.GREEN + "Done" + CommonTools.RESET);
    }

    private static class DatabaseNotEmptyException extends Exception {
    }
}
