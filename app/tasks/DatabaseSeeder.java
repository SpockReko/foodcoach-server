package tasks;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.ServerConfig;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import me.tongfei.progressbar.ProgressBar;
import models.food.*;
import models.recipe.Amount;
import models.recipe.Ingredient;
import models.recipe.Recipe;
import org.avaje.datasource.DataSourceConfig;
import play.Logger;

import javax.persistence.PersistenceException;
import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private static final String CSV_PATH = "resources/db/FoodDB_201702061629.csv";

    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String CYAN = "\u001B[36m";
    private static final String PURPLE = "\u001B[35m";
    private static final String RESET = "\u001B[0m";

    public static void main(String[] args) {

        db = getDatabase();

        System.out.println("\n" + PURPLE + "--- (Seeding database) ---\n" + RESET);

        try {
            db.find(FoodItem.class).where().eq("id", "1").findUnique();
        } catch (PersistenceException e) {
            System.out.println(YELLOW
                + "No database tables present. Please start server and run evolution script first!\n"
                + RESET);
            return;
        }

        System.out.println(CYAN + "Importing foods from Excel..." + RESET);
        try {
            importFoods();
        } catch (DatabaseNotEmptyException e) {
            System.out.println(YELLOW + "Foods already imported, moving on..." + RESET);
        }

        System.out.print(CYAN + "\nLinking food groups parents... " + RESET);
        linkFoodGroupsParents();
        printDone();

        System.out.print(CYAN + "\nAdding mocked recipes... " + RESET);
        mockRecipes();
        printDone();

        System.out.println();
    }

    private static void importFoods() throws DatabaseNotEmptyException {

        CsvParserSettings settings = new CsvParserSettings();
        settings.getFormat().setLineSeparator("\n");
        settings.setNumberOfRowsToSkip(1);

        CsvParser parser = new CsvParser(settings);
        List<String[]> allRows = parser.parseAll(getReader(CSV_PATH));

        if (db.find(FoodItem.class).where().findCount() > 0) {
            throw new DatabaseNotEmptyException();
        }

        ProgressBar pb = new ProgressBar("Importing", allRows.size(), 50).start();

        for (String[] cols : allRows) {
            String name = cols[0];
            int lmvFoodNumber = Integer.parseInt(cols[1]); Float energyKcal = toFloat(cols[2]);
            Float energyKj = toFloat(cols[3]); Float carbohydrates = toFloat(cols[4]);
            Float fat = toFloat(cols[5]); Float protein = toFloat(cols[6]);
            Float fibre = toFloat(cols[7]); Float water = toFloat(cols[8]);
            Float alcohol = toFloat(cols[9]); Float ash = toFloat(cols[10]);
            Float monosaccharides = toFloat(cols[11]); Float disaccharides = toFloat(cols[12]);
            Float sucrose = toFloat(cols[13]); Float wholeGrain = toFloat(cols[14]);
            Float sugar = toFloat(cols[15]); Float sumSaturatedFats = toFloat(cols[16]);
            Float fattyAcid40100 = toFloat(cols[17]); Float fattyAcid120 = toFloat(cols[18]);
            Float fattyAcid140 = toFloat(cols[19]); Float fattyAcid160 = toFloat(cols[20]);
            Float fattyAcid180 = toFloat(cols[21]); Float fattyAcid200 = toFloat(cols[22]);
            Float sumMonounsaturatedFats = toFloat(cols[23]); Float fattyAcid161 = toFloat(cols[24]);
            Float fattyAcid181 = toFloat(cols[25]); Float sumPolyunsaturatedFats = toFloat(cols[26]);
            Float fattyAcid182 = toFloat(cols[27]); Float fattyAcid183 = toFloat(cols[28]);
            Float fattyAcid204 = toFloat(cols[29]); Float epaFattyAcid205 = toFloat(cols[30]);
            Float dpaFattyAcid225 = toFloat(cols[31]); Float dhaFattyAcid226 = toFloat(cols[32]);
            Float cholesterol = toFloat(cols[33]); Float retinol = toFloat(cols[34]);
            Float vitaminA = toFloat(cols[35]); Float betaKaroten = toFloat(cols[36]);
            Float vitaminD = toFloat(cols[37]); Float vitaminE = toFloat(cols[38]);
            Float vitaminK = toFloat(cols[39]); Float thiamine = toFloat(cols[40]);
            Float riboflavin = toFloat(cols[41]); Float vitaminC = toFloat(cols[42]);
            Float niacin = toFloat(cols[43]); Float niacinEquivalents = toFloat(cols[44]);
            Float vitaminB6 = toFloat(cols[45]); Float vitaminB12 = toFloat(cols[46]);
            Float folate = toFloat(cols[47]); Float phosphorus = toFloat(cols[48]);
            Float iodine = toFloat(cols[49]); Float iron = toFloat(cols[50]);
            Float calcium = toFloat(cols[51]); Float potassium = toFloat(cols[52]);
            Float magnesium = toFloat(cols[53]); Float sodium = toFloat(cols[54]);
            Float salt = toFloat(cols[55]); Float selenium = toFloat(cols[56]);
            Float zink = toFloat(cols[57]); Float waste = toFloat(cols[58]);
            String scientificName = cols[60];
            String lmvProject = cols[59];

            Fats fats = new Fats(fat, sumSaturatedFats, fattyAcid40100, fattyAcid120, fattyAcid140,
                fattyAcid160, fattyAcid180, fattyAcid200, sumMonounsaturatedFats, fattyAcid161,
                fattyAcid181, sumPolyunsaturatedFats, fattyAcid182, fattyAcid183, fattyAcid204,
                epaFattyAcid205, dpaFattyAcid225, dhaFattyAcid226);
            Sugars sugars = new Sugars(sugar, monosaccharides, disaccharides, sucrose);
            Vitamins vitamins = new Vitamins(retinol, betaKaroten, vitaminA, vitaminB6,
                vitaminB12, vitaminC, vitaminD, vitaminE, vitaminK, thiamine,
                riboflavin, niacin, niacinEquivalents);
            Minerals minerals =  new Minerals(folate, phosphorus, iodine, iron, calcium, potassium,
                magnesium, sodium, salt, selenium, zink);
            FoodItem item =
                new FoodItem(name, scientificName, lmvFoodNumber, lmvProject, energyKcal, energyKj,
                    carbohydrates, protein, fibre, wholeGrain, cholesterol, water, alcohol, ash,
                    waste, sugars, fats, vitamins, minerals);

            if (cols[61] != null) {
                String[] groups = cols[61].split(";");
                for (String group : groups) {
                    linkFoodGroup(item, extractNameAndCode(group));
                }
            }
            if (cols[62] != null) {
                linkFoodSources(item, extractNameAndCode(cols[62]));
            }
            for (int i = 63; i < cols.length; i++) {
                if (cols[i] != null) {
                    String[] languals = cols[i].split(";");
                    for (String langual : languals) {
                        String[] nameOrCode = extractNameAndCode(langual);
                        linkFoodLangual(item, getLangualType(i), nameOrCode);
                    }
                }
            }

            db.save(item);
            pb.step();
        }
        pb.stop();
    }

    private static void linkFoodGroup(FoodItem item, String[] nameOrCode) {
        FoodGroup group;
        if (db.find(FoodGroup.class).where().eq("langualCode", nameOrCode[1]).findCount() == 0) {

            if (nameOrCode[1].isEmpty()) {
                FoodGroup existing =
                    db.find(FoodGroup.class).where().eq("name", nameOrCode[0]).findUnique();
                if (existing == null) {
                    group = new FoodGroup(nameOrCode[0], null);
                } else {
                    group = existing;
                }
            } else {
                group = new FoodGroup(nameOrCode[0], nameOrCode[1]);
            }

            db.save(group);
        } else {
            group = db.find(FoodGroup.class).where().eq("langualCode", nameOrCode[1]).findUnique();
        }

        item.groups.add(group);
    }

    private static void linkFoodSources(FoodItem item, String[] nameOrCode) {
        FoodSource source;
        if (db.find(FoodSource.class).where().eq("langualCode", nameOrCode[1]).findCount() == 0) {

            if (nameOrCode[1].isEmpty()) {
                FoodSource existing =
                    db.find(FoodSource.class).where().eq("name", nameOrCode[0]).findUnique();
                if (existing == null) {
                    source = new FoodSource(nameOrCode[0], null);
                } else {
                    source = existing;
                }
            } else {
                source = new FoodSource(nameOrCode[0], nameOrCode[1]);
            }

            db.save(source);
        } else {
            source =
                db.find(FoodSource.class).where().eq("langualCode", nameOrCode[1]).findUnique();
        }

        item.source = source;
    }

    private static void linkFoodLangual(FoodItem item, LangualTerm.Type type, String[] nameOrCode) {
        LangualTerm term;
        if (db.find(LangualTerm.class).where().eq("code", nameOrCode[1]).findCount() == 0) {

            if (nameOrCode[1].isEmpty()) {
                LangualTerm existing =
                    db.find(LangualTerm.class).where().eq("name", nameOrCode[0]).findUnique();
                if (existing == null) {
                    term = new LangualTerm(null, nameOrCode[0], type);
                } else {
                    term = existing;
                }
            } else {
                term = new LangualTerm(nameOrCode[1], nameOrCode[0], type);
            }

            db.save(term);
        } else {
            term = db.find(LangualTerm.class).where().eq("code", nameOrCode[1]).findUnique();
        }
        switch (type) {
            case PART_OF_PLANT_OR_ANIMAL:
                item.partOfPlantOrAnimal = term;
                break;
            case PHYSICAL_FORM:
                item.physicalForm = term;
                break;
            case HEAT_TREATMENT:
                item.heatTreatment = term;
                break;
            case COOKING_METHOD:
                item.cookingMethod = term;
                break;
            case INDUSTRIAL_PROCESS:
                item.industrialProcess = term;
                break;
            case PRESERVATION_METHOD:
                item.preservationMethod = term;
                break;
            case PACKING_MEDIUM:
                item.packingMedium = term;
                break;
            case PACKING_TYPE:
                item.packingType = term;
                break;
            case PACKING_MATERIAL:
                item.packingMaterial = term;
                break;
            case LABEL_CLAIM:
                item.labelClaim = term;
                break;
            case GEOGRAPHIC_SOURCE:
                item.geographicSource = term;
                break;
            case DISTINCTIVE_FEATURES:
                item.distinctiveFeatures = term;
                break;
        }
    }

    private static void linkFoodGroupsParents() {
        if (db.find(FoodGroup.class).where().eq("langualCode", "A0784").findCount() == 0) {
            db.insert(new FoodGroup("Ost", "A0784"));
        }
        updateGroupParent("A0310", "A0784");
        updateGroupParent("A0311", "A0784");
        updateGroupParent("A0312", "A0784");
        updateGroupParent("A0314", "A0784");
        updateGroupParent("A0780", "A0778");
        updateGroupParent("A0781", "A0780");
        updateGroupParent("A0782", "A0780");
        updateGroupParent("A0783", "A0778");
        updateGroupParent("A0784", "A0778");
        updateGroupParent("A0786", "A0784");
        updateGroupParent("A0787", "A0784");
        updateGroupParent("A0788", "A0784");
        updateGroupParent("A0789", "A0778");
        if (db.find(FoodGroup.class).where().eq("langualCode", "A0790").findCount() == 0) {
            db.insert(new FoodGroup("Ägg eller äggprodukt", "A0790"));
        }
        updateGroupParent("A0791", "A0790");
        updateGroupParent("A0792", "A0790");
        updateGroupParent("A0794", "A0793");
        updateGroupParent("A0795", "A0793");
        updateGroupParent("A0796", "A0793");
        updateGroupParent("A0797", "A0793");
        updateGroupParent("A0798", "A0793");
        updateGroupParent("A0799", "A0793");
        updateGroupParent("A0800", "A0793");
        updateGroupParent("A0802", "A0801");
        updateGroupParent("A0803", "A0801");
        updateGroupParent("A0804", "A0801");
        if (db.find(FoodGroup.class).where().eq("langualCode", "A0805").findCount() == 0) {
            db.insert(new FoodGroup("Fett eller olja", "A0805"));
        }
        updateGroupParent("A0806", "A0805");
        updateGroupParent("A0807", "A0805");
        if (db.find(FoodGroup.class).where().eq("langualCode", "A0808").findCount() == 0) {
            db.insert(new FoodGroup("Animaliska fetter", "A0808"));
        }
        updateGroupParent("A0808", "A0805");
        updateGroupParent("A0809", "A0808");
        updateGroupParent("A0810", "A0808");
        updateGroupParent("A0813", "A0812");
        updateGroupParent("A0814", "A0812");
        updateGroupParent("A0815", "A0812");
        updateGroupParent("A0816", "A0812");
        if (db.find(FoodGroup.class).where().eq("langualCode", "A0817").findCount() == 0) {
            db.insert(new FoodGroup("Bröd och liknande produkter", "A0817"));
        }
        updateGroupParent("A0818", "A0817");
        updateGroupParent("A0819", "A0817");
        updateGroupParent("A0820", "A0817");
        updateGroupParent("A0821", "A0812");
        updateGroupParent("A0824", "A0823");
        updateGroupParent("A0827", "A0826");
        updateGroupParent("A0828", "A0826");
        updateGroupParent("A0830", "A0829");
        updateGroupParent("A0832", "A0831");
        updateGroupParent("A0834", "A0833");
        updateGroupParent("A0836", "A0835");
        updateGroupParent("A0837", "A0835");
        updateGroupParent("A0838", "A0835");
        updateGroupParent("A0839", "A0835");
        if (db.find(FoodGroup.class).where().eq("langualCode", "A0840").findCount() == 0) {
            db.insert(new FoodGroup("Dryck (ej mjölk)", "A0840"));
        }
        updateGroupParent("A0841", "A0840");
        updateGroupParent("A0842", "A0840");
        updateGroupParent("A0843", "A0842");
        updateGroupParent("A0844", "A0842");
        updateGroupParent("A0845", "A0842");
        if (db.find(FoodGroup.class).where().eq("langualCode", "A0846").findCount() == 0) {
            db.insert(new FoodGroup("Dryck med alkohol", "A0846"));
        }
        updateGroupParent("A0847", "A0846");
        updateGroupParent("A0848", "A0846");
        updateGroupParent("A0849", "A0846");
        updateGroupParent("A0850", "A0846");
        updateGroupParent("A0854", "A0853");
        updateGroupParent("A0856", "A0853");
        updateGroupParent("A0857", "A0853");
        updateGroupParent("A0858", "A0853");
        updateGroupParent("A0859", "A0853");
        updateGroupParent("A0860", "A0853");
        updateGroupParent("A0862", "A0861");
        updateGroupParent("A0863", "A0861");
        updateGroupParent("A0864", "A0861");
        updateGroupParent("A0865", "A0861");
        updateGroupParent("A0866", "A0861");
        updateGroupParent("A0868", "A0861");
        updateGroupParent("A0870", "A0869");
    }

    private static void mockRecipes() {
        FoodItem jasminris = db.find(FoodItem.class).where().eq("lmvFoodNumber", 2477).findUnique();
        jasminris.densityConstant = 0.85;
        db.save(jasminris);
        FoodItem paprika = db.find(FoodItem.class).where().eq("lmvFoodNumber", 351).findUnique();
        FoodItem linser = db.find(FoodItem.class).where().eq("lmvFoodNumber", 884).findUnique();
        linser.densityConstant = 0.9;
        db.save(linser);
        FoodItem kokosmjolk = db.find(FoodItem.class).where().eq("lmvFoodNumber", 1590).findUnique();
        kokosmjolk.densityConstant = 1.0;
        db.save(kokosmjolk);
        FoodItem lok = db.find(FoodItem.class).where().eq("lmvFoodNumber", 344).findUnique();
        FoodItem tomater = db.find(FoodItem.class).where().eq("lmvFoodNumber", 422).findUnique();
        List<Ingredient> ingredients = new LinkedList<>();
        ingredients.add(new Ingredient(jasminris, new Amount(2, Amount.Unit.DECILITER)));
        ingredients.add(new Ingredient(paprika, new Amount(150, Amount.Unit.GRAM)));
        ingredients.add(new Ingredient(linser, new Amount(2, Amount.Unit.DECILITER)));
        ingredients.add(new Ingredient(kokosmjolk, new Amount(400, Amount.Unit.MILLILITER)));
        ingredients.add(new Ingredient(lok, new Amount(100, Amount.Unit.GRAM)));
        ingredients.add(new Ingredient(tomater, new Amount(400, Amount.Unit.GRAM)));
        Recipe linsgryta = new Recipe("Linsgryta", null, 4, ingredients);
        db.save(linsgryta);
    }

	/*
    Helper methods
	 */

    private static void updateGroupParent(String mainCode, String parentCode) {
        try {
            FoodGroup group =
                db.find(FoodGroup.class).where().eq("langualCode", mainCode).findUnique();
            group.parent =
                db.find(FoodGroup.class).where().eq("langualCode", parentCode).findUnique();
            db.save(group);
        } catch (NullPointerException e) {
            Logger
                .error("Failed to set FoodGroup parent '" + parentCode + "' to '" + mainCode + "'");
        }
    }

    private static String[] extractNameAndCode(String line) {
        String code = "";
        Pattern pattern = Pattern.compile("[A-Z]\\d{4}");
        Matcher matcher = pattern.matcher(line);
        if (matcher.find())
            code = matcher.group(0);
        String name = line.split("[A-Z]\\d{4}")[0];
        if (name.contains("()")) {
            name = name.substring(0, name.length() - 1);
        }
        name = name.substring(0, name.length() - 1);

        return new String[] { name.trim(), code };
    }

    private static Float toFloat(String col) {
        if (!col.equals("NULL")) {
            return Float.parseFloat(col);
        } else {
            return null;
        }
    }

    private static EbeanServer getDatabase() {
        Config conf = ConfigFactory.parseFile(new File("conf/application.conf")).resolve();
        DataSourceConfig foodDB = new DataSourceConfig();
        ServerConfig config = new ServerConfig();

        config.setName("mysql");
        foodDB.setDriver("com.mysql.jdbc.Driver");
        foodDB.setUsername(conf.getString("db.default.username"));
        foodDB.setPassword(conf.getString("db.default.password"));
        foodDB.setUrl(conf.getString("db.default.url"));
        config.setDataSourceConfig(foodDB);
        config.setDefaultServer(true);
        config.setRegister(false);

        return EbeanServerFactory.create(config);
    }

    private static Reader getReader(String path) {
        try {
            return new BufferedReader(new FileReader(path));
        } catch (FileNotFoundException e) {
            throw new IllegalStateException("Unable to read input", e);
        }
    }

    private static LangualTerm.Type getLangualType(int column) {
        switch (column) {
            case 63:
                return LangualTerm.Type.PART_OF_PLANT_OR_ANIMAL;
            case 64:
                return LangualTerm.Type.PHYSICAL_FORM;
            case 65:
                return LangualTerm.Type.HEAT_TREATMENT;
            case 66:
                return LangualTerm.Type.COOKING_METHOD;
            case 67:
                return LangualTerm.Type.INDUSTRIAL_PROCESS;
            case 68:
                return LangualTerm.Type.PRESERVATION_METHOD;
            case 69:
                return LangualTerm.Type.PACKING_MEDIUM;
            case 70:
                return LangualTerm.Type.PACKING_TYPE;
            case 71:
                return LangualTerm.Type.PACKING_MATERIAL;
            case 72:
                return LangualTerm.Type.LABEL_CLAIM;
            case 73:
                return LangualTerm.Type.GEOGRAPHIC_SOURCE;
            case 74:
                return LangualTerm.Type.DISTINCTIVE_FEATURES;
            default:
                throw new IllegalArgumentException("Not a valid column!");
        }
    }

    private static void printDone() {
        System.out.println(GREEN + "Done" + RESET);
    }

    private static class DatabaseNotEmptyException extends Exception {}
}
