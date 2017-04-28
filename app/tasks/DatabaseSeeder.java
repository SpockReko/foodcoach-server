package tasks;

import com.avaje.ebean.EbeanServer;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;

import static helpers.StringHelper.CYAN;
import static helpers.StringHelper.GREEN;
import static helpers.StringHelper.PURPLE;
import static helpers.StringHelper.YELLOW;
import static helpers.StringHelper.RESET;

import helpers.Constants;
import models.food.*;
import models.recipe.Amount;
import models.recipe.Ingredient;
import models.recipe.Recipe;

import javax.persistence.PersistenceException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.*;

/**
 * Parses through the provided TSV files containing food groups, foods and their basic data.
 * Sets the correct values to the Java objects and persists them to the database
 * using the Ebean ORM included with the application.
 *
 * Can be executed with the command "sbt seed" to fill an empty database with data.
 *
 * @author Fredrik Kindstrom
 */
public class DatabaseSeeder {

    private static EbeanServer db;

    private static final String FINELI_GROUP_TSV = "resources/food/Fineli_FoodGroups.tsv";
    private static final String FINELI_DATA_TSV = "resources/food/Fineli_FoodData.tsv";
    private static final String SLV_GROUP_TSV = "resources/food/SLV_FoodGroups.tsv";
    private static final String SLV_DATA_TSV = "resources/food/SLV_FoodData.tsv";
    private static final String NSDA_GROUP_TSV = "resources/food/NSDA_FoodGroups.tsv";
    private static final String NSDA_DATA_TSV = "resources/food/NSDA_FoodData.tsv";
    private static final String RECIPES_PATH = "resources/recipes/recipes_fineli.csv";

    private enum Fineli {
        GROUP_ID(0), GROUP_NAME(1), GROUP_DEFAULT(2), GROUP_SPECIFIC_TAGS(3), GROUP_DISPLAY_NAME(4),
        GROUP_EXTRA_TAGS(5), GROUP_PIECE_WEIGHT(6), GROUP_DENSITY_CONSTANT(7), GROUP_EXAMPLE_BRANDS(8),
        GROUP_SCIENTIFIC_NAME(9), GROUP_PROCESSING(10), GROUP_CLASSIFICATION(11), GROUP_SPECIAL_DIETS(13),
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

    private enum SLV {
        GROUP_ID(0), GROUP_NAME(1), GROUP_DEFAULT(2), GROUP_SPECIFIC_TAGS(3), GROUP_DISPLAY_NAME(4),
        GROUP_EXTRA_TAGS(5), GROUP_PIECE_WEIGHT(6), GROUP_DENSITY_CONSTANT(7), GROUP_EXAMPLE_BRANDS(8),
        GROUP_SCIENTIFIC_NAME(9), GROUP_CLASSIFICATION(10),
        DATA_ID(1), DATA_ENERGY_KJ(3), DATA_CARB(4), DATA_PROTEIN(6), DATA_FAT(5), DATA_FIBRE(7),
        DATA_ALCOHOL(9), DATA_SALT(55), DATA_VITAMIN_A(35), DATA_VITAMIN_B6(45),
        DATA_VITAMIN_B12(46), DATA_VITAMIN_C(42), DATA_VITAMIN_D(37), DATA_VITAMIN_E(38),
        DATA_VITAMIN_K(39), DATA_THIAMINE(40), DATA_RIBOFLAVIN(41), DATA_NIACIN(43),
        DATA_NIACIN_EQ(44), DATA_FOLATE(47), DATA_PHOSPHORUS(48), DATA_IODINE(49), DATA_IRON(50),
        DATA_CALCIUM(51), DATA_POTASSIUM(52), DATA_MAGNESIUM(53), DATA_SODIUM(54),
        DATA_SELENIUM(56), DATA_ZINK(57);

        private final int id;

        SLV(final int id) {
            this.id = id;
        }
    }

