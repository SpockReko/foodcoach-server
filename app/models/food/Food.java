package models.food;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.DbArray;
import com.fasterxml.jackson.annotation.JsonBackReference;
import helpers.Constants;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

import static models.food.Category.Sotsaker;

/**
 * Created by fredrikkindstrom on 2017-04-04.
 */
@Entity
@Table(name = "Foods")
public class Food extends Model {

    @Id private long id;
    private final int dataSourceId;

    @NotNull public String name;
    @JsonBackReference
    @NotNull @ManyToOne(cascade = CascadeType.PERSIST) public FoodGeneral general;
    @DbArray(length = 255) public List<String> tags = new ArrayList<>();

    public String scientificName;
    public String exampleBrands;
    public Integer pieceWeightGrams;
    public Double densityConstant;

    @Enumerated(EnumType.STRING) public Processing processing;
    @Enumerated(EnumType.STRING) public Category category;
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
    public double getNutrient(Nutrient nutrient) {
        Double value;
        // TODO Returning 0 instead of null where no data present for now
        switch (nutrient) {
            case KCAL:
                value = energyKj / Constants.KCAL_FACTOR;
                break;
            case KJ:
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
    public double getCO2(){
        Double value=0.0;
        Category category=this.category;
        //frukt, gronsaker
        switch(category) {
            case appelfrukt : case Citrusfrukter : case ovrig_frukt : case Fruktkonserver : case  Bladgronsaker :
                case Gronsaksfrukt: case Gronsakskonserver: case Bar: case Kal: case Svamp : case Lokgronsaker:
                value = 0.1;
                break;
        }

        switch(category) {
            case Rot_och_knolvaxter:
                value = 0.02;
                break;
        }

        //drycker
        switch(category) {
            case Saftdrycker: case  Juicer:
                value = 0.1;
                break;
        }
        switch(category) {
            case Kaffe: case Te:
                value = 0.3;
                break;
        }
        switch(category) {
            case ovriga_alkoholdrycker: case Starksprit: case ol: case Vin:
                value = 0.3;
                break;
        }


        switch(category) {
            case Baljvaxter :
                value = 0.07;
                break;
        }
        switch(category) {
            case Notter_fron :
                value = 0.15;
                break;
        }
        //kolhydrat

        switch(category) {
            case ovrigt_spannmal: case Vete : case Havre_korn
                : case Socker_sirap: case Starkelse: case Sotningsmedel: case
                    Torrt_brod: case Rag:
                value = 0.06;
                break;
        }
        switch(category) {
            case Ris :
            value = 0.2;
                break;
        }
        switch(category) {
            case Pasta_makaroner:
                value = 0.08;
                break;
        }
        switch(category) {
            case Potatis: case Potatisprodukter:
                value = 0.01;
                break;
        }

        //mejeri
        switch(category) {
            case Mjolk: case Modersmjolksersattningar_och_modersmjolk: case Syrade_mjolkprodukter: case ovrig_mjolk:
                value = 0.1;
                break;
        }
        switch(category) {
            case agg_honsagg: case agg_av_andra_faglar:
                value = 0.15;
                break;
        }
        switch(category) {
            case Ost:
                value = 0.8;
                break;
        }
        switch(category) {
            case Smor_mjolkfettblandningar: case Animaliskt_fett:
                value = 0.8;
                break;
        }
        switch(category) {
            case Margarin_och_matfett_under_55: case Margarin_och_matfett_over_55: case ovriga_fetter_fettprodukter:
                value = 0.15;
                break;
        }
        switch(category) {
            case Gradde_creme :
                value = 0.4;
                break;
        }

        //sas, krydda

        switch(category) {
            case Kryddsaser: case Torkade_orter:case Hjalpamnen_vid_tillverkning: case Torkade_kryddor: case
                    Salt: case Kliniska_naringspreparat:
                value = 0.1;
                break;
        }


        switch(category) {
            case Olja: case Matlagnings_och_industrifett:
                value = 0.15;
                break;
        }
       //kott
        switch(category) {
            case Fisk :case  Fiskprodukter: case Skaldjur:
                value = 0.3;
                break;
        }
        switch(category) {
            case Not:
                value = 2.6;
                break;
        }
        switch(category) {
            case Gris : case  Kottprodukter :case  Organ:
                value = 0.6;
                break;
        }
        switch(category) {
            case Lamm:
                value = 2.1;
                break;
        }
        switch(category) {
            case Faglar:
                value = 0.3;
                break;
        }
        switch(category) {
            case Korv:
                value = 0.7;
                break;
        }
        switch(category) {
            case Vilt:
                value = 0.05;
                break;
        }
        switch(category) {
            case Sojaprodukter:
                value = 0.4;
                break;
        }

        //godis

        switch(category) {
            case Choklad: case Glass : case Sotsaker: case Diverse_godis: case Snacks:
                value = 0.2;
                break;
        }
        switch(category) {
            case Laskedrycker:
            value = 00.3;
                break;
        }
        switch(category) {
            case Vatten:
                value = 0.0;
                break;
        }
        return value;
    }
}
