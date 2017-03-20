package models.food;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.DbArray;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * Represents a food item that is present in Livsmedelsdatabasen by Livsmedelsverket.
 * Contains embeddable classes that represents more nutritional information
 * like sugars, fats etc. This is just to make the code less cluttered with too much methods.
 * The underlying database tables contain all these fields as columns in a single table.
 * All data is measured with 100 grams of the food item.
 * The density value can be used to convert an item from 100 grams to 100 ml (1 dl).
 *
 * @author Fredrik Kindstrom
 */
@Entity
@Table(name = "FoodItems")
public class FoodItem extends Model {

    @Id private long id;

    @Column(nullable = false) private final String name;
    private String scientificName;
    @Column(unique = true) private final int lmvFoodNumber;
    private String lmvProject;

    /* Used to convert between 100g and 100ml (1 dl) */
    public Double densityConstant;

    /* Data used when matching web strings */
    public String example;
    public String screenName;
    @DbArray(length = 255) public List<String> searchTags;

    private Float energyKcal;
    private Float energyKj;
    @Column(name = "carbohydrates_g") private Float carbohydrates;
    @Column(name = "protein_g") private Float protein;
    @Column(name = "fibre_g") private Float fibre;
    @Column(name = "whole_grain_g") private Float wholeGrain;
    @Column(name = "cholesterol_mg") private Float cholesterol;
    @Column(name = "water_g") private Float water;
    @Column(name = "alcohol_g") private Float alcohol;
    @Column(name = "ash_g") private Float ash;
    @Column(name = "waste_percent") private Float waste;

    @Embedded private Sugars sugars;
    @Embedded private Fats fats;
    @Embedded private Vitamins vitamins;
    @Embedded private Minerals minerals;

    @ManyToMany(cascade = CascadeType.ALL) @JsonManagedReference public Set<FoodGroup> groups;

    @ManyToOne(cascade = CascadeType.PERSIST) @JsonManagedReference public FoodSource source;

    @ManyToOne public LangualTerm partOfPlantOrAnimal;
    @ManyToOne public LangualTerm physicalForm;
    @ManyToOne public LangualTerm heatTreatment;
    @ManyToOne public LangualTerm cookingMethod;
    @ManyToOne public LangualTerm industrialProcess;
    @ManyToOne public LangualTerm preservationMethod;
    @ManyToOne public LangualTerm packingMedium;
    @ManyToOne public LangualTerm packingType;
    @ManyToOne public LangualTerm packingMaterial;
    @ManyToOne public LangualTerm labelClaim;
    @ManyToOne public LangualTerm geographicSource;
    @ManyToOne public LangualTerm distinctiveFeatures;
    
    public FoodItem(String name, int lmvFoodNumber) {
        this.name = name;
        this.lmvFoodNumber = lmvFoodNumber;
    }

    public FoodItem(String name, String scientificName, int lmvFoodNumber, String lmvProject,
        Float energyKcal, Float energyKj, Float carbohydrates, Float protein, Float fibre,
        Float wholeGrain, Float cholesterol, Float water, Float alcohol, Float ash, Float waste,
        Sugars sugars, Fats fats, Vitamins vitamins, Minerals minerals) {
        this.name = name;
        this.scientificName = scientificName;
        this.lmvFoodNumber = lmvFoodNumber;
        this.lmvProject = lmvProject;
        this.energyKcal = energyKcal;
        this.energyKj = energyKj;
        this.carbohydrates = carbohydrates;
        this.protein = protein;
        this.fibre = fibre;
        this.wholeGrain = wholeGrain;
        this.cholesterol = cholesterol;
        this.water = water;
        this.alcohol = alcohol;
        this.ash = ash;
        this.waste = waste;
        this.sugars = sugars;
        this.fats = fats;
        this.vitamins = vitamins;
        this.minerals = minerals;
    }

    public static Finder<Long, FoodItem> find = new Finder<>(FoodItem.class);

    public long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getScientificName() {
        return scientificName;
    }
    public int getLmvFoodNumber() {
        return lmvFoodNumber;
    }
    public String getLmvProject() {
        return lmvProject;
    }
    public Float getEnergyKcal() {
        return energyKcal;
    }
    public Float getEnergyKj() {
        return energyKj;
    }
    public Float getCarbohydrates() {
        return carbohydrates;
    }
    public Float getProtein() {
        return protein;
    }
    public Float getFibre() {
        return fibre;
    }
    public Float getWholeGrain() {
        return wholeGrain;
    }
    public Float getCholesterol() {
        return cholesterol;
    }
    public Float getWater() {
        return water;
    }
    public Float getAlcohol() {
        return alcohol;
    }
    public Float getAsh() {
        return ash;
    }
    public Float getWaste() {
        return waste;
    }
    public Sugars getSugars() {
        return sugars;
    }
    public Fats getFats() {
        return fats;
    }
    public Vitamins getVitamins() {
        return vitamins;
    }
    public Minerals getMinerals() {
        return minerals;
    }
}