    private enum NSDA {
        GROUP_ID(0), GROUP_NAME(1), GROUP_DEFAULT(2), GROUP_SPECIFIC_TAGS(3), GROUP_DISPLAY_NAME(4),
        GROUP_EXTRA_TAGS(5), GROUP_PIECE_WEIGHT(6), GROUP_DENSITY_CONSTANT(7),
        GROUP_EXAMPLE_BRANDS(8), GROUP_SCIENTIFIC_NAME(9), GROUP_CLASSIFICATION(10), DATA_ID(0),
        DATA_ENERGY_KCAL(3), DATA_CARB(7), DATA_PROTEIN(4), DATA_FAT(5), DATA_FIBRE(8),
        DATA_VITAMIN_A(33), DATA_VITAMIN_B6(25), DATA_VITAMIN_B12(31), DATA_VITAMIN_C(20),
        DATA_VITAMIN_D(41), DATA_VITAMIN_E(40), DATA_VITAMIN_K(43), DATA_THIAMINE(21),
        DATA_RIBOFLAVIN(22), DATA_NIACIN(23), DATA_FOLATE(26), DATA_PHOSPHORUS(13), DATA_IRON(11),
        DATA_CALCIUM(10), DATA_POTASSIUM(14), DATA_MAGNESIUM(12), DATA_SODIUM(15),
        DATA_SELENIUM(19), DATA_ZINK(16);

        private final int id;

        NSDA(final int id) {
            this.id = id;
        }
    }

    private static Set<FoodGroup> foodGroups = new HashSet<>();
    private static Map<FoodGroup, Food> defaultFoods = new HashMap<>();
    private static List<Food> foods = new ArrayList<>();
    private static List<Diet> diets = new ArrayList<>();
    private static Map<Integer, Integer> fineliRowIds = new HashMap<>();
    private static Map<Integer, Integer> lmvRowIds = new HashMap<>();
    private static Map<Integer, Integer> nsdaRowIds = new HashMap<>();

    public static void main(String[] args) {

        db = CommonTools.getDatabase();
        TsvParserSettings settings = new TsvParserSettings();
        settings.getFormat().setLineSeparator("\n");
        settings.setNumberOfRowsToSkip(1);
        TsvParser parser = new TsvParser(settings);

        List<String[]> fineliGroupRows = parser.parseAll(getReader(FINELI_GROUP_TSV));
        List<String[]> fineliDataRows = parser.parseAll(getReader(FINELI_DATA_TSV));
        List<String[]> lmvGroupRows = parser.parseAll(getReader(SLV_GROUP_TSV));
        List<String[]> lmvDataRows = parser.parseAll(getReader(SLV_DATA_TSV));
        List<String[]> nsdaGroupRows = parser.parseAll(getReader(NSDA_GROUP_TSV));
        List<String[]> nsdaDataRows = parser.parseAll(getReader(NSDA_DATA_TSV));

        System.out.println(
            "\n" + PURPLE + "--- (Seeding database) ---\n" + RESET);

        try {
            db.find(Food.class).where().eq("id", "1").findUnique();
        } catch (PersistenceException e) {
            System.out.println(YELLOW
                + "No database tables present. Please start server and run evolution script first!\n"
                + RESET);
            return;
        }

        for (int i = 0; i < fineliDataRows.size(); i++) {
            fineliRowIds.put(Integer.parseInt(fineliDataRows.get(i)[Fineli.DATA_ID.id]), i);
        }
        for (int i = 0; i < lmvDataRows.size(); i++) {
            lmvRowIds.put(Integer.parseInt(lmvDataRows.get(i)[SLV.DATA_ID.id]), i);
        }
        for (int i = 0; i < nsdaDataRows.size(); i++) {
            nsdaRowIds.put(Integer.parseInt(nsdaDataRows.get(i)[NSDA.DATA_ID.id]), i);
        }

        System.out.print(CYAN + "Parsing foods from Fineli... " + RESET);
        for (String[] cols : fineliGroupRows) {
            readFineliGroupRow(cols, fineliDataRows);
        }
        printDone();

        System.out.print(CYAN + "Parsing extra foods from Livsmedelsverket... " + RESET);
        for (String[] cols : lmvGroupRows) {
            readSlvGroupRow(cols, lmvDataRows);
        }
        printDone();

        System.out.print(CYAN + "Parsing extra foods from NSDA... " + RESET);
        for (String[] cols : nsdaGroupRows) {
            readNsdaGroupRow(cols, nsdaDataRows);
        }
        printDone();

        System.out.print(CYAN + "Persisting to database... " + RESET);
        db.saveAll(foodGroups);
        db.saveAll(foods);
        for (FoodGroup foodGroup : foodGroups) {
            foodGroup.defaultFood = defaultFoods.get(foodGroup);
            db.save(foodGroup);
        }
        printDone();

        System.out.print(CYAN + "Adding mocked recipes... " + RESET);
        mockRecipes();
        printDone();

        System.out.println();
    }

