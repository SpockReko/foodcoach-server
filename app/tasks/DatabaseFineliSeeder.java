package tasks;

import com.avaje.ebean.EbeanServer;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;
import models.food.fineli.*;
import models.recipe.Amount;
import models.recipe.Ingredient;
import models.recipe.Recipe;

import javax.persistence.PersistenceException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.*;

import static tasks.CommonTools.CYAN;
import static tasks.CommonTools.YELLOW;

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

    private static final String FINELI_GENERAL_TSV = "resources/fineli_food/Fineli_GeneralFoods.tsv";
    private static final String FINELI_DATA_TSV = "resources/fineli_food/Fineli_FoodData.tsv";
    private static final String MOCK_PATH = "resources/recipes/recipes_fineli.csv";

    private enum Fineli {
        GEN_ID(0), GEN_NAME(1), GEN_DEFAULT(2), GEN_SPECIFIC_TAGS(3), GEN_DISPLAY_NAME(4),
        GEN_EXTRA_TAGS(5), GEN_PIECE_WEIGHT(6), GEN_DENSITY_CONSTANT(7), GEN_EXAMPLE_BRANDS(8),
        GEN_SCIENTIFIC_NAME(9), GEN_PROCESSING(10), GEN_SPECIAL_DIETS(13),

        DATA_ID(0), DATA_ENERGY_KJ(2), DATA_CARB(3), DATA_PROTEIN(5), DATA_FAT(4), DATA_FIBRE(7),
        DATA_ALCOHOL(6), DATA_SALT(39), DATA_VITAMIN_A(52), DATA_VITAMIN_B6(47),
        DATA_VITAMIN_B12(50), DATA_VITAMIN_C(51), DATA_VITAMIN_D(54), DATA_VITAMIN_E(55),
        DATA_VITAMIN_K(56), DATA_THIAMINE(49), DATA_RIBOFLAVIN(48), DATA_NIACIN(46),
        DATA_NIACIN_EQ(45), DATA_FOLATE(44), DATA_PHOSPHORUS(40), DATA_IODINE(35), DATA_IRON(34),
        DATA_CALCIUM(33), DATA_POTASSIUM(36), DATA_MAGNESIUM(37), DATA_SODIUM(38),
        DATA_SELENIUM(41), DATA_ZINK(42);

        private final int row;
        
        Fineli(final int row) {
            this.row = row;
        }
    }

    private static Set<FoodGeneral> generalFoods = new HashSet<>();
    private static Map<FoodGeneral, Food> defaultFoods = new HashMap<>();
    private static List<Food> foods = new ArrayList<>();
    private static List<Diet> diets = new ArrayList<>();
    private static Map<Integer, Integer> idRowNumbers = new HashMap<>();

    public static void main(String[] args) {

        db = CommonTools.getDatabase();

        TsvParserSettings settings = new TsvParserSettings();
        settings.getFormat().setLineSeparator("\n");
        settings.setNumberOfRowsToSkip(1);

        TsvParser parser = new TsvParser(settings);
        List<String[]> generalRows = parser.parseAll(getReader(FINELI_GENERAL_TSV));
        List<String[]> specificRows = parser.parseAll(getReader(FINELI_DATA_TSV));

        System.out.println(
            "\n" + CommonTools.PURPLE + "--- (Seeding database) ---\n" + CommonTools.RESET);

        try {
            db.find(Food.class).where().eq("id", "1").findUnique();
        } catch (PersistenceException e) {
            System.out.println(YELLOW
                + "No database tables present. Please start server and run evolution script first!\n"
                + CommonTools.RESET);
            return;
        }

        for (int i = 0; i < specificRows.size(); i++) {
            idRowNumbers.put(Integer.parseInt(specificRows.get(i)[Fineli.DATA_ID.row]), i);
        }

        System.out.print(CYAN + "Importing foods from Fineli..." + CommonTools.RESET);
        importFoods(generalRows, specificRows);
        printDone();

        System.out.print(CYAN + "\nAdding mocked recipes... " + CommonTools.RESET);
        mockRecipes();
        printDone();

        System.out.println();
    }

    private static void importFoods(List<String[]> generalRows, List<String[]> specificRows) {
        for (String[] cols : generalRows) {
            readGeneralRow(cols, specificRows);
        }

        db.saveAll(generalFoods);
        db.saveAll(foods);

        for (FoodGeneral generalFood : generalFoods) {
            generalFood.defaultFood = defaultFoods.get(generalFood);
            db.save(generalFood);
        }
    }

    private static void importExtraFoods(List<String[]> generalRows, List<String[]> specificRows) {
        for (String[] cols : generalRows) {

        }
    }

    private static void readGeneralRow(String[] cols, List<String[]> specificRows) {
        FoodGeneral generalFood;
        if (generalFoods.stream().anyMatch(g -> g.name.equals(cols[Fineli.GEN_NAME.row]))) {
            generalFood = generalFoods.stream()
                .filter(g -> g.name.equals(cols[Fineli.GEN_NAME.row])).findFirst().get();
        } else {
            generalFood = new FoodGeneral(cols[Fineli.GEN_NAME.row]);
        }

        String name = cols[Fineli.GEN_DISPLAY_NAME.row];
        int fineliId = Integer.parseInt(cols[Fineli.GEN_ID.row]);
        String[] nutritionCols = specificRows.get(idRowNumbers.get(fineliId));
        Food specificFood = new Food(
            name, fineliId, DataSource.FINELI,
            toDouble(nutritionCols[Fineli.DATA_ENERGY_KJ.row]),
            toDouble(nutritionCols[Fineli.DATA_CARB.row]),
            toDouble(nutritionCols[Fineli.DATA_PROTEIN.row]),
            toDouble(nutritionCols[Fineli.DATA_FAT.row]),
            toDouble(nutritionCols[Fineli.DATA_FIBRE.row]),
            toDouble(nutritionCols[Fineli.DATA_ALCOHOL.row]),
            toDouble(nutritionCols[Fineli.DATA_SALT.row]),
            toDouble(nutritionCols[Fineli.DATA_VITAMIN_A.row]),
            toDouble(nutritionCols[Fineli.DATA_VITAMIN_B6.row]),
            toDouble(nutritionCols[Fineli.DATA_VITAMIN_B12.row]),
            toDouble(nutritionCols[Fineli.DATA_VITAMIN_C.row]),
            toDouble(nutritionCols[Fineli.DATA_VITAMIN_D.row]),
            toDouble(nutritionCols[Fineli.DATA_VITAMIN_E.row]),
            toDouble(nutritionCols[Fineli.DATA_VITAMIN_K.row]),
            toDouble(nutritionCols[Fineli.DATA_THIAMINE.row]),
            toDouble(nutritionCols[Fineli.DATA_RIBOFLAVIN.row]),
            toDouble(nutritionCols[Fineli.DATA_NIACIN.row]),
            toDouble(nutritionCols[Fineli.DATA_NIACIN_EQ.row]),
            toDouble(nutritionCols[Fineli.DATA_FOLATE.row]),
            toDouble(nutritionCols[Fineli.DATA_PHOSPHORUS.row]),
            toDouble(nutritionCols[Fineli.DATA_IODINE.row]),
            toDouble(nutritionCols[Fineli.DATA_IRON.row]),
            toDouble(nutritionCols[Fineli.DATA_CALCIUM.row]),
            toDouble(nutritionCols[Fineli.DATA_POTASSIUM.row]),
            toDouble(nutritionCols[Fineli.DATA_MAGNESIUM.row]),
            toDouble(nutritionCols[Fineli.DATA_SODIUM.row]),
            toDouble(nutritionCols[Fineli.DATA_SELENIUM.row]),
            toDouble(nutritionCols[Fineli.DATA_ZINK.row])
        );

        if (cols[Fineli.GEN_SPECIFIC_TAGS.row] != null) {
            String[] tags = cols[Fineli.GEN_SPECIFIC_TAGS.row].split(",");
            Arrays.setAll(tags, i -> tags[i].trim());
            Collections.addAll(specificFood.tags, tags);
        }
        if (cols[Fineli.GEN_PIECE_WEIGHT.row] != null) {
            specificFood.pieceWeightGrams = Integer.parseInt(cols[Fineli.GEN_PIECE_WEIGHT.row]);
        }
        if (cols[Fineli.GEN_DENSITY_CONSTANT.row] != null) {
            specificFood.densityConstant = Double.parseDouble(cols[Fineli.GEN_DENSITY_CONSTANT.row]);
        }
        if (cols[Fineli.GEN_EXAMPLE_BRANDS.row] != null) {
            specificFood.exampleBrands = cols[Fineli.GEN_EXAMPLE_BRANDS.row].trim();
        }
        if (cols[Fineli.GEN_SCIENTIFIC_NAME.row] != null) {
            specificFood.scientificName = cols[Fineli.GEN_SCIENTIFIC_NAME.row].trim();
        }
        if (cols[Fineli.GEN_PROCESSING.row] != null) {
            specificFood.processing = getProcessing(cols[Fineli.GEN_PROCESSING.row].trim());
        }
        if (cols[Fineli.GEN_SPECIAL_DIETS.row] != null) {
            String[] diets = cols[Fineli.GEN_SPECIAL_DIETS.row].split(",");
            for (String diet : diets) {
                specificFood.diets.add(getDiet(diet.trim()));
            }
        }

        if (cols[Fineli.GEN_DEFAULT.row] != null) {
            if (cols[Fineli.GEN_EXTRA_TAGS.row] != null) {
                String[] tags = cols[Fineli.GEN_EXTRA_TAGS.row].split(",");
                for (String tag : tags) {
                    generalFood.searchTags.add(tag.trim());
                }
            }
            defaultFoods.put(generalFood, specificFood);
        } else {
            generalFood.foods.add(specificFood);
        }

        specificFood.general = generalFood;
        generalFoods.add(generalFood);
        foods.add(specificFood);
    }

    private static void mockRecipes() {
        CsvParserSettings settings = new CsvParserSettings();
        settings.getFormat().setLineSeparator("\n");
        settings.setNumberOfRowsToSkip(1);

        CsvParser parser = new CsvParser(settings);
        List<String[]> allRows = parser.parseAll(getReader(MOCK_PATH));

        for (String[] row : allRows) {

            String title = row[0];
            int portions = Integer.parseInt(row[1]);
            List<Ingredient> ingredients = new LinkedList<>();
            String[] ingredientsCsv = row[2].split(";");

            for (String ingCsv : ingredientsCsv) {

                String[] part = ingCsv.split("_");
                long fineliId = Long.parseLong(part[0]);
                double amount = Double.parseDouble(part[1]);
                Amount.Unit unit = null;
                for (Amount.Unit u : Amount.Unit.values()) {
                    if (u.name().toLowerCase().equals(part[2])) {
                        unit = u;
                    }
                }

                if (unit == null) {
                    throw new RuntimeException("Wrong unit in recipes.csv");
                }

                Food food = db.find(Food.class).where().eq("dataSourceId", fineliId).findUnique();

                if (food == null) {
                    throw new RuntimeException("No food with food number " + fineliId);
                }

                ingredients.add(new Ingredient(food, new Amount(amount, unit)));
            }

            Recipe recipe = new Recipe(title, portions, ingredients);
            if (db.find(Recipe.class).where().eq("title", title).findCount() == 0) {
                db.save(recipe);
            } else {
                System.out.println("\n" + YELLOW + title + " already exists!");
            }
        }
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

    private static Diet getDiet(String str) {
        switch (str) {
            case "CHOLFREE":
                return addOrGetDiet(Diet.Type.CHOLESTEROL_FREE);
            case "EGGFREE":
                return addOrGetDiet(Diet.Type.EGG_FREE);
            case "FATFREE":
                return addOrGetDiet(Diet.Type.FAT_FREE);
            case "GLUTFREE":
                return addOrGetDiet(Diet.Type.GLUTEN_FREE);
            case "HIGHFIBR":
                return addOrGetDiet(Diet.Type.HIGH_FIBRE);
            case "HIGHSALT":
                return addOrGetDiet(Diet.Type.STRONGLY_SALTED);
            case "LACOVEGE":
                return addOrGetDiet(Diet.Type.LACTO_OVO_VEG);
            case "LACSFREE":
                return addOrGetDiet(Diet.Type.LACTOSE_FREE);
            case "LACVEGE":
                return addOrGetDiet(Diet.Type.LACTO_VEG);
            case "LOWCHOL":
                return addOrGetDiet(Diet.Type.LOW_CHOLESTEROL);
            case "LOWFAT":
                return addOrGetDiet(Diet.Type.LOW_FAT);
            case "LOWLACS":
                return addOrGetDiet(Diet.Type.LOW_LACTOSE);
            case "LOWPROT":
                return addOrGetDiet(Diet.Type.LOW_PROTEIN);
            case "LOWSALT":
                return addOrGetDiet(Diet.Type.REDUCED_SALT);
            case "MILKFREE":
                return addOrGetDiet(Diet.Type.MILK_FREE);
            case "NAGLUFRE":
                return addOrGetDiet(Diet.Type.NATURALLY_GLUTEN_FREE);
            case "SALTFREE":
                return addOrGetDiet(Diet.Type.SALT_FREE);
            case "SOYAFREE":
                return addOrGetDiet(Diet.Type.SOY_FREE);
            case "UNSWEET":
                return addOrGetDiet(Diet.Type.UNSWEETENED);
            case "VEGAN":
                return addOrGetDiet(Diet.Type.VEGAN);
            case "VITAMADD":
                return addOrGetDiet(Diet.Type.ADDED_VITAMINS);
            default:
                return null;
        }
    }

    private static Diet addOrGetDiet(Diet.Type type) {
        Optional<Diet> opt = diets.stream().filter(d -> d.type == type).findFirst();
        if (opt.isPresent()) {
            return opt.get();
        } else {
            Diet diet = new Diet(type);
            diets.add(diet);
            return diet;
        }
    }

    private static Double toDouble(String col) {
        if (col.equals("<0.1")) {
            return 0.09;
        } else if (col.equals("<0.01")) {
            return 0.009;
        } else if (col != null && !col.equals("N/A")) {
            return Double.parseDouble(col);
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
}
