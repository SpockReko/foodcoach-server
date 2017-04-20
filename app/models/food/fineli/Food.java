package models.food.fineli;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.DbArray;
import com.fasterxml.jackson.annotation.JsonBackReference;
import helpers.Constants;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fredrikkindstrom on 2017-04-04.
 */
@Entity
@Table(name = "Foods")
public class Food extends Model {

    @Id private long id;
    @Column(unique = true) private final int dataSourceId;

    @NotNull public String name;
    @JsonBackReference
    @NotNull @ManyToOne(cascade = CascadeType.PERSIST) public FoodGeneral general;
    @DbArray(length = 255) public List<String> tags = new ArrayList<>();

    public String scientificName;
    public String exampleBrands;
    public Integer pieceWeightGrams;
    public Double densityConstant;

    @Enumerated(EnumType.STRING) public Processing processing;
    @ManyToMany(cascade = CascadeType.ALL) public List<Diet> diets = new ArrayList<>();

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

    @Enumerated(EnumType.STRING) private final DataSource dataSource;

    public Food(String name, int dataSourceId, DataSource dataSource) {
        this.name = name;
        this.dataSourceId = dataSourceId;
        this.dataSource = dataSource;
    }

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

    public long getId() {
        return id;
    }
    public int getDataSourceId() {
        return dataSourceId;
    }
    public Double getNutrient(Nutrient nutrient) {
        switch (nutrient) {
            case KCAL:
                return energyKj / Constants.KCAL_FACTOR;
            case KJ:
                return energyKj;
            case CARBOHYDRATES:
                return carbohydrates;
            case PROTEIN:
                return protein;
            case FAT:
                return fat;
            case FIBRE:
                return fibre;
            case SALT:
                return salt;
            case ALCOHOL:
                return alcohol;
            case VITAMIN_A:
                return vitaminA;
            case VITAMIN_B6:
                return vitaminB6;
            case VITAMIN_B12:
                return vitaminB12;
            case VITAMIN_C:
                return vitaminC;
            case VITAMIN_D:
                return vitaminD;
            case VITAMIN_E:
                return vitaminE;
            case VITAMIN_K:
                return vitaminK;
            case THIAMINE:
                return thiamine;
            case RIBOFLAVIN:
                return riboflavin;
            case NIACIN:
                return niacin;
            case NIACIN_EQ:
                return niacinEquivalents;
            case FOLATE:
                return folate;
            case PHOSPHORUS:
                return phosphorus;
            case IODINE:
                return iodine;
            case IRON:
                return iron;
            case CALCIUM:
                return calcium;
            case POTASSIUM:
                return potassium;
            case MAGNESIUM:
                return magnesium;
            case SODIUM:
                return sodium;
            case SELENIUM:
                return selenium;
            case ZINC:
                return zinc;
            default:
                throw new IllegalArgumentException("No such nutrient for this food");
        }
    }
}