    private static void readFineliGroupRow(String[] cols, List<String[]> dataRows) {
        FoodGroup foodGroup;
        if (foodGroups.stream().anyMatch(g -> g.name.equals(cols[Fineli.GROUP_NAME.id]))) {
            foodGroup = foodGroups.stream()
                .filter(g -> g.name.equals(cols[Fineli.GROUP_NAME.id])).findFirst().get();
        } else {
            foodGroup = new FoodGroup(cols[Fineli.GROUP_NAME.id]);
        }

        String name = cols[Fineli.GROUP_DISPLAY_NAME.id];
        int fineliId = Integer.parseInt(cols[Fineli.GROUP_ID.id]);
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

        if (cols[Fineli.GROUP_SPECIFIC_TAGS.id] != null) {
            String[] tags = cols[Fineli.GROUP_SPECIFIC_TAGS.id].split(",");
            Arrays.setAll(tags, i -> tags[i].trim());
            Collections.addAll(specificFood.tags, tags);
        }
        if (cols[Fineli.GROUP_PIECE_WEIGHT.id] != null) {
            specificFood.pieceWeightGrams = Integer.parseInt(cols[Fineli.GROUP_PIECE_WEIGHT.id]);
        }
        if (cols[Fineli.GROUP_DENSITY_CONSTANT.id] != null) {
            specificFood.densityConstant = Double.parseDouble(cols[Fineli.GROUP_DENSITY_CONSTANT.id]);
        }
        if (cols[Fineli.GROUP_EXAMPLE_BRANDS.id] != null) {
            specificFood.exampleBrands = cols[Fineli.GROUP_EXAMPLE_BRANDS.id].trim();
        }
        if (cols[Fineli.GROUP_SCIENTIFIC_NAME.id] != null) {
            specificFood.scientificName = cols[Fineli.GROUP_SCIENTIFIC_NAME.id].trim();
        }
        if (cols[Fineli.GROUP_PROCESSING.id] != null) {
            specificFood.processing = getProcessing(cols[Fineli.GROUP_PROCESSING.id].trim());
        }
        if (cols[Fineli.GROUP_CLASSIFICATION.id] != null) {
            specificFood.category = getCategory(cols[Fineli.GROUP_CLASSIFICATION.id].trim());
        }
        if (cols[Fineli.GROUP_SPECIAL_DIETS.id] != null) {
            String[] diets = cols[Fineli.GROUP_SPECIAL_DIETS.id].split(",");
            for (String diet : diets) {
                specificFood.diets.add(getDiet(diet.trim()));
            }
        }

        if (cols[Fineli.GROUP_DEFAULT.id] != null) {
            if (cols[Fineli.GROUP_EXTRA_TAGS.id] != null) {
                String[] tags = cols[Fineli.GROUP_EXTRA_TAGS.id].split(",");
                for (String tag : tags) {
                    foodGroup.searchTags.add(tag.trim());
                }
            }
            defaultFoods.put(foodGroup, specificFood);
        } else {
            foodGroup.foods.add(specificFood);
        }

        specificFood.group = foodGroup;
        foodGroups.add(foodGroup);
        foods.add(specificFood);
    }

