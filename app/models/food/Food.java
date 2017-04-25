package models.food;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.DbArray;
import com.fasterxml.jackson.annotation.JsonBackReference;
import helpers.Constants;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * A factual food with attached nutrition information and other meta data.
 * All foods belong to a {@link FoodGroup}.
 *
 * @author Fredrik Kindstrom
 */
@Entity
@Table(name = "Foods")
public class Food extends Model {

    @Id private long id;
    private final int dataSourceId;

    @NotNull public String name;
    @JsonBackReference
    @NotNull @ManyToOne(cascade = CascadeType.PERSIST) public FoodGroup group;
    @DbArray(length = 255) public List<String> tags = new ArrayList<>();

    public String scientificName;
    public String exampleBrands;
    public Integer pieceWeightGrams;
    public Double densityConstant;

    @Enumerated(EnumType.STRING) public Processing processing;
    @Enumerated(EnumType.STRING) public Category category;
    @ManyToMany(cascade = CascadeType.ALL) public List<Diet> diets = new ArrayList<>();
    @Enumerated(EnumType.STRING) private final DataSource dataSource;

    private Double energyKj;
    @Column(name = "carbohydrates_g") private Double carbohydrates;
    @Column(name = "protein_g") private Double protein;
    @Column(name = "fat_g") private Double fat;
    @Column(name = "fibre_g") private Double fibre;
    @Column(name = "alcohol_g") private Double alcohol;
    @Column(name = "salt_g") private Double salt;

    @Column(name = "vitamin_a_ug") private Double vitaminA;
    @Column(name = "vitamin_b6_ug") private Double vitaminB6;
    @Column(name = "vitamin_b12_ug") private Double vitaminB12;
    @Column(name = "vitamin_c_mg") private Double vitaminC;
    @Column(name = "vitamin_d_ug") private Double vitaminD;
    @Column(name = "vitamin_e_mg") private Double vitaminE;
    @Column(name = "vitamin_k_ug") private Double vitaminK;
    @Column(name = "thiamine_mg") private Double thiamine;
    @Column(name = "riboflavin_mg") private Double riboflavin;
    @Column(name = "niacin_mg") private Double niacin;
    @Column(name = "niacin_equivalents_mg") private Double niacinEquivalents;

    @Column(name = "folate_ug") private Double folate;
    @Column(name = "phosphorus_mg") private Double phosphorus;
    @Column(name = "iodine_ug") private Double iodine;
    @Column(name = "iron_mg") private Double iron;
    @Column(name = "calcium_mg") private Double calcium;
    @Column(name = "potassium_mg") private Double potassium;
    @Column(name = "magnesium_mg") private Double magnesium;
    @Column(name = "sodium_mg") private Double sodium;
    @Column(name = "selenium_ug") private Double selenium;
    @Column(name = "zinc_mg") private Double zinc;

    /**
     * Simple test constructor for a food.
     * @param name The name of the food.
     * @param dataSourceId The id of the food in the database the nutrition data is from.
     * @param dataSource The source of the nutrition information.
     */
    public Food(String name, int dataSourceId, DataSource dataSource) {
        this.name = name;
        this.dataSourceId = dataSourceId;
        this.dataSource = dataSource;
    }

    /**
     * Complete constructor for a food. The excessive amount of parameters is because
     * we don't want the nutrition data to be modified in a later stage.
     * @param name The name of the food.
     * @param dataSourceId The id of the food in the database the nutrition data is from.
     * @param dataSource The source of the nutrition information.
     * @param energyKj The amount of energy per 100g in kj.
     * @param carbohydrates The amount of carbohydrates per 100g in grams.
     * @param protein The amount of protein per 100g in grams.
     * @param fat The amount of fat per 100g in grams.
     * @param fibre The amount of fibre per 100g in grams.
     * @param alcohol The amount of alcohol per 100g in grams.
     * @param salt The amount of salt per 100g in grams.
     * @param vitaminA The amount of vitamin A per 100g in micrograms.
     * @param vitaminB6 The amount of vitamin B6 per 100g in micrograms.
     * @param vitaminB12 The amount of vitamin B12 per 100g in micrograms.
     * @param vitaminC The amount of vitamin C per 100g in milligrams.
     * @param vitaminD The amount of vitamin D per 100g in micrograms.
     * @param vitaminE The amount of vitamin E per 100g in milligrams.
     * @param vitaminK The amount of vitamin K per 100g in micrograms.
     * @param thiamine The amount of thiamine per 100g in milligrams.
     * @param riboflavin The amount of riboflavin per 100g in milligrams.
     * @param niacin The amount of niacin per 100g in milligrams.
     * @param niacinEquivalents The amount of niacin equivalents per 100g in milligrams.
     * @param folate The amount of folate per 100g in micrograms.
     * @param phosphorus The amount of phosphorus per 100g in milligrams.
     * @param iodine The amount of iodine per 100g in micrograms.
     * @param iron The amount of iron per 100g in milligrams.
     * @param calcium The amount of calcium per 100g in milligrams.
     * @param potassium The amount of potassium per 100g in milligrams.
     * @param magnesium The amount of magnesium per 100g in milligrams.
     * @param sodium The amount of sodium per 100g in milligrams.
     * @param selenium The amount of selenium per 100g in micrograms.
     * @param zinc The amount of zinc per 100g in milligrams.
     */
    public Food(String name, int dataSourceId, DataSource dataSource, Double energyKj,
        Double carbohydrates, Double protein, Double fat, Double fibre, Double alcohol, Double salt,
        Double vitaminA, Double vitaminB6, Double vitaminB12, Double vitaminC, Double vitaminD,
        Double vitaminE, Double vitaminK, Double thiamine, Double riboflavin, Double niacin,
        Double niacinEquivalents, Double folate, Double phosphorus, Double iodine, Double iron,
        Double calcium, Double potassium, Double magnesium, Double sodium, Double selenium,
        Double zinc) {
        this.name = name;
        this.dataSourceId = dataSourceId;
        this.dataSource = dataSource;
        this.energyKj = energyKj;
        this.carbohydrates = carbohydrates;
        this.protein = protein;
        this.fat = fat;
        this.fibre = fibre;
        this.alcohol = alcohol;
        this.salt = salt;
        this.vitaminA = vitaminA;
        this.vitaminB6 = vitaminB6;
        this.vitaminB12 = vitaminB12;
        this.vitaminC = vitaminC;
        this.vitaminD = vitaminD;
        this.vitaminE = vitaminE;
        this.vitaminK = vitaminK;
        this.thiamine = thiamine;
        this.riboflavin = riboflavin;
        this.niacin = niacin;
        this.niacinEquivalents = niacinEquivalents;
        this.folate = folate;
        this.phosphorus = phosphorus;
        this.iodine = iodine;
        this.iron = iron;
        this.calcium = calcium;
        this.potassium = potassium;
        this.magnesium = magnesium;
        this.sodium = sodium;
        this.selenium = selenium;
        this.zinc = zinc;
    }

