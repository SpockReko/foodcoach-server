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

    private static final String FINELI_GROUP_TSV = "resources/fineli_food/Fineli_GeneralFoods.tsv";
    private static final String FINELI_DATA_TSV = "resources/fineli_food/Fineli_FoodData.tsv";
    private static final String SLV_GROUP_TSV = "resources/lmv_food/LMV_GeneralFoods.tsv";
    private static final String SLV_DATA_TSV = "resources/lmv_food/LMV_FoodData.tsv";
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
        GROUP_SCIENTIFIC_NAME(9),
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

    private static Set<FoodGroup> foodGroups = new HashSet<>();
    private static Map<FoodGroup, Food> defaultFoods = new HashMap<>();
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

        List<String[]> fineliGroupRows = parser.parseAll(getReader(FINELI_GROUP_TSV));
        List<String[]> fineliDataRows = parser.parseAll(getReader(FINELI_DATA_TSV));

        List<String[]> lmvGroupRows = parser.parseAll(getReader(SLV_GROUP_TSV));
        List<String[]> lmvDataRows = parser.parseAll(getReader(SLV_DATA_TSV));

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

        System.out.print(CYAN + "Parsing foods from Fineli... " + RESET);
        for (String[] cols : fineliGroupRows) {
            readGroupRow(cols, fineliDataRows);
        }
        printDone();

        System.out.print(CYAN + "Parsing extra foods from Livsmedelsverket... " + RESET);
        for (String[] cols : lmvGroupRows) {
            readExtraGroupRow(cols, lmvDataRows);
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

    private static void readGroupRow(String[] cols, List<String[]> dataRows) {
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

    private static void readExtraGroupRow(String[] cols, List<String[]> dataRows) {
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
            case "Baljväxter": return Category.Baljvaxter;
            case "Övrig frukt": return Category.ovrig_frukt;
            case "Fruktkonserver": return Category.Fruktkonserver;
            case "Juicer": return Category.Juicer;
            case "Saftdrycker": return Category.Saftdrycker;
            case "Fiskprodukter": return Category.Fiskprodukter;
            case "Citrusfrukter": return Category.Citrusfrukter;
            case "Äppelfrukt": return Category.appelfrukt;
            case "Bladgrönsaker": return Category.Bladgronsaker;
            case "Grönsaksfrukt": return Category.Gronsaksfrukt;
            case "Rot- och knölväxter": return Category.Rot_och_knolvaxter;
            case "Olja": return Category.Olja;
            case "Gris": return Category.Gris;
            case "Matlagnings- och industrifett": return Category.Matlagnings_och_industrifett;
            case "Hjälpämnen vid tillverkning": return Category.Hjalpamnen_vid_tillverkning;
            case "Korv": return Category.Korv;
            case "Grönsakskonserver": return Category.Gronsakskonserver;
            case "Bär": return Category.Bar;
            case "Mjölk": return Category.Mjolk;
            case "Torkade örter": return Category.Torkade_orter;
            case "Skaldjur": return Category.Skaldjur;
            case "Ost": return Category.Ost;
            case "Nöt": return Category.Not;
            case "Organ": return Category.Organ;
            case "Kål": return Category.Kal;
            case "Övrigt spannmål": return Category.ovrigt_spannmal;
            case "Fisk": return Category.Fisk;
            case "Köttprodukter": return Category.Kottprodukter;
            case "Fåglar": return Category.Faglar;
            case "Modersmjölksersättningar och modersmjölk": return Category.Modersmjolksersattningar_och_modersmjolk;
            case "Kaffe": return Category.Kaffe;
            case "Nötter, frön": return Category.Notter_fron;
            case "Svamp": return Category.Svamp;
            case "Choklad": return Category.Choklad;
            case "Övriga alkoholdrycker": return Category.ovriga_alkoholdrycker;
            case "Vete": return Category.Vete;
            case "Grädde/creme": return Category.Gradde_creme;
            case "Kryddsåser": return Category.Kryddsaser;
            case "Syrade mjölkprodukter": return Category.Syrade_mjolkprodukter;
            case "Socker, sirap": return Category.Socker_sirap;
            case "Sötsaker": return Category.Sotsaker;
            case "Läskedrycker": return Category.Laskedrycker;
            case "Pasta, makaroner": return Category.Pasta_makaroner;
            case "Glass": return Category.Glass;
            case "Ris": return Category.Ris;
            case "Lökgrönsaker": return Category.Lokgronsaker;
            case "Övriga fetter, fettprodukter": return Category.ovriga_fetter_fettprodukter;
            case "Vilt": return Category.Vilt;
            case "Havre, korn": return Category.Havre_korn;
            case "Starksprit": return Category.Starksprit;
            case "Stärkelse": return Category.Starkelse;
            case "Lamm": return Category.Lamm;
            case "Kliniska näringspreparat": return Category.Kliniska_naringspreparat;
            case "Margarin och matfett >55 %": return Category.Margarin_och_matfett_over_55;
            case "Margarin och matfett <55 %": return Category.Margarin_och_matfett_under_55;
            case "Smör, mjölkfettblandningar": return Category.Smor_mjolkfettblandningar;
            case "Vatten": return Category.Vatten;
            case "Råg": return Category.Rag;
            case "Övrig mjölk": return Category.ovrig_mjolk;
            case "Diverse godis": return Category.Diverse_godis;
            case "Öl": return Category.ol;
            case "Torkade kryddor": return Category.Torkade_kryddor;
            case "Potatisprodukter": return Category.Potatisprodukter;
            case "Potatis": return Category.Potatis;
            case "Vin": return Category.Vin;
            case "Salt": return Category.Salt;
            case "Torrt bröd": return Category.Torrt_brod;
            case "Sojaprodukter": return Category.Sojaprodukter;
            case "Sötningsmedel": return Category.Sotningsmedel;
            case "Animaliskt fett": return Category.Animaliskt_fett;
            case "Te": return Category.Te;
            case "Snacks": return Category.Snacks;
            case "Ägg av andra fåglar": return Category.agg_honsagg;
            case "Ägg, hönsägg": return Category.agg_honsagg;
            // For windows with error for å,ä,ö.
            case "Baljvaxter": return Category.Baljvaxter;
            case "Ovrig frukt": return Category.ovrig_frukt;
            case "Appelfrukt": return Category.appelfrukt;
            case "Bladgronsaker": return Category.Bladgronsaker;
            case "Gronsaksfrukt": return Category.Gronsaksfrukt;
            case "Rot- och knolvaxter": return Category.Rot_och_knolvaxter;
            case "Hjalpamnen vid tillverkning": return Category.Hjalpamnen_vid_tillverkning;
            case "Gronsakskonserver": return Category.Gronsakskonserver;
            case "Bar": return Category.Bar;
            case "Mjolk": return Category.Mjolk;
            case "Torkade orter": return Category.Torkade_orter;
            case "Not": return Category.Not;
            case "Kal": return Category.Kal;
            case "Ovrigt spannmal": return Category.ovrigt_spannmal;
            case "Kottprodukter": return Category.Kottprodukter;
            case "Faglar": return Category.Faglar;
            case "Modersmjolksersattningar och modersmjolk": return Category.Modersmjolksersattningar_och_modersmjolk;
            case "Notter, fron": return Category.Notter_fron;
            case "Ovriga alkoholdrycker": return Category.ovriga_alkoholdrycker;
            case "Gradde/creme": return Category.Gradde_creme;
            case "Kryddsaser": return Category.Kryddsaser;
            case "Syrade mjolkprodukter": return Category.Syrade_mjolkprodukter;
            case "Sotsaker": return Category.Sotsaker;
            case "Laskedrycker": return Category.Laskedrycker;
            case "Lokgronsaker": return Category.Lokgronsaker;
            case "Ovriga fetter, fettprodukter": return Category.ovriga_fetter_fettprodukter;
            case "Starkelse": return Category.Starkelse;
            case "Kliniska naringspreparat": return Category.Kliniska_naringspreparat;
            case "Smor, mjolkfettblandningar": return Category.Smor_mjolkfettblandningar;
            case "Rag": return Category.Rag;
            case "Ovrig mjolk": return Category.ovrig_mjolk;
            case "Ol": return Category.ol;
            case "Sotningsmedel": return Category.Sotningsmedel;
            case "Agg av andra faglar": return Category.agg_honsagg;
            case "Agg, honsagg": return Category.agg_honsagg;
            case "Torrt brod": return Category.Torrt_brod;
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
        System.out.println(GREEN + "Done" + RESET);
    }
}
