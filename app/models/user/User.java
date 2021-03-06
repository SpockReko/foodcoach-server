package models.user;

import com.avaje.ebean.Model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.*;

import static models.user.User.Sex.FEMALE;

import helpers.Constants;
import models.food.Nutrient;


/**
 * Created by Stolof on 17/02/17.
 *
 *
 */

@Entity
@Table(name = "Users")
public class User extends Model {

    /**
     * Ska möjligen ta bort lastname, email och andra onödiga saker vi inte kommer använda
     * för redovisinen / denna iteration.
     *
     */

    @Id public long id;

    public String firstName;
    public String lastName;
    public String email;
    public Date birthDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false) public Sex sex;
    @Column(nullable = false) public Double weight;
    @Column(nullable = false) public Double height;
    @Column(nullable = false) public Integer age;
    @Column(nullable = false) public Double activityLevel;
    @OneToMany public ArrayList<String> allergier = new ArrayList<String>();
    //@Column(nullable = false) public Double goal;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false) public Goal goal;

    public Timestamp dateEntered;



    public enum Sex {
        MALE, FEMALE
    }

    public enum Goal {
        DECREASE(-500), STAY(0), INCREASE(500);

        private final int value;

        Goal(int value) {
            this.value = value;
        }

        public int getGoal() {
            return value;
        }
    }

    /**
     * Osäker om dessa setters och getters behövs.
     *
     *
     */

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     * Skapar Hmap
     *
     *
     */

    public static Finder<Long, User> find = new Finder<>(User.class);

    public HashMap<Nutrient, Double> hmap = new HashMap<>();
    public HashMap<Nutrient, Double> overdoseValues = new HashMap<>();

    /**
     * En Defualt som inte används längre.
     *
     *
     */

     public User() {
        firstName = "user";
        hmap.put(Nutrient.ENERGY_KCAL, 2000D);
        hmap.put(Nutrient.ENERGY_KJ, 2000D* Constants.KCAL_FACTOR);
        hmap.put(Nutrient.PROTEIN, 0.15*hmap.get(Nutrient.ENERGY_KCAL)/4);
        hmap.put(Nutrient.CARBOHYDRATES, 0.55*hmap.get(Nutrient.ENERGY_KCAL)/4);
        hmap.put(Nutrient.FAT, 0.30*hmap.get(Nutrient.ENERGY_KCAL)/9);
        hmap.put(Nutrient.FIBRE, 30D);

        hmap.put(Nutrient.VITAMIN_A, 800D);
        hmap.put(Nutrient.VITAMIN_D, 20D);
        hmap.put(Nutrient.VITAMIN_E, 9D);
        hmap.put(Nutrient.THIAMINE, 1.2D);
        hmap.put(Nutrient.RIBOFLAVIN, 1.35D);
        hmap.put(Nutrient.NIACIN, 16D);
        hmap.put(Nutrient.VITAMIN_B6, 1.4D);
        hmap.put(Nutrient.FOLATE, 300D);
        hmap.put(Nutrient.VITAMIN_B12, 2D);
        hmap.put(Nutrient.VITAMIN_C, 75D);
        hmap.put(Nutrient.CALCIUM, 800D);
        hmap.put(Nutrient.PHOSPHORUS, 600D);
        hmap.put(Nutrient.POTASSIUM, 3300D);
        hmap.put(Nutrient.MAGNESIUM, 315D);
        hmap.put(Nutrient.IRON, 9D);
        hmap.put(Nutrient.ZINC, 8D);
        hmap.put(Nutrient.IODINE, 150D);
        hmap.put(Nutrient.SELENIUM, 55D);

        calculateOverdoseValues(30);
    }



    /**
     * "skapande av en user" lägga in alla behov osv i en hmap.
     *
     *
     */

        public User(String name){
        User user = User.find.where().eq("firstName", name).findUnique();
        if(user != null) {
            this.firstName = user.firstName;
            this.sex = user.sex;
            this.activityLevel = user.activityLevel;
            this.weight = user.weight;
            this.height = user.height;
            this.age = user.age;
            this.goal = user.goal;
            dailyCalories();
            calculateOverdoseValues(age);
        }
         else {
             //new User();
            this.firstName = ("Stefan");
            this.sex = Sex.MALE;
            this.activityLevel = 1.2;
            this.weight = 102.0;
            this.height = 191.0;
            this.age = 30;
            this.goal = Goal.DECREASE;
            dailyCalories();
            calculateOverdoseValues(age);
         }
        }


    /**
     * Också onödig, (något i receptoptimeringen som försöker skapa en standard user?)
     * Funkar getRDI och annat?
     *
     */

    public User(Sex sex, double activityLevel, double weight, double height, int age, Goal goal, ArrayList<String> allergier){
        this.sex = sex;
        this.activityLevel = activityLevel;
        this.weight = weight;
        this.height = height;
        this.age = age;
        this.goal = goal;

        addAllergier(allergier);
        dailyCalories();
        calculateOverdoseValues(age);
    }

    /**
     * Allergier, används inte för tillfället.
     *
     *
     */

    private void addAllergier(ArrayList<String> allergier) {
        if(allergier==null){
            this.allergier = new ArrayList<>();
        }else {
            this.allergier = allergier;
        }
    }

    /**
     *
     * sätter in alla värden i hmap
     *
     */

    private void dailyCalories() {
        double dc;

        if (sex == FEMALE) {
            dc = (activityLevel * (655.0955 + (9.5634 * weight) + (1.8496 * height) - (4.6756 * age)) + goal.getGoal());
        }else{
            dc = (activityLevel * (66.5 + (13.7516 * weight) + (5.0033 * height) - (6.7550 * age)) + goal.getGoal());
        }

        double proteinNeed = 0.15 * dc / 4;
        double fatNeed = 0.3 * dc / 9;
        double carbohydratesNeed = 0.55 * dc / 4;

        hmap.put(Nutrient.ENERGY_KCAL, dc);
        hmap.put(Nutrient.ENERGY_KJ, dc*Constants.KCAL_FACTOR);
        hmap.put(Nutrient.PROTEIN, proteinNeed);
        hmap.put(Nutrient.CARBOHYDRATES, carbohydratesNeed);
        hmap.put(Nutrient.FAT, fatNeed);
        hmap.put(Nutrient.FIBRE, 30D);

       getRDI();

    }


    private void getRDI() {
        if(age<=10) getChildRDI(age);
        else if(sex.equals(Sex.MALE)){
            getMaleRDI(age);
        }else if(sex.equals(Sex.FEMALE)){
            getFemaleRDI(age);
        }
    }

    private void getChildRDI(int age) {

        if (age < 1) {
            hmap.put(Nutrient.VITAMIN_A, 300D);
            hmap.put(Nutrient.VITAMIN_D, 10D);
            hmap.put(Nutrient.VITAMIN_E, 3D);
            hmap.put(Nutrient.THIAMINE, 0.4D);
            hmap.put(Nutrient.RIBOFLAVIN, 0.5D);
            hmap.put(Nutrient.NIACIN, 5D);
            hmap.put(Nutrient.VITAMIN_B6, 0.4D);
            hmap.put(Nutrient.FOLATE, 50D);
            hmap.put(Nutrient.VITAMIN_B12, 0.5D);
            hmap.put(Nutrient.VITAMIN_C, 20D);
            hmap.put(Nutrient.CALCIUM, 540D);
            hmap.put(Nutrient.PHOSPHORUS, 420D);
            hmap.put(Nutrient.POTASSIUM, 1100D);
            hmap.put(Nutrient.MAGNESIUM, 80D);
            hmap.put(Nutrient.IRON, 8D);
            hmap.put(Nutrient.ZINC, 5D);
            hmap.put(Nutrient.IODINE, 50D);
            hmap.put(Nutrient.SELENIUM, 15D);



        } else if (age < 2) {

            hmap.put(Nutrient.VITAMIN_A, 300D);
            hmap.put(Nutrient.VITAMIN_D, 10D);
            hmap.put(Nutrient.VITAMIN_E, 4D);
            hmap.put(Nutrient.THIAMINE, 0.5D);
            hmap.put(Nutrient.RIBOFLAVIN, 0.6D);
            hmap.put(Nutrient.NIACIN, 7D);
            hmap.put(Nutrient.VITAMIN_B6, 0.5D);
            hmap.put(Nutrient.FOLATE, 60D);
            hmap.put(Nutrient.VITAMIN_B12, 0.6D);
            hmap.put(Nutrient.VITAMIN_C, 25D);
            hmap.put(Nutrient.CALCIUM, 600D);
            hmap.put(Nutrient.PHOSPHORUS, 470D);
            hmap.put(Nutrient.POTASSIUM, 1400D);
            hmap.put(Nutrient.MAGNESIUM, 85D);
            hmap.put(Nutrient.IRON, 8D);
            hmap.put(Nutrient.ZINC, 5D);
            hmap.put(Nutrient.IODINE, 70D);
            hmap.put(Nutrient.SELENIUM, 20D);


        } else if (age < 5) {

            hmap.put(Nutrient.VITAMIN_A, 350D);
            hmap.put(Nutrient.VITAMIN_D, 10D);
            hmap.put(Nutrient.VITAMIN_E, 5D);
            hmap.put(Nutrient.THIAMINE, 0.6D);
            hmap.put(Nutrient.RIBOFLAVIN, 0.7D);
            hmap.put(Nutrient.NIACIN, 9D);
            hmap.put(Nutrient.VITAMIN_B6, 0.7D);
            hmap.put(Nutrient.FOLATE, 80D);
            hmap.put(Nutrient.VITAMIN_B12, 0.8D);
            hmap.put(Nutrient.VITAMIN_C, 30D);
            hmap.put(Nutrient.CALCIUM, 600D);
            hmap.put(Nutrient.PHOSPHORUS, 470D);
            hmap.put(Nutrient.POTASSIUM, 1800D);
            hmap.put(Nutrient.MAGNESIUM, 120D);
            hmap.put(Nutrient.IRON, 8D);
            hmap.put(Nutrient.ZINC, 6D);
            hmap.put(Nutrient.IODINE, 90D);
            hmap.put(Nutrient.SELENIUM, 25D);


        } else if (age <= 10) {

            hmap.put(Nutrient.VITAMIN_A, 400D);
            hmap.put(Nutrient.VITAMIN_D, 10D);
            hmap.put(Nutrient.VITAMIN_E, 6D);
            hmap.put(Nutrient.THIAMINE, 0.9D);
            hmap.put(Nutrient.RIBOFLAVIN, 1.1D);
            hmap.put(Nutrient.NIACIN, 12D);
            hmap.put(Nutrient.VITAMIN_B6, 1D);
            hmap.put(Nutrient.FOLATE, 130D);
            hmap.put(Nutrient.VITAMIN_B12, 1.3D);
            hmap.put(Nutrient.VITAMIN_C, 40D);
            hmap.put(Nutrient.CALCIUM, 700D);
            hmap.put(Nutrient.PHOSPHORUS, 540D);
            hmap.put(Nutrient.POTASSIUM, 2000D);
            hmap.put(Nutrient.MAGNESIUM, 200D);
            hmap.put(Nutrient.IRON, 9D);
            hmap.put(Nutrient.ZINC, 7D);
            hmap.put(Nutrient.IODINE, 120D);
            hmap.put(Nutrient.SELENIUM, 30D);
        }

    }

    private void getMaleRDI(int age) {

        if (age >= 10 && age < 13) {

            hmap.put(Nutrient.VITAMIN_A, 600D);
            hmap.put(Nutrient.VITAMIN_D, 10D);
            hmap.put(Nutrient.VITAMIN_E, 8D);
            hmap.put(Nutrient.THIAMINE, 1.1D);
            hmap.put(Nutrient.RIBOFLAVIN, 1.3D);
            hmap.put(Nutrient.NIACIN, 15D);
            hmap.put(Nutrient.VITAMIN_B6, 1.3D);
            hmap.put(Nutrient.FOLATE, 200D);
            hmap.put(Nutrient.VITAMIN_B12, 2D);
            hmap.put(Nutrient.VITAMIN_C, 50D);
            hmap.put(Nutrient.CALCIUM, 900D);
            hmap.put(Nutrient.PHOSPHORUS, 700D);
            hmap.put(Nutrient.POTASSIUM, 3300D);
            hmap.put(Nutrient.MAGNESIUM, 280D);
            hmap.put(Nutrient.IRON, 11D);
            hmap.put(Nutrient.ZINC, 11D);
            hmap.put(Nutrient.IODINE, 150D);
            hmap.put(Nutrient.SELENIUM, 40D);


        } else if (age < 17) {

            hmap.put(Nutrient.VITAMIN_A, 900D);
            hmap.put(Nutrient.VITAMIN_D, 10D);
            hmap.put(Nutrient.VITAMIN_E, 10D);
            hmap.put(Nutrient.THIAMINE, 1.4D);
            hmap.put(Nutrient.RIBOFLAVIN, 1.7D);
            hmap.put(Nutrient.NIACIN, 19D);
            hmap.put(Nutrient.VITAMIN_B6, 1.6D);
            hmap.put(Nutrient.FOLATE, 300D);
            hmap.put(Nutrient.VITAMIN_B12, 2D);
            hmap.put(Nutrient.VITAMIN_C, 75D);
            hmap.put(Nutrient.CALCIUM, 900D);
            hmap.put(Nutrient.PHOSPHORUS, 700D);
            hmap.put(Nutrient.POTASSIUM, 3500D);
            hmap.put(Nutrient.MAGNESIUM, 350D);
            hmap.put(Nutrient.IRON, 11D);
            hmap.put(Nutrient.ZINC, 12D);
            hmap.put(Nutrient.IODINE, 150D);
            hmap.put(Nutrient.SELENIUM, 60D);


        } else if (age < 30) {

            hmap.put(Nutrient.VITAMIN_A, 900D);
            hmap.put(Nutrient.VITAMIN_D, 10D);
            hmap.put(Nutrient.VITAMIN_E, 10D);
            hmap.put(Nutrient.THIAMINE, 1.4D);
            hmap.put(Nutrient.RIBOFLAVIN, 1.6D);
            hmap.put(Nutrient.NIACIN, 19D);
            hmap.put(Nutrient.VITAMIN_B6, 1.6D);
            hmap.put(Nutrient.FOLATE, 300D);
            hmap.put(Nutrient.VITAMIN_B12, 2D);
            hmap.put(Nutrient.VITAMIN_C, 75D);
            hmap.put(Nutrient.CALCIUM, 800D);
            hmap.put(Nutrient.PHOSPHORUS, 600D);
            hmap.put(Nutrient.POTASSIUM, 3500D);
            hmap.put(Nutrient.MAGNESIUM, 350D);
            hmap.put(Nutrient.IRON, 9D);
            hmap.put(Nutrient.ZINC, 9D);
            hmap.put(Nutrient.IODINE, 150D);
            hmap.put(Nutrient.SELENIUM, 60D);


        } else if (age < 60) {

            hmap.put(Nutrient.VITAMIN_A, 900D);
            hmap.put(Nutrient.VITAMIN_D, 10D);
            hmap.put(Nutrient.VITAMIN_E, 10D);
            hmap.put(Nutrient.THIAMINE, 1.3D);
            hmap.put(Nutrient.RIBOFLAVIN, 1.5D);
            hmap.put(Nutrient.NIACIN, 18D);
            hmap.put(Nutrient.VITAMIN_B6, 1.6D);
            hmap.put(Nutrient.FOLATE, 300D);
            hmap.put(Nutrient.VITAMIN_B12, 2D);
            hmap.put(Nutrient.VITAMIN_C, 75D);
            hmap.put(Nutrient.CALCIUM, 800D);
            hmap.put(Nutrient.PHOSPHORUS, 600D);
            hmap.put(Nutrient.POTASSIUM, 3500D);
            hmap.put(Nutrient.MAGNESIUM, 350D);
            hmap.put(Nutrient.IRON, 9D);
            hmap.put(Nutrient.ZINC, 9D);
            hmap.put(Nutrient.IODINE, 150D);
            hmap.put(Nutrient.SELENIUM, 60D);


        } else if (age < 74) {

            hmap.put(Nutrient.VITAMIN_A, 900D);
            hmap.put(Nutrient.VITAMIN_D, 10D);
            hmap.put(Nutrient.VITAMIN_E, 10D);
            hmap.put(Nutrient.THIAMINE, 1.2D);
            hmap.put(Nutrient.RIBOFLAVIN, 1.4D);
            hmap.put(Nutrient.NIACIN, 16D);
            hmap.put(Nutrient.VITAMIN_B6, 1.6D);
            hmap.put(Nutrient.FOLATE, 300D);
            hmap.put(Nutrient.VITAMIN_B12, 2D);
            hmap.put(Nutrient.VITAMIN_C, 75D);
            hmap.put(Nutrient.CALCIUM, 800D);
            hmap.put(Nutrient.PHOSPHORUS, 600D);
            hmap.put(Nutrient.POTASSIUM, 3500D);
            hmap.put(Nutrient.MAGNESIUM, 350D);
            hmap.put(Nutrient.IRON, 9D);
            hmap.put(Nutrient.ZINC, 9D);
            hmap.put(Nutrient.IODINE, 150D);
            hmap.put(Nutrient.SELENIUM, 60D);


        } else if (age >= 74) {

            hmap.put(Nutrient.VITAMIN_A, 900D);
            hmap.put(Nutrient.VITAMIN_D, 20D);
            hmap.put(Nutrient.VITAMIN_E, 10D);
            hmap.put(Nutrient.THIAMINE, 1.2D);
            hmap.put(Nutrient.RIBOFLAVIN, 1.3D);
            hmap.put(Nutrient.NIACIN, 15D);
            hmap.put(Nutrient.VITAMIN_B6, 1.6D);
            hmap.put(Nutrient.FOLATE, 300D);
            hmap.put(Nutrient.VITAMIN_B12, 2D);
            hmap.put(Nutrient.VITAMIN_C, 75D);
            hmap.put(Nutrient.CALCIUM, 800D);
            hmap.put(Nutrient.PHOSPHORUS, 600D);
            hmap.put(Nutrient.POTASSIUM, 3500D);
            hmap.put(Nutrient.MAGNESIUM, 350D);
            hmap.put(Nutrient.IRON, 9D);
            hmap.put(Nutrient.ZINC, 9D);
            hmap.put(Nutrient.IODINE, 150D);
            hmap.put(Nutrient.SELENIUM, 60D);

        }
    }

    private void getFemaleRDI(int age) {

        if (age >= 10 && age < 13) {

            hmap.put(Nutrient.VITAMIN_A, 600D);
            hmap.put(Nutrient.VITAMIN_D, 10D);
            hmap.put(Nutrient.VITAMIN_E, 7D);
            hmap.put(Nutrient.THIAMINE, 1D);
            hmap.put(Nutrient.RIBOFLAVIN, 1.2D);
            hmap.put(Nutrient.NIACIN, 14D);
            hmap.put(Nutrient.VITAMIN_B6, 1.1D);
            hmap.put(Nutrient.FOLATE, 300D);
            hmap.put(Nutrient.VITAMIN_B12, 2D);
            hmap.put(Nutrient.VITAMIN_C, 50D);
            hmap.put(Nutrient.CALCIUM, 900D);
            hmap.put(Nutrient.PHOSPHORUS, 700D);
            hmap.put(Nutrient.POTASSIUM, 2900D);
            hmap.put(Nutrient.MAGNESIUM, 280D);
            hmap.put(Nutrient.IRON, 11D);
            hmap.put(Nutrient.ZINC, 8D);
            hmap.put(Nutrient.IODINE, 150D);
            hmap.put(Nutrient.SELENIUM, 40D);


        } else if (age < 17) {

            hmap.put(Nutrient.VITAMIN_A, 900D);
            hmap.put(Nutrient.VITAMIN_D, 10D);
            hmap.put(Nutrient.VITAMIN_E, 8D);
            hmap.put(Nutrient.THIAMINE, 1.2D);
            hmap.put(Nutrient.RIBOFLAVIN, 1.4D);
            hmap.put(Nutrient.NIACIN, 16D);
            hmap.put(Nutrient.VITAMIN_B6, 1.3D);
            hmap.put(Nutrient.FOLATE, 300D);
            hmap.put(Nutrient.VITAMIN_B12, 2D);
            hmap.put(Nutrient.VITAMIN_C, 75D);
            hmap.put(Nutrient.CALCIUM, 900D);
            hmap.put(Nutrient.PHOSPHORUS, 700D);
            hmap.put(Nutrient.POTASSIUM, 3100D);
            hmap.put(Nutrient.MAGNESIUM, 280D);
            hmap.put(Nutrient.IRON, 15D);
            hmap.put(Nutrient.ZINC, 7D);
            hmap.put(Nutrient.IODINE, 150D);
            hmap.put(Nutrient.SELENIUM, 50D);

        } else if (age < 30) {

            hmap.put(Nutrient.VITAMIN_A, 700D);
            hmap.put(Nutrient.VITAMIN_D, 10D);
            hmap.put(Nutrient.VITAMIN_E, 8D);
            hmap.put(Nutrient.THIAMINE, 1.1D);
            hmap.put(Nutrient.RIBOFLAVIN, 1.3D);
            hmap.put(Nutrient.NIACIN, 15D);
            hmap.put(Nutrient.VITAMIN_B6, 1.3D);
            hmap.put(Nutrient.FOLATE, 400D);
            hmap.put(Nutrient.VITAMIN_B12, 2D);
            hmap.put(Nutrient.VITAMIN_C, 75D);
            hmap.put(Nutrient.CALCIUM, 800D);
            hmap.put(Nutrient.PHOSPHORUS, 600D);
            hmap.put(Nutrient.POTASSIUM, 3100D);
            hmap.put(Nutrient.MAGNESIUM, 280D);
            hmap.put(Nutrient.IRON, 15D);
            hmap.put(Nutrient.ZINC, 7D);
            hmap.put(Nutrient.IODINE, 150D);
            hmap.put(Nutrient.SELENIUM, 50D);


        } else if (age < 60) {

            hmap.put(Nutrient.VITAMIN_A, 700D);
            hmap.put(Nutrient.VITAMIN_D, 10D);
            hmap.put(Nutrient.VITAMIN_E, 8D);
            hmap.put(Nutrient.THIAMINE, 1.1D);
            hmap.put(Nutrient.RIBOFLAVIN, 1.2D);
            hmap.put(Nutrient.NIACIN, 14D);
            hmap.put(Nutrient.VITAMIN_B6, 1.2D);
            hmap.put(Nutrient.FOLATE, 400D);
            hmap.put(Nutrient.VITAMIN_B12, 2D);
            hmap.put(Nutrient.VITAMIN_C, 75D);
            hmap.put(Nutrient.CALCIUM, 800D);
            hmap.put(Nutrient.PHOSPHORUS, 600D);
            hmap.put(Nutrient.POTASSIUM, 3100D);
            hmap.put(Nutrient.MAGNESIUM, 280D);
            hmap.put(Nutrient.IRON, 15D);
            hmap.put(Nutrient.ZINC, 7D);
            hmap.put(Nutrient.IODINE, 150D);
            hmap.put(Nutrient.SELENIUM, 50D);


        } else if (age < 74) {

            hmap.put(Nutrient.VITAMIN_A, 700D);
            hmap.put(Nutrient.VITAMIN_D, 10D);
            hmap.put(Nutrient.VITAMIN_E, 8D);
            hmap.put(Nutrient.THIAMINE, 1D);
            hmap.put(Nutrient.RIBOFLAVIN, 1.2D);
            hmap.put(Nutrient.NIACIN, 13D);
            hmap.put(Nutrient.VITAMIN_B6, 1.2D);
            hmap.put(Nutrient.FOLATE, 300D);
            hmap.put(Nutrient.VITAMIN_B12, 2D);
            hmap.put(Nutrient.VITAMIN_C, 75D);
            hmap.put(Nutrient.CALCIUM, 800D);
            hmap.put(Nutrient.PHOSPHORUS, 600D);
            hmap.put(Nutrient.POTASSIUM, 3100D);
            hmap.put(Nutrient.MAGNESIUM, 280D);
            hmap.put(Nutrient.IRON, 9D);
            hmap.put(Nutrient.ZINC, 7D);
            hmap.put(Nutrient.IODINE, 150D);
            hmap.put(Nutrient.SELENIUM, 50D);


        } else if (age >= 74) {

            hmap.put(Nutrient.VITAMIN_A, 700D);
            hmap.put(Nutrient.VITAMIN_D, 20D);
            hmap.put(Nutrient.VITAMIN_E, 8D);
            hmap.put(Nutrient.THIAMINE, 1D);
            hmap.put(Nutrient.RIBOFLAVIN, 1.2D);
            hmap.put(Nutrient.NIACIN, 13D);
            hmap.put(Nutrient.VITAMIN_B6, 1.2D);
            hmap.put(Nutrient.FOLATE, 300D);
            hmap.put(Nutrient.VITAMIN_B12, 2D);
            hmap.put(Nutrient.VITAMIN_C, 75D);
            hmap.put(Nutrient.CALCIUM, 800D);
            hmap.put(Nutrient.PHOSPHORUS, 600D);
            hmap.put(Nutrient.POTASSIUM, 3100D);
            hmap.put(Nutrient.MAGNESIUM, 280D);
            hmap.put(Nutrient.IRON, 9D);
            hmap.put(Nutrient.ZINC, 7D);
            hmap.put(Nutrient.IODINE, 150D);
            hmap.put(Nutrient.SELENIUM, 50D);

        }
    }

    private void calculateOverdoseValues(int age) {

        overdoseValues.put(Nutrient.ENERGY_KCAL,hmap.get(Nutrient.ENERGY_KCAL)*1.2D);
        overdoseValues.put(Nutrient.PROTEIN,hmap.get(Nutrient.PROTEIN)*2D);
        overdoseValues.put(Nutrient.CARBOHYDRATES,hmap.get(Nutrient.CARBOHYDRATES)*2D);
        overdoseValues.put(Nutrient.FAT,hmap.get(Nutrient.FAT)*2D);
        overdoseValues.put(Nutrient.FIBRE,hmap.get(Nutrient.FIBRE)*2D);

        overdoseValues.put(Nutrient.VITAMIN_A,7500D);
        overdoseValues.put(Nutrient.VITAMIN_D,100D);
        overdoseValues.put(Nutrient.VITAMIN_E,Double.POSITIVE_INFINITY);
        overdoseValues.put(Nutrient.THIAMINE,500D);
        overdoseValues.put(Nutrient.RIBOFLAVIN,300D);
        overdoseValues.put(Nutrient.NIACIN,50D);
        overdoseValues.put(Nutrient.VITAMIN_B6,200D);
        overdoseValues.put(Nutrient.FOLATE,Double.POSITIVE_INFINITY);
        overdoseValues.put(Nutrient.VITAMIN_B12,Double.POSITIVE_INFINITY);
        overdoseValues.put(Nutrient.VITAMIN_C,Double.POSITIVE_INFINITY);
        overdoseValues.put(Nutrient.CALCIUM,2500D);
        overdoseValues.put(Nutrient.PHOSPHORUS,4000D);
        overdoseValues.put(Nutrient.POTASSIUM,15000D);
        overdoseValues.put(Nutrient.MAGNESIUM,1000D);
        overdoseValues.put(Nutrient.IRON,100D);
        overdoseValues.put(Nutrient.IODINE,10000D);
        overdoseValues.put(Nutrient.SELENIUM,300D);

        if(age <= 10) {
            overdoseValues.put(Nutrient.VITAMIN_A,200D*weight);
            overdoseValues.put(Nutrient.VITAMIN_D,50D);
        }
    }

}