    public static Finder<Long, Food> find = new Finder<>(Food.class);

    /**
     * The unique id of the food in the database.
     * @return The id as a long.
     */
    public long getId() {
        return id;
    }

    /**
     * The id of the food in the source database where the nutrition data originally comes from.
     * Either Fineli "livsmedelskod" or Livsmedelsverket "livsmedelsnummer". See {@link DataSource}.
     * @return The id as an integer.
     */
    public int getDataSourceId() {
        return dataSourceId;
    }

    /**
     * The source database where the nutrition data originally comes from.
     * Either Fineli or Livsmedelsverket. See {@link DataSource}.
     * @return The {@link DataSource} enum.
     */
    public DataSource getDataSource() {
        return dataSource;
    }

    /**
     * Returns a nutrient value for the specified nutrient.
     * @param nutrient The nutrient which you want information for. See {@link Nutrient}.
     * @return The nutrient value as a double, 0.0 if no value is present.
     */
    public double getNutrient(Nutrient nutrient) {
        Double value;
        // TODO Returning 0 instead of null where no data present for now
        switch (nutrient) {
            case ENERGY_KCAL:
                value = energyKj / Constants.KCAL_FACTOR;
                break;
            case ENERGY_KJ:
                value = energyKj;
                break;
            case CARBOHYDRATES:
                value = carbohydrates;
                break;
            case PROTEIN:
                value = protein;
                break;
            case FAT:
                value = fat;
                break;
            case FIBRE:
                value = fibre;
                break;
            case SALT:
                value = salt;
                break;
            case ALCOHOL:
                value = alcohol;
                break;
            case VITAMIN_A:
                value = vitaminA;
                break;
            case VITAMIN_B6:
                value = vitaminB6;
                break;
            case VITAMIN_B12:
                value = vitaminB12;
                break;
            case VITAMIN_C:
                value = vitaminC;
                break;
            case VITAMIN_D:
                value = vitaminD;
                break;
            case VITAMIN_E:
                value = vitaminE;
                break;
            case VITAMIN_K:
                value = vitaminK;
                break;
            case THIAMINE:
                value = thiamine;
                break;
            case RIBOFLAVIN:
                value = riboflavin;
                break;
            case NIACIN:
                value = niacin;
                break;
            case NIACIN_EQ:
                value = niacinEquivalents;
                break;
            case FOLATE:
                value = folate;
                break;
            case PHOSPHORUS:
                value = phosphorus;
                break;
            case IODINE:
                value = iodine;
                break;
            case IRON:
                value = iron;
                break;
            case CALCIUM:
                value = calcium;
                break;
            case POTASSIUM:
                value = potassium;
                break;
            case MAGNESIUM:
                value = magnesium;
                break;
            case SODIUM:
                value = sodium;
                break;
            case SELENIUM:
                value = selenium;
                break;
            case ZINC:
                value = zinc;
                break;
            default:
                throw new IllegalArgumentException("No such nutrient for this food");
        }
        return value != null ? value : 0d;
    }

    /**
     * Returns estimated CO2 emission for 100g of the food in kilograms.
     * @return The CO2 emission value as a double, 0.0 if no value is present.
     */
    public double getCO2() {
        return this.category == null ? 0.0 : Constants.CO2(this.category);
    }
}
