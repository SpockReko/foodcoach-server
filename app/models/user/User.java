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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Double getActivityLevel() {
        return activityLevel;
    }

    public void setActivityLevel(Double activityLevel) {
        this.activityLevel = activityLevel;
    }

    public ArrayList<String> getAllergier() {
        return allergier;
    }

    public void setAllergier(ArrayList<String> allergier) {
        this.allergier = allergier;
    }

    public Goal getGoal() {
        return goal;
    }

    public void setGoal(Goal goal) {
        this.goal = goal;
    }

    public Timestamp getDateEntered() {
        return dateEntered;
    }

    public void setDateEntered(Timestamp dateEntered) {
        this.dateEntered = dateEntered;
    }

    public User(long id, String firstName, String lastName, String email, Date birthDate, Sex sex,
                Double weight, Double height, Integer age, Double activityLevel, ArrayList<String> allergier,
                Goal goal, Timestamp dateEntered) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;

        this.email = email;
        this.birthDate = birthDate;
        this.sex = sex;
        this.weight = weight;
        this.height = height;
        this.age = age;
        this.activityLevel = activityLevel;
        this.allergier = allergier;
        this.goal = goal;
        this.dateEntered = dateEntered;
    }

    // TEST FÖR FORM
    public User(String firstName, Sex sex, double activityLevel, double weight, double height, int age, Goal goal){
        this.firstName = firstName;
        this.sex = sex;
        this.activityLevel = activityLevel;
        this.weight = weight;
        this.height = height;
        this.age = age;
        this.goal = goal;
    }

    public static Finder<Long, User> find = new Finder<>(User.class);

    public HashMap<Nutrient, Double> hmap = new HashMap<>();
    public HashMap<Nutrient, Double> overdoseValues = new HashMap<>();

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

    public static User getUserByName2(String name) {
        User user = User.find.where().eq("firstName", name).findUnique();
        if (user != null) {
            return user;
        } else {
            return User.find.where().eq("firstName", "Bob").findUnique();
        }
    }

    public User(String name){

        if(name.equals("Stefan")) {
            this.sex = Sex.MALE;
            this.activityLevel = 1.2;
            this.weight = 102.0;
            this.height = 191.0;
            this.age = 30;
            this.goal = Goal.DECREASE;
            dailyCalories();
            calculateOverdoseValues(age);
        }else if(name.equals("Bob")){
            this.sex = Sex.MALE;
            this.activityLevel = 1.2;
            this.weight = 102.0;
            this.height = 180.0;
            this.age = 76;
            this.goal = Goal.STAY;
            dailyCalories();
            calculateOverdoseValues(age);
        }else if(name.equals("Alice")){
            this.sex = Sex.FEMALE;
            this.activityLevel = 2.0;
            this.weight = 60.0;
            this.height = 160.0;
            this.age = 20;
            this.goal = Goal.STAY;
            dailyCalories();
            calculateOverdoseValues(age);
        }else{
            new User();
        }

    }

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

    private void addAllergier(ArrayList<String> allergier) {
        if(allergier==null){
            this.allergier = new ArrayList<>();
        }else {
            this.allergier = allergier;
        }
    }


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
        if(age<=10){
            getChildRDI(age);
        }else if(sex.equals(Sex.MALE)){
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




