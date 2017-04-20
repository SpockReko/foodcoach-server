package tasks;

import com.avaje.ebean.EbeanServer;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;
import models.food.*;
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
public class DatabaseSeeder {

    private static EbeanServer db;

    private static final String FINELI_GENERAL_TSV = "resources/fineli_food/Fineli_GeneralFoods.tsv";
    private static final String FINELI_DATA_TSV = "resources/fineli_food/Fineli_FoodData.tsv";
    private static final String LMV_GENERAL_TSV = "resources/lmv_food/LMV_GeneralFoods.tsv";
    private static final String LMV_DATA_TSV = "resources/lmv_food/LMV_FoodData.tsv";
    private static final String RECIPES_PATH = "resources/recipes/recipes_fineli.csv";

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

        private final int id;

        Fineli(final int id) {
            this.id = id;
        }
    }

    private enum LMV {
        GEN_ID(0), GEN_NAME(1), GEN_DEFAULT(2), GEN_SPECIFIC_TAGS(3), GEN_DISPLAY_NAME(4),
        GEN_EXTRA_TAGS(5), GEN_PIECE_WEIGHT(6), GEN_DENSITY_CONSTANT(7), GEN_EXAMPLE_BRANDS(8),
        GEN_SCIENTIFIC_NAME(9),

        DATA_ID(1), DATA_ENERGY_KJ(3), DATA_CARB(4), DATA_PROTEIN(6), DATA_FAT(5), DATA_FIBRE(7),
        DATA_ALCOHOL(9), DATA_SALT(55), DATA_VITAMIN_A(35), DATA_VITAMIN_B6(45),
        DATA_VITAMIN_B12(46), DATA_VITAMIN_C(42), DATA_VITAMIN_D(37), DATA_VITAMIN_E(38),
        DATA_VITAMIN_K(39), DATA_THIAMINE(40), DATA_RIBOFLAVIN(41), DATA_NIACIN(43),
        DATA_NIACIN_EQ(44), DATA_FOLATE(47), DATA_PHOSPHORUS(48), DATA_IODINE(49), DATA_IRON(50),
        DATA_CALCIUM(51), DATA_POTASSIUM(52), DATA_MAGNESIUM(53), DATA_SODIUM(54),
        DATA_SELENIUM(56), DATA_ZINK(57);

        private final int id;

        LMV(final int id) {
            this.id = id;
        }
    }

    private static Set<FoodGeneral> generalFoods = new HashSet<>();
    private static Map<FoodGeneral, Food> defaultFoods = new HashMap<>();
    private static List<Food> foods = new ArrayList<>();
    private static List<Diet> diets = new ArrayList<>();
    private static Map<Integer, Integer> fineliRowIds = new HashMap<>();
    private static Map<Integer, Integer> lmvRowIds = new HashMap<>();

    public static void main(String[] args) {

        db = CommonTools.getDatabase();
        TsvParserSettings settings = new TsvParserSettings();
        settings.getFormat().setLineSeparator("\n");
        settings.setNumberOfRowsToSkip(1);
        TsvParser parser = new TsvParser(settings);

        List<String[]> fineliGeneralRows = parser.parseAll(getReader(FINELI_GENERAL_TSV));
        List<String[]> fineliDataRows = parser.parseAll(getReader(FINELI_DATA_TSV));

        List<String[]> lmvGeneralRows = parser.parseAll(getReader(LMV_GENERAL_TSV));
        List<String[]> lmvDataRows = parser.parseAll(getReader(LMV_DATA_TSV));

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

        for (int i = 0; i < fineliDataRows.size(); i++) {
            fineliRowIds.put(Integer.parseInt(fineliDataRows.get(i)[Fineli.DATA_ID.id]), i);
        }
        for (int i = 0; i < lmvDataRows.size(); i++) {
            lmvRowIds.put(Integer.parseInt(lmvDataRows.get(i)[LMV.DATA_ID.id]), i);
        }

        System.out.print(CYAN + "Parsing foods from Fineli... " + CommonTools.RESET);
        for (String[] cols : fineliGeneralRows) {
            readGeneralRow(cols, fineliDataRows);
        }
        printDone();

        System.out.print(CYAN + "Parsing extra foods from Livsmedelsverket... " + CommonTools.RESET);
        for (String[] cols : lmvGeneralRows) {
            readExtraGeneralRow(cols, lmvDataRows);
        }
        printDone();

        System.out.print(CYAN + "Persisting to database... " + CommonTools.RESET);
        db.saveAll(generalFoods);
        db.saveAll(foods);
        for (FoodGeneral generalFood : generalFoods) {
            generalFood.defaultFood = defaultFoods.get(generalFood);
            db.save(generalFood);
        }
        printDone();

        System.out.print(CYAN + "Adding mocked recipes... " + CommonTools.RESET);
        mockRecipes();
        printDone();

        System.out.println();
    }

    private static void readGeneralRow(String[] cols, List<String[]> dataRows) {
        FoodGeneral generalFood;
        if (generalFoods.stream().anyMatch(g -> g.name.equals(cols[Fineli.GEN_NAME.id]))) {
            generalFood = generalFoods.stream()
                .filter(g -> g.name.equals(cols[Fineli.GEN_NAME.id])).findFirst().get();
        } else {
            generalFood = new FoodGeneral(cols[Fineli.GEN_NAME.id]);
        }

        String name = cols[Fineli.GEN_DISPLAY_NAME.id];
        int fineliId = Integer.parseInt(cols[Fineli.GEN_ID.id]);
        String[] rows = dataRows.get(fineliRowIds.get(fineliId));
        Food specificFood = new Food(
            name, fineliId, DataSource.FINELI,
            toDouble(rows[Fineli.DATA_ENERGY_KJ.id]),
            toDouble(rows[Fineli.DATA_CARB.id]),
            toDouble(rows[Fineli.DATA_PROTEIN.id]),
            toDouble(rows[Fineli.DATA_FAT.id]),
            toDouble(rows[Fineli.DATA_FIBRE.id]),
            toDouble(rows[Fineli.DATA_ALCOHOL.id]),
            toDouble(rows[Fineli.DATA_SALT.id]),
            toDouble(rows[Fineli.DATA_VITAMIN_A.id]),
            toDouble(rows[Fineli.DATA_VITAMIN_B6.id]),
            toDouble(rows[Fineli.DATA_VITAMIN_B12.id]),
            toDouble(rows[Fineli.DATA_VITAMIN_C.id]),
            toDouble(rows[Fineli.DATA_VITAMIN_D.id]),
            toDouble(rows[Fineli.DATA_VITAMIN_E.id]),
            toDouble(rows[Fineli.DATA_VITAMIN_K.id]),
            toDouble(rows[Fineli.DATA_THIAMINE.id]),
            toDouble(rows[Fineli.DATA_RIBOFLAVIN.id]),
            toDouble(rows[Fineli.DATA_NIACIN.id]),
            toDouble(rows[Fineli.DATA_NIACIN_EQ.id]),
            toDouble(rows[Fineli.DATA_FOLATE.id]),
            toDouble(rows[Fineli.DATA_PHOSPHORUS.id]),
            toDouble(rows[Fineli.DATA_IODINE.id]),
            toDouble(rows[Fineli.DATA_IRON.id]),
            toDouble(rows[Fineli.DATA_CALCIUM.id]),
            toDouble(rows[Fineli.DATA_POTASSIUM.id]),
            toDouble(rows[Fineli.DATA_MAGNESIUM.id]),
            toDouble(rows[Fineli.DATA_SODIUM.id]),
            toDouble(rows[Fineli.DATA_SELENIUM.id]),
            toDouble(rows[Fineli.DATA_ZINK.id])
        );

        if (cols[Fineli.GEN_SPECIFIC_TAGS.id] != null) {
            String[] tags = cols[Fineli.GEN_SPECIFIC_TAGS.id].split(",");
            Arrays.setAll(tags, i -> tags[i].trim());
            Collections.addAll(specificFood.tags, tags);
        }
        if (cols[Fineli.GEN_PIECE_WEIGHT.id] != null) {
            specificFood.pieceWeightGrams = Integer.parseInt(cols[Fineli.GEN_PIECE_WEIGHT.id]);
        }
        if (cols[Fineli.GEN_DENSITY_CONSTANT.id] != null) {
            specificFood.densityConstant = Double.parseDouble(cols[Fineli.GEN_DENSITY_CONSTANT.id]);
        }
        if (cols[Fineli.GEN_EXAMPLE_BRANDS.id] != null) {
            specificFood.exampleBrands = cols[Fineli.GEN_EXAMPLE_BRANDS.id].trim();
        }
        if (cols[Fineli.GEN_SCIENTIFIC_NAME.id] != null) {
            specificFood.scientificName = cols[Fineli.GEN_SCIENTIFIC_NAME.id].trim();
        }
        if (cols[Fineli.GEN_PROCESSING.id] != null) {
            specificFood.processing = getProcessing(cols[Fineli.GEN_PROCESSING.id].trim());
        }
        if (cols[Fineli.GEN_SPECIAL_DIETS.id] != null) {
            String[] diets = cols[Fineli.GEN_SPECIAL_DIETS.id].split(",");
            for (String diet : diets) {
                specificFood.diets.add(getDiet(diet.trim()));
            }
        }

        if (cols[Fineli.GEN_DEFAULT.id] != null) {
            if (cols[Fineli.GEN_EXTRA_TAGS.id] != null) {
                String[] tags = cols[Fineli.GEN_EXTRA_TAGS.id].split(",");
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

    private static void readExtraGeneralRow(String[] cols, List<String[]> dataRows) {
        FoodGeneral generalFood;
        if (generalFoods.stream().anyMatch(g -> g.name.equals(cols[LMV.GEN_NAME.id]))) {
            generalFood = generalFoods.stream()
                .filter(g -> g.name.equals(cols[LMV.GEN_NAME.id])).findFirst().get();
        } else {
            generalFood = new FoodGeneral(cols[LMV.GEN_NAME.id]);
        }

        String name = cols[LMV.GEN_DISPLAY_NAME.id];
        int lmvId = Integer.parseInt(cols[LMV.GEN_ID.id]);
        String[] rows = dataRows.get(lmvRowIds.get(lmvId));
        Food specificFood = new Food(
            name, lmvId, DataSource.LMV,
            toDouble(rows[LMV.DATA_ENERGY_KJ.id]),
            toDouble(rows[LMV.DATA_CARB.id]),
            toDouble(rows[LMV.DATA_PROTEIN.id]),
            toDouble(rows[LMV.DATA_FAT.id]),
            toDouble(rows[LMV.DATA_FIBRE.id]),
            toDouble(rows[LMV.DATA_ALCOHOL.id]),
            toDouble(rows[LMV.DATA_SALT.id]),
            toDouble(rows[LMV.DATA_VITAMIN_A.id]),
            toDouble(rows[LMV.DATA_VITAMIN_B6.id]),
            toDouble(rows[LMV.DATA_VITAMIN_B12.id]),
            toDouble(rows[LMV.DATA_VITAMIN_C.id]),
            toDouble(rows[LMV.DATA_VITAMIN_D.id]),
            toDouble(rows[LMV.DATA_VITAMIN_E.id]),
            toDouble(rows[LMV.DATA_VITAMIN_K.id]),
            toDouble(rows[LMV.DATA_THIAMINE.id]),
            toDouble(rows[LMV.DATA_RIBOFLAVIN.id]),
            toDouble(rows[LMV.DATA_NIACIN.id]),
            toDouble(rows[LMV.DATA_NIACIN_EQ.id]),
            toDouble(rows[LMV.DATA_FOLATE.id]),
            toDouble(rows[LMV.DATA_PHOSPHORUS.id]),
            toDouble(rows[LMV.DATA_IODINE.id]),
            toDouble(rows[LMV.DATA_IRON.id]),
            toDouble(rows[LMV.DATA_CALCIUM.id]),
            toDouble(rows[LMV.DATA_POTASSIUM.id]),
            toDouble(rows[LMV.DATA_MAGNESIUM.id]),
            toDouble(rows[LMV.DATA_SODIUM.id]),
            toDouble(rows[LMV.DATA_SELENIUM.id]),
            toDouble(rows[LMV.DATA_ZINK.id])
        );

        if (cols[LMV.GEN_SPECIFIC_TAGS.id] != null) {
            String[] tags = cols[LMV.GEN_SPECIFIC_TAGS.id].split(",");
            Arrays.setAll(tags, i -> tags[i].trim());
            Collections.addAll(specificFood.tags, tags);
        }
        if (cols[LMV.GEN_PIECE_WEIGHT.id] != null) {
            specificFood.pieceWeightGrams = Integer.parseInt(cols[LMV.GEN_PIECE_WEIGHT.id]);
        }
        if (cols[LMV.GEN_DENSITY_CONSTANT.id] != null) {
            specificFood.densityConstant = Double.parseDouble(cols[LMV.GEN_DENSITY_CONSTANT.id]);
        }
        if (cols[LMV.GEN_EXAMPLE_BRANDS.id] != null) {
            specificFood.exampleBrands = cols[LMV.GEN_EXAMPLE_BRANDS.id].trim();
        }
        if (cols[LMV.GEN_SCIENTIFIC_NAME.id] != null) {
            specificFood.scientificName = cols[LMV.GEN_SCIENTIFIC_NAME.id].trim();
        }

        if (cols[LMV.GEN_DEFAULT.id] != null) {
            if (cols[LMV.GEN_EXTRA_TAGS.id] != null) {
                String[] tags = cols[LMV.GEN_EXTRA_TAGS.id].split(",");
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
        List<String[]> allRows = parser.parseAll(getReader(RECIPES_PATH));

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
        } else if (col != null && !col.equals("N/A") && !col.equals("NULL")) {
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