    private static void readSlvGroupRow(String[] cols, List<String[]> dataRows) {
        FoodGroup foodGroup;
        if (foodGroups.stream().anyMatch(g -> g.name.equals(cols[SLV.GROUP_NAME.id]))) {
            foodGroup = foodGroups.stream()
                .filter(g -> g.name.equals(cols[SLV.GROUP_NAME.id])).findFirst().get();
        } else {
            foodGroup = new FoodGroup(cols[SLV.GROUP_NAME.id]);
        }

        String name = cols[SLV.GROUP_DISPLAY_NAME.id];
        int lmvId = Integer.parseInt(cols[SLV.GROUP_ID.id]);
        String[] rows = dataRows.get(lmvRowIds.get(lmvId));
        Food specificFood = new Food(
            name, lmvId, DataSource.SLV,
            toDouble(rows[SLV.DATA_ENERGY_KJ.id]),
            toDouble(rows[SLV.DATA_CARB.id]),
            toDouble(rows[SLV.DATA_PROTEIN.id]),
            toDouble(rows[SLV.DATA_FAT.id]),
            toDouble(rows[SLV.DATA_FIBRE.id]),
            toDouble(rows[SLV.DATA_ALCOHOL.id]),
            toDouble(rows[SLV.DATA_SALT.id]),
            toDouble(rows[SLV.DATA_VITAMIN_A.id]),
            toDouble(rows[SLV.DATA_VITAMIN_B6.id]),
            toDouble(rows[SLV.DATA_VITAMIN_B12.id]),
            toDouble(rows[SLV.DATA_VITAMIN_C.id]),
            toDouble(rows[SLV.DATA_VITAMIN_D.id]),
            toDouble(rows[SLV.DATA_VITAMIN_E.id]),
            toDouble(rows[SLV.DATA_VITAMIN_K.id]),
            toDouble(rows[SLV.DATA_THIAMINE.id]),
            toDouble(rows[SLV.DATA_RIBOFLAVIN.id]),
            toDouble(rows[SLV.DATA_NIACIN.id]),
            toDouble(rows[SLV.DATA_NIACIN_EQ.id]),
            toDouble(rows[SLV.DATA_FOLATE.id]),
            toDouble(rows[SLV.DATA_PHOSPHORUS.id]),
            toDouble(rows[SLV.DATA_IODINE.id]),
            toDouble(rows[SLV.DATA_IRON.id]),
            toDouble(rows[SLV.DATA_CALCIUM.id]),
            toDouble(rows[SLV.DATA_POTASSIUM.id]),
            toDouble(rows[SLV.DATA_MAGNESIUM.id]),
            toDouble(rows[SLV.DATA_SODIUM.id]),
            toDouble(rows[SLV.DATA_SELENIUM.id]),
            toDouble(rows[SLV.DATA_ZINK.id])
        );

        if (cols[SLV.GROUP_SPECIFIC_TAGS.id] != null) {
            String[] tags = cols[SLV.GROUP_SPECIFIC_TAGS.id].split(",");
            Arrays.setAll(tags, i -> tags[i].trim());
            Collections.addAll(specificFood.tags, tags);
        }
        if (cols[SLV.GROUP_PIECE_WEIGHT.id] != null) {
            specificFood.pieceWeightGrams = Integer.parseInt(cols[SLV.GROUP_PIECE_WEIGHT.id]);
        }
        if (cols[SLV.GROUP_DENSITY_CONSTANT.id] != null) {
            specificFood.densityConstant = Double.parseDouble(cols[SLV.GROUP_DENSITY_CONSTANT.id]);
        }
        if (cols[SLV.GROUP_EXAMPLE_BRANDS.id] != null) {
            specificFood.exampleBrands = cols[SLV.GROUP_EXAMPLE_BRANDS.id].trim();
        }
        if (cols[SLV.GROUP_SCIENTIFIC_NAME.id] != null) {
            specificFood.scientificName = cols[SLV.GROUP_SCIENTIFIC_NAME.id].trim();
        }
        if (cols[SLV.GROUP_CLASSIFICATION.id] != null) {
            specificFood.category = getCategory(cols[SLV.GROUP_CLASSIFICATION.id].trim());
        }

        if (cols[SLV.GROUP_DEFAULT.id] != null) {
            if (cols[SLV.GROUP_EXTRA_TAGS.id] != null) {
                String[] tags = cols[SLV.GROUP_EXTRA_TAGS.id].split(",");
                for (String tag : tags) {
                    foodGroup.searchTags.add(tag.trim());
                }
            }
            defaultFoods.put(foodGroup, specificFood);
        } else {
            foodGroup.foods.add(specificFood);
        }

        specificFood.group = foodGroup;
        foodGroups.add(foodGroup);
        foods.add(specificFood);
    }

