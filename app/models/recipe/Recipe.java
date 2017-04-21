package models.recipe;

import com.avaje.ebean.Model;
import models.food.Food;
import models.food.Nutrient;
import models.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * A static recipe most likely parsed and matched with ingredients from the web.
 * Contains wrapper methods that returns the total nutrient data for all ingredients in a recipe.
 *
 * @author Fredrik Kindstrom
 */
@Entity
@Table(name = "Recipes")
public class Recipe extends Model {

    @Id private long id;

    @NotNull private final String title;
    @NotNull private final int portions;
    public String description;
    public int cookingDurationMinutes;

    @ManyToMany(cascade = CascadeType.ALL) public List<Ingredient> ingredients;

    public String sourceUrl;

    public Recipe(String title, int portions, List<Ingredient> ingredients) {
        this.title = title;
        this.portions = portions;
        this.ingredients = ingredients;
    }

    public static Finder<Long, Recipe> find = new Finder<>(Recipe.class);

    public String getTitle() {
        return title;
    }
    public int getPortions() {
        return portions;
    }
    public List<Ingredient> getIngredients(){return ingredients;}

    public Double getCO2() {
        return ingredients.stream().mapToDouble(Ingredient::getCO2).sum();
    }
    public Double getEnergyKcal() {
        return ingredients.stream().mapToDouble(Ingredient::getEnergyKcal).sum();
    }
    public Double getEnergyKj() {
        return ingredients.stream().mapToDouble(Ingredient::getEnergyKj).sum();
    }
    public Double getCarbohydrates() {
        return ingredients.stream().mapToDouble(Ingredient::getCarbohydrates).sum();
    }
    public Double getProtein() {
        return ingredients.stream().mapToDouble(Ingredient::getProtein).sum();
    }
    public Double getFat() {
        return ingredients.stream().mapToDouble(Ingredient::getFat).sum();
    }
    public Double getFibre() {
        return ingredients.stream().mapToDouble(Ingredient::getFibre).sum();
    }
    public Double getAlcohol() {
        return ingredients.stream().mapToDouble(Ingredient::getAlcohol).sum();
    }

    /*
    Extra nutrition data
     */
    public Double getVitaminA() {
        return ingredients.stream().mapToDouble(Ingredient::getVitaminA).sum();
    }
    public Double getVitaminB6() {
        return ingredients.stream().mapToDouble(Ingredient::getVitaminB6).sum();
    }
    public Double getVitaminB12() {
        return ingredients.stream().mapToDouble(Ingredient::getVitaminB12).sum();
    }
    public Double getVitaminC() {
        return ingredients.stream().mapToDouble(Ingredient::getVitaminC).sum();
    }
    public Double getVitaminD() {
        return ingredients.stream().mapToDouble(Ingredient::getVitaminD).sum();
    }
    public Double getVitaminE() {
        return ingredients.stream().mapToDouble(Ingredient::getVitaminE).sum();
    }
    public Double getThiamine() {
        return ingredients.stream().mapToDouble(Ingredient::getThiamine).sum();
    }
    public Double getRiboflavin() {
        return ingredients.stream().mapToDouble(Ingredient::getRiboflavin).sum();
    }
    public Double getNiacin() {
        return ingredients.stream().mapToDouble(Ingredient::getNiacin).sum();
    }
    public Double getNiacinEquivalents() {
        return ingredients.stream().mapToDouble(Ingredient::getNiacinEquivalents).sum();
    }
    public Double getFolate() {
        return ingredients.stream().mapToDouble(Ingredient::getFolate).sum();
    }
    public Double getPhosphorus() {
        return ingredients.stream().mapToDouble(Ingredient::getPhosphorus).sum();
    }
    public Double getIodine() {
        return ingredients.stream().mapToDouble(Ingredient::getIodine).sum();
    }
    public Double getIron() {
        return ingredients.stream().mapToDouble(Ingredient::getIron).sum();
    }
    public Double getCalcium() {
        return ingredients.stream().mapToDouble(Ingredient::getCalcium).sum();
    }
    public Double getPotassium() {
        return ingredients.stream().mapToDouble(Ingredient::getPotassium).sum();
    }
    public Double getMagnesium() {
        return ingredients.stream().mapToDouble(Ingredient::getMagnesium).sum();
    }
    public Double getSalt() {
        return ingredients.stream().mapToDouble(Ingredient::getSalt).sum();
    }
    public Double getSelenium() {
        return ingredients.stream().mapToDouble(Ingredient::getSelenium).sum();
    }
    public Double getZink() {
        return ingredients.stream().mapToDouble(Ingredient::getZink).sum();
    }


    /*
    Extra calculations
     */
    public Double getEnergyPercentProtein() {
        return 4*100*getProtein()/getEnergyKcal(); // energi från protein per portion
    }
    public Double getEnergyPercentCarbohydrates() {
        return 4*100*getCarbohydrates()/getEnergyKcal(); // energi från kolhydrater per portion
    }
    public Double getEnergyPercentFat() {
        return 9*100*getFat()/getEnergyKcal(); // energi från fett per portion
    }
    public Double getEnergyPercentFibre() {
        return 2*100*getFibre()/getEnergyKcal();
    }

    public Double getKcalPerPortion() {
        return getEnergyKcal()/getPortions();
    }

    public Recipe getOnePortionRecipe(){
        List<Ingredient> ingredients = getIngredients();
        List<Ingredient> newIngredients = new ArrayList<>();
        for (Ingredient i: ingredients) {
            double onePortionValue = i.getAmount().getAmount()/portions;
            Amount.Unit currentUnit = i.getAmount().getUnit();
            Amount newAmount = new Amount(onePortionValue,currentUnit);
            Food currentFood = i.getFood();
            Ingredient newIngredient = new Ingredient(currentFood, newAmount);
            newIngredients.add(newIngredient);
        }
        Recipe recepie = new Recipe(this.getTitle(),1,newIngredients);
        return recepie;
    }
    public Recipe getUserRecipe(User user){
        List<Ingredient> ingredients = getIngredients();
        List<Ingredient> newIngredients = new ArrayList<>();
        double div=user.hmap.get(Nutrient.KCAL)/this.getEnergyKcal()*0.3;
        for (Ingredient i: ingredients) {
            double onePortionValue = i.getAmount().getAmount()*div;
            Amount.Unit currentUnit = i.getAmount().getUnit();
            Amount newAmount = new Amount(onePortionValue,currentUnit);
            Food currentFood = i.getFood();
            Ingredient newIngredient = new Ingredient(currentFood, newAmount);
            newIngredients.add(newIngredient);
        }
        Recipe recepie = new Recipe(this.getTitle(),1,newIngredients);
        return recepie;
    }

    public String recipeToString(Recipe recipe){
        String text = recipe.getTitle()+"\n\n";
        for( Ingredient i : recipe.ingredients ){
            text += i.getFood().name;
            int stringLength = i.getFood().name.length();
            if(stringLength<8) {
                text += "\t\t\t\t\t";
            } else if(stringLength>=8 && stringLength<16) {
                text += "\t\t\t\t";
            } else if(stringLength>=16 && stringLength<24) {
                text += "\t\t\t";
            } else if (stringLength>=24 && stringLength <32) {
                text +="\t\t";
            } else {
                text +="\t";
            }
            text += i.getAmount().getAmount()+" "+i.getAmount().getUnit()+"\n";
        }
        text = text + "\n\n";
        return text;
    }

}