    private static void readNsdaGroupRow(String[] cols, List<String[]> dataRows) {
        FoodGroup foodGroup;
        if (foodGroups.stream().anyMatch(g -> g.name.equals(cols[NSDA.GROUP_NAME.id]))) {
            foodGroup = foodGroups.stream()
                .filter(g -> g.name.equals(cols[NSDA.GROUP_NAME.id])).findFirst().get();
        } else {
            foodGroup = new FoodGroup(cols[NSDA.GROUP_NAME.id]);
        }

        String name = cols[NSDA.GROUP_DISPLAY_NAME.id];
        int nsdaId = Integer.parseInt(cols[NSDA.GROUP_ID.id]);
        String[] rows = dataRows.get(nsdaRowIds.get(nsdaId));
        Food specificFood = new Food(
            name, nsdaId, DataSource.NSDA,
            toDouble(rows[NSDA.DATA_ENERGY_KCAL.id]) * Constants.KCAL_FACTOR,
            toDouble(rows[NSDA.DATA_CARB.id]),
            toDouble(rows[NSDA.DATA_PROTEIN.id]),
            toDouble(rows[NSDA.DATA_FAT.id]),
            toDouble(rows[NSDA.DATA_FIBRE.id]),
            null,
            null,
            toDouble(rows[NSDA.DATA_VITAMIN_A.id]),
            toDouble(rows[NSDA.DATA_VITAMIN_B6.id]),
            toDouble(rows[NSDA.DATA_VITAMIN_B12.id]),
            toDouble(rows[NSDA.DATA_VITAMIN_C.id]),
            toDouble(rows[NSDA.DATA_VITAMIN_D.id]),
            toDouble(rows[NSDA.DATA_VITAMIN_E.id]),
            toDouble(rows[NSDA.DATA_VITAMIN_K.id]),
            toDouble(rows[NSDA.DATA_THIAMINE.id]),
            toDouble(rows[NSDA.DATA_RIBOFLAVIN.id]),
            toDouble(rows[NSDA.DATA_NIACIN.id]),
            null,
            toDouble(rows[NSDA.DATA_FOLATE.id]),
            toDouble(rows[NSDA.DATA_PHOSPHORUS.id]),
            null,
            toDouble(rows[NSDA.DATA_IRON.id]),
            toDouble(rows[NSDA.DATA_CALCIUM.id]),
            toDouble(rows[NSDA.DATA_POTASSIUM.id]),
            toDouble(rows[NSDA.DATA_MAGNESIUM.id]),
            toDouble(rows[NSDA.DATA_SODIUM.id]),
            toDouble(rows[NSDA.DATA_SELENIUM.id]),
            toDouble(rows[NSDA.DATA_ZINK.id])
        );

        if (cols[NSDA.GROUP_SPECIFIC_TAGS.id] != null) {
            String[] tags = cols[NSDA.GROUP_SPECIFIC_TAGS.id].split(",");
            Arrays.setAll(tags, i -> tags[i].trim());
            Collections.addAll(specificFood.tags, tags);
        }
        if (cols[NSDA.GROUP_PIECE_WEIGHT.id] != null) {
            specificFood.pieceWeightGrams = Integer.parseInt(cols[NSDA.GROUP_PIECE_WEIGHT.id]);
        }
        if (cols[NSDA.GROUP_DENSITY_CONSTANT.id] != null) {
            specificFood.densityConstant = Double.parseDouble(cols[NSDA.GROUP_DENSITY_CONSTANT.id]);
        }
        if (cols[NSDA.GROUP_EXAMPLE_BRANDS.id] != null) {
            specificFood.exampleBrands = cols[NSDA.GROUP_EXAMPLE_BRANDS.id].trim();
        }
        if (cols[NSDA.GROUP_SCIENTIFIC_NAME.id] != null) {
            specificFood.scientificName = cols[NSDA.GROUP_SCIENTIFIC_NAME.id].trim();
        }
        if (cols[NSDA.GROUP_CLASSIFICATION.id] != null) {
            specificFood.category = getCategory(cols[NSDA.GROUP_CLASSIFICATION.id].trim());
        }

        if (cols[NSDA.GROUP_DEFAULT.id] != null) {
            if (cols[NSDA.GROUP_EXTRA_TAGS.id] != null) {
                String[] tags = cols[NSDA.GROUP_EXTRA_TAGS.id].split(",");
                for (String tag : tags) {
                    foodGroup.searchTags.add(tag.trim());
                }
            }
            defaultFoods.put(foodGroup, specificFood);
        } else {
            foodGroup.foods.add(specificFood);
        }

        specificFood.group = foodGroup;
        foodGroups.add(foodGroup);
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

                Food food = db.find(Food.class)
                    .where().conjunction()
                    .eq("dataSourceId", fineliId)
                    .eq("dataSource", DataSource.FINELI).findUnique();

                if (food == null) {
                    throw new RuntimeException("No food with food number " + fineliId);
                }

                ingredients.add(new Ingredient(new Amount(amount, unit), food));
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
            case "Rökt": case "Rokt":
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

    private static Category getCategory(String str) {
        switch (str) {
            case "Baljväxter": return Category.PULSES_VEGETABLES;
            case "Övrig frukt": return Category.OTHER_FRUITS;
            case "Fruktkonserver": return Category.CANNED_FRUITS;
            case "Juicer": return Category.JUICES;
            case "Saftdrycker": return Category.JUICE_DRINKS;
            case "Fiskprodukter": return Category.FISH_PRODUCTS;
            case "Citrusfrukter": return Category.CITRUS_FRUITS;
            case "Äppelfrukt": return Category.MALACEOUS_FRUITS;
            case "Bladgrönsaker": return Category.LEAF_VEGETABLES;
            case "Grönsaksfrukt": return Category.FRUIT_VEGETABLES;
            case "Rot- och knölväxter": return Category.ROOT_VEGETABLES_AND_TUBERS;
            case "Olja": return Category.OILS;
            case "Gris": return Category.PORK;
            case "Matlagnings- och industrifett": return Category.FATS_FOR_COOKING_AND_INDUSTRIA;
            case "Hjälpämnen vid tillverkning": return Category.MISCELLANEOUS_INGREDIENTS;
            case "Korv": return Category.SAUSAGES;
            case "Grönsakskonserver": return Category.CANNED_VEGETABLES;
            case "Bär": return Category.BERRIES;
            case "Mjölk": return Category.MILK;
            case "Torkade örter": return Category.DRIED_HERBS;
            case "Skaldjur": return Category.CRUSTACEANS_AND_MOLLUSCS;
            case "Ost": return Category.CHEESE;
            case "Nöt": return Category.BEEF;
            case "Organ": return Category.OFFAL;
            case "Kål": return Category.CABBAGES;
            case "Övrigt spannmål": return Category.OTHER_GRAINS;
            case "Fisk": return Category.FISH;
            case "Köttprodukter": return Category.MEAT_PRODUCTS;
            case "Fåglar": return Category.BIRDS;
            case "Modersmjölksersättningar och modersmjölk": return Category.INFANT_FORMULAS_AND_HUMAN_MILK;
            case "Kaffe": return Category.COFFEE;
            case "Nötter, frön": return Category.NUTS_AND_SEEDS;
            case "Svamp": return Category.EDIBLE_FUNGI;
            case "Choklad": return Category.CHOCOLATE;
            case "Övriga alkoholdrycker": return Category.OTHER_ALCOHOLIC_BEVERAGES;
            case "Vete": return Category.WHEAT;
            case "Grädde/creme": return Category.CREAM;
            case "Kryddsåser": return Category.CONDIMENTS;
            case "Syrade mjölkprodukter": return Category.FERMENTED_MILK_PRODUCTS;
            case "Socker, sirap": return Category.SUGAR_AND_SYRUPS;
            case "Sötsaker": return Category.NON_CHOCOLATE_CONFECTIONERY;
            case "Läskedrycker": return Category.SOFT_DRINKS;
            case "Pasta, makaroner": return Category.PASTA_AND_MACARONI;
            case "Glass": return Category.ICE_CREAM;
            case "Ris": return Category.RICE;
            case "Lökgrönsaker": return Category.ONION_FAMILY_VEGETABLES;
            case "Övriga fetter, fettprodukter": return Category.OTHER_FAT_PRODUCTS;
            case "Vilt": return Category.GAME_MEAT;
            case "Havre, korn": return Category.OATS_AND_BARLEY;
            case "Starksprit": return Category.SPIRITS;
            case "Stärkelse": return Category.STARCHES;
            case "Lamm": return Category.LAMB;
            case "Kliniska näringspreparat": return Category.PRODUCTS_FOR_NUTRITIONAL_SUPPORT;
            case "Margarin och matfett >55 %": return Category.MARGARINE_FAT_SPREAD_OVER_55;
            case "Margarin och matfett <55 %": return Category.MARGARINE_FAT_SPREAD_UNDER_55;
            case "Smör, mjölkfettblandningar": return Category.BUTTER_AND_BUTTER_SPREADS;
            case "Vatten": return Category.WATER;
            case "Råg": return Category.RYE;
            case "Övrig mjölk": return Category.OTHER_MILK_PRODUCTS;
            case "Diverse godis": return Category.OTHER_SUGAR_PRODUCTS;
            case "Öl": return Category.BEER;
            case "Torkade kryddor": return Category.DRIED_SPICES;
            case "Potatisprodukter": return Category.POTATO_PRODUCTS;
            case "Potatis": return Category.POTATOES;
            case "Vin": return Category.WINE;
            case "Salt": return Category.SALT;
            case "Torrt bröd": return Category.CRISPBREADS;
            case "Sojaprodukter": return Category.SOYA_PRODUCTS;
            case "Sötningsmedel": return Category.SWEETENERS;
            case "Animaliskt fett": return Category.ANIMAL_FATS;
            case "Te": return Category.TEA;
            case "Snacks": return Category.SNACKS;
            case "Ägg av andra fåglar": return Category.CHICKEN_EGGS;
            case "Ägg, hönsägg": return Category.CHICKEN_EGGS;
            // For windows with error for å,ä,ö.
            case "Baljvaxter": return Category.PULSES_VEGETABLES;
            case "Ovrig frukt": return Category.OTHER_FRUITS;
            case "Appelfrukt": return Category.MALACEOUS_FRUITS;
            case "Bladgronsaker": return Category.LEAF_VEGETABLES;
            case "Gronsaksfrukt": return Category.FRUIT_VEGETABLES;
            case "Rot- och knolvaxter": return Category.ROOT_VEGETABLES_AND_TUBERS;
            case "Hjalpamnen vid tillverkning": return Category.MISCELLANEOUS_INGREDIENTS;
            case "Gronsakskonserver": return Category.CANNED_VEGETABLES;
            case "Bar": return Category.BERRIES;
            case "Mjolk": return Category.MILK;
            case "Torkade orter": return Category.DRIED_HERBS;
            case "Not": return Category.BEEF;
            case "Kal": return Category.CABBAGES;
            case "Ovrigt spannmal": return Category.OTHER_GRAINS;
            case "Kottprodukter": return Category.MEAT_PRODUCTS;
            case "Faglar": return Category.BIRDS;
            case "Modersmjolksersattningar och modersmjolk": return Category.INFANT_FORMULAS_AND_HUMAN_MILK;
            case "Notter, fron": return Category.NUTS_AND_SEEDS;
            case "Ovriga alkoholdrycker": return Category.OTHER_ALCOHOLIC_BEVERAGES;
            case "Gradde/creme": return Category.CREAM;
            case "Kryddsaser": return Category.CONDIMENTS;
            case "Syrade mjolkprodukter": return Category.FERMENTED_MILK_PRODUCTS;
            case "Sotsaker": return Category.NON_CHOCOLATE_CONFECTIONERY;
            case "Laskedrycker": return Category.SOFT_DRINKS;
            case "Lokgronsaker": return Category.ONION_FAMILY_VEGETABLES;
            case "Ovriga fetter, fettprodukter": return Category.OTHER_FAT_PRODUCTS;
            case "Starkelse": return Category.STARCHES;
            case "Kliniska naringspreparat": return Category.PRODUCTS_FOR_NUTRITIONAL_SUPPORT;
            case "Smor, mjolkfettblandningar": return Category.BUTTER_AND_BUTTER_SPREADS;
            case "Rag": return Category.RYE;
            case "Ovrig mjolk": return Category.OTHER_MILK_PRODUCTS;
            case "Ol": return Category.BEER;
            case "Sotningsmedel": return Category.SWEETENERS;
            case "Agg av andra faglar": return Category.CHICKEN_EGGS;
            case "Agg, honsagg": return Category.CHICKEN_EGGS;
            case "Torrt brod": return Category.CRISPBREADS;
            default:
                System.out.println("No category found named: " + str);
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
        if (col == null) {
            return null;
        } else if (col.equals("<0.1")) {
            return 0.09;
        } else if (col.equals("<0.01")) {
            return 0.009;
        } else if (!col.equals("N/A") && !col.equals("NULL")) {
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
        System.out.println(GREEN + "Done" + RESET);
    }
}
