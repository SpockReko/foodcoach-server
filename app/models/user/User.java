package models.user;

import com.avaje.ebean.Model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static models.user.User.Sex.FEMALE;
import static models.user.User.Sex.MALE;


/**
 * Created by Stolof on 17/02/17.
 */

@Entity
@Table(name = "Users")
public class User extends Model {


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
    @Enumerated(EnumType.STRING) public Goal goal;

    public Timestamp dateEntered;
    @Id public long id;

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



    public User(Sex sex, double activityLevel, double weight, double height, int age, Goal goal, ArrayList<String> allergier){
        this.sex = sex;
        this.activityLevel = activityLevel;
        this.weight = weight;
        this.height = height;
        this.age = age;
        this.goal = goal;
        this.allergier = allergier;
    }

    public HashMap<String, Double> hmap = new HashMap<>();

    public void dailyCalori() {
        double dc;

        if (sex == FEMALE) {
            dc = (activityLevel * (655.0955 + (9.5634 * weight) + (1.8496 * height) - (4.6756 * age)) + goal.getGoal());

            double proteinNeed = 0.15 * dc / 4;
            double fatNeed = 0.3 * dc / 9;
            double carbohydratesNeed = 0.55 * dc / 9;

            hmap.put("bmr", dc);
            hmap.put("proteinNeed", proteinNeed);
            hmap.put("carbohydratesNeed", carbohydratesNeed);
            hmap.put("fatNeed", fatNeed);


            if (age < 10)
                getChildRDI(age);
            else
                getFemaleRDI(age);

        }
        if (sex == MALE) {
            dc = (activityLevel * (66.5 + (13.7516 * weight) + (5.0033 * height) - (6.7550 * age)) + goal.getGoal());

            double proteinNeed = 0.15 * dc / 4;
            double fatNeed = 0.3 * dc / 9;
            double carbohydratesNeed = 0.55 * dc / 9;

            hmap.put("bmr", dc);
            hmap.put("proteinNeed", proteinNeed);
            hmap.put("carbohydratesNeed", carbohydratesNeed);
            hmap.put("fatNeed", fatNeed);


            if (age < 10)
                getChildRDI(age);
            else
                getMaleRDI(age);


        }

    }

    private void getChildRDI(int age) {

        if (age < 1) {
            hmap.put("vitaminANeedug", 300D);
            hmap.put("vitaminDNeedug", 10D);
            hmap.put("vitaminENeedmg", 3D);
            hmap.put("tiaminNeedmg", 0.4D);
            hmap.put("riboflavinNeedmg", 0.5D);
            hmap.put("niacinNeedmg", 5D);
            hmap.put("vitaminB6Needmg", 0.4D);
            hmap.put("folateNeedug", 50D);
            hmap.put("vitaminB12Needug", 0.5D);
            hmap.put("vitaminCNeedmg", 20D);
            hmap.put("calciumNeedmg", 540D);
            hmap.put("phosphorusNeedmg", 420D);
            hmap.put("potassiumNeedg", 1.1D);
            hmap.put("magnesiumNeed", 80D);
            hmap.put("ironNeedmg", 8D);
            hmap.put("zinkNeedmg", 5D);
            hmap.put("copperNeedmg", 0.3D);
            hmap.put("iodineNeedug", 50D);
            hmap.put("seleniumNeedug", 15D);


        } else if (age < 2) {

            hmap.put("vitaminANeedug", 300D);
            hmap.put("vitaminDNeedug", 10D);
            hmap.put("vitaminENeedmg", 4D);
            hmap.put("tiaminNeedmg", 0.5D);
            hmap.put("riboflavinNeedmg", 0.6D);
            hmap.put("niacinNeedmg", 7D);
            hmap.put("vitaminB6Needmg", 0.5D);
            hmap.put("folateNeedug", 60D);
            hmap.put("vitaminB12Needug", 0.6D);
            hmap.put("vitaminCNeedmg", 25D);
            hmap.put("calciumNeedmg", 600D);
            hmap.put("phosphorusNeedmg", 470D);
            hmap.put("potassiumNeedg", 1.4D);
            hmap.put("magnesiumNeed", 85D);
            hmap.put("ironNeedmg", 8D);
            hmap.put("zinkNeedmg", 5D);
            hmap.put("copperNeedmg", 0.3D);
            hmap.put("iodineNeedug", 70D);
            hmap.put("seleniumNeedug", 20D);


        } else if (age < 5) {

            hmap.put("vitaminANeedug", 350D);
            hmap.put("vitaminDNeedug", 10D);
            hmap.put("vitaminENeedmg", 5D);
            hmap.put("tiaminNeedmg", 0.6D);
            hmap.put("riboflavinNeedmg", 0.7D);
            hmap.put("niacinNeedmg", 9D);
            hmap.put("vitaminB6Needmg", 0.7D);
            hmap.put("folateNeedug", 80D);
            hmap.put("vitaminB12Needug", 0.8D);
            hmap.put("vitaminCNeedmg", 30D);
            hmap.put("calciumNeedmg", 600D);
            hmap.put("phosphorusNeedmg", 470D);
            hmap.put("potassiumNeedg", 1.8D);
            hmap.put("magnesiumNeed", 120D);
            hmap.put("ironNeedmg", 8D);
            hmap.put("zinkNeedmg", 6D);
            hmap.put("copperNeedmg", 0.4D);
            hmap.put("iodineNeedug", 90D);
            hmap.put("seleniumNeedug", 25D);


        } else if (age <= 10) {

            hmap.put("vitaminANeedug", 400D);
            hmap.put("vitaminDNeedug", 10D);
            hmap.put("vitaminENeedmg", 6D);
            hmap.put("tiaminNeedmg", 0.9D);
            hmap.put("riboflavinNeedmg", 1.1D);
            hmap.put("niacinNeedmg", 12D);
            hmap.put("vitaminB6Needmg", 1D);
            hmap.put("folateNeedug", 130D);
            hmap.put("vitaminB12Needug", 1.3D);
            hmap.put("vitaminCNeedmg", 40D);
            hmap.put("calciumNeedmg", 700D);
            hmap.put("phosphorusNeedmg", 540D);
            hmap.put("potassiumNeedg", 2D);
            hmap.put("magnesiumNeed", 200D);
            hmap.put("ironNeedmg", 9D);
            hmap.put("zinkNeedmg", 7D);
            hmap.put("copperNeedmg", 0.5D);
            hmap.put("iodineNeedug", 120D);
            hmap.put("seleniumNeedug", 30D);


        }

    }


    private void getMaleRDI(int age) {

        if (age >= 10 && age < 13) {

            hmap.put("vitaminANeedug", 600D);
            hmap.put("vitaminDNeedug", 10D);
            hmap.put("vitaminENeedmg", 8D);
            hmap.put("tiaminNeedmg", 1.1D);
            hmap.put("riboflavinNeedmg", 1.3D);
            hmap.put("niacinNeedmg", 15D);
            hmap.put("vitaminB6Needmg", 1.3D);
            hmap.put("folateNeedug", 200D);
            hmap.put("vitaminB12Needug", 2D);
            hmap.put("vitaminCNeedmg", 50D);
            hmap.put("calciumNeedmg", 900D);
            hmap.put("phosphorusNeedmg", 700D);
            hmap.put("potassiumNeedg", 3.3D);
            hmap.put("magnesiumNeedmg", 280D);
            hmap.put("ironNeedmg", 11D);
            hmap.put("zinkNeedmg", 11D);
            hmap.put("copperNeedmg", 0.7D);
            hmap.put("iodineNeedug", 150D);
            hmap.put("seleniumNeedug", 40D);


        } else if (age < 17) {

            hmap.put("vitaminANeedug", 900D);
            hmap.put("vitaminDNeedug", 10D);
            hmap.put("vitaminENeedmg", 10D);
            hmap.put("tiaminNeedmg", 1.4D);
            hmap.put("riboflavinNeedmg", 1.7D);
            hmap.put("niacinNeedmg", 19D);
            hmap.put("vitaminB6Needmg", 1.6D);
            hmap.put("folateNeedug", 300D);
            hmap.put("vitaminB12Needug", 2D);
            hmap.put("vitaminCNeedmg", 75D);
            hmap.put("calciumNeedmg", 900D);
            hmap.put("phosphorusNeedmg", 700D);
            hmap.put("potassiumNeedg", 3.5D);
            hmap.put("magnesiumNeedmg", 350D);
            hmap.put("ironNeedmg", 11D);
            hmap.put("zinkNeedmg", 12D);
            hmap.put("copperNeedmg", 0.9D);
            hmap.put("iodineNeedug", 150D);
            hmap.put("seleniumNeedug", 60D);


        } else if (age < 30) {

            hmap.put("vitaminANeedug", 900D);
            hmap.put("vitaminDNeedug", 10D);
            hmap.put("vitaminENeedmg", 10D);
            hmap.put("tiaminNeedmg", 1.4D);
            hmap.put("riboflavinNeedmg", 1.6D);
            hmap.put("niacinNeedmg", 19D);
            hmap.put("vitaminB6Needmg", 1.6D);
            hmap.put("folateNeedug", 300D);
            hmap.put("vitaminB12Needug", 2D);
            hmap.put("vitaminCNeedmg", 75D);
            hmap.put("calciumNeedmg", 800D);
            hmap.put("phosphorusNeedmg", 600D);
            hmap.put("potassiumNeedg", 3.5D);
            hmap.put("magnesiumNeedmg", 350D);
            hmap.put("ironNeedmg", 9D);
            hmap.put("zinkNeedmg", 9D);
            hmap.put("copperNeedmg", 0.9D);
            hmap.put("iodineNeedug", 150D);
            hmap.put("seleniumNeedug", 60D);


        } else if (age < 60) {

            hmap.put("vitaminANeedug", 900D);
            hmap.put("vitaminDNeedug", 10D);
            hmap.put("vitaminENeedmg", 10D);
            hmap.put("tiaminNeedmg", 1.3D);
            hmap.put("riboflavinNeedmg", 1.5D);
            hmap.put("niacinNeedmg", 18D);
            hmap.put("vitaminB6Needmg", 1.6D);
            hmap.put("folateNeedug", 300D);
            hmap.put("vitaminB12Needug", 2D);
            hmap.put("vitaminCNeedmg", 75D);
            hmap.put("calciumNeedmg", 800D);
            hmap.put("phosphorusNeedmg", 600D);
            hmap.put("potassiumNeedg", 3.5D);
            hmap.put("magnesiumNeedmg", 350D);
            hmap.put("ironNeedmg", 9D);
            hmap.put("zinkNeedmg", 9D);
            hmap.put("copperNeedmg", 0.9D);
            hmap.put("iodineNeedug", 150D);
            hmap.put("seleniumNeedug", 60D);


        } else if (age < 74) {

            hmap.put("vitaminANeedug", 900D);
            hmap.put("vitaminDNeedug", 10D);
            hmap.put("vitaminENeedmg", 10D);
            hmap.put("tiaminNeedmg", 1.2D);
            hmap.put("riboflavinNeedmg", 1.4D);
            hmap.put("niacinNeedmg", 16D);
            hmap.put("vitaminB6Needmg", 1.6D);
            hmap.put("folateNeedug", 300D);
            hmap.put("vitaminB12Needug", 2D);
            hmap.put("vitaminCNeedmg", 75D);
            hmap.put("calciumNeedmg", 800D);
            hmap.put("phosphorusNeedmg", 600D);
            hmap.put("potassiumNeedg", 3.5D);
            hmap.put("magnesiumNeedmg", 350D);
            hmap.put("ironNeedmg", 9D);
            hmap.put("zinkNeedmg", 9D);
            hmap.put("copperNeedmg", 0.9D);
            hmap.put("iodineNeedug", 150D);
            hmap.put("seleniumNeedug", 60D);


        } else if (age >= 74) {

            hmap.put("vitaminANeedug", 900D);
            hmap.put("vitaminDNeedug", 20D);
            hmap.put("vitaminENeedmg", 10D);
            hmap.put("tiaminNeedmg", 1.2D);
            hmap.put("riboflavinNeedmg", 1.3D);
            hmap.put("niacinNeedmg", 15D);
            hmap.put("vitaminB6Needmg", 1.6D);
            hmap.put("folateNeedug", 300D);
            hmap.put("vitaminB12Needug", 2D);
            hmap.put("vitaminCNeedmg", 75D);
            hmap.put("calciumNeedmg", 800D);
            hmap.put("phosphorusNeedmg", 600D);
            hmap.put("potassiumNeedg", 3.5D);
            hmap.put("magnesiumNeedmg", 350D);
            hmap.put("ironNeedmg", 9D);
            hmap.put("zinkNeedmg", 9D);
            hmap.put("copperNeedmg", 0.9D);
            hmap.put("iodineNeedug", 150D);
            hmap.put("seleniumNeedug", 60D);

        }


    }

    private void getFemaleRDI(int age) {

        if (age >= 10 && age < 13) {

            hmap.put("vitaminANeedug", 600D);
            hmap.put("vitaminDNeedug", 10D);
            hmap.put("vitaminENeedmg", 7D);
            hmap.put("tiaminNeedmg", 1D);
            hmap.put("riboflavinNeedmg", 1.2D);
            hmap.put("niacinNeedmg", 14D);
            hmap.put("vitaminB6Needmg", 1.1D);
            hmap.put("folateNeedug", 300D);
            hmap.put("vitaminB12Needug", 2D);
            hmap.put("vitaminCNeedmg", 50D);
            hmap.put("calciumNeedmg", 900D);
            hmap.put("phosphorusNeedmg", 700D);
            hmap.put("potassiumNeedg", 2.9D);
            hmap.put("magnesiumNeedmg", 280D);
            hmap.put("ironNeedmg", 11D);
            hmap.put("zinkNeedmg", 8D);
            hmap.put("copperNeedmg", 0.7D);
            hmap.put("iodineNeedug", 150D);
            hmap.put("seleniumNeedug", 40D);


        } else if (age < 17) {

            hmap.put("vitaminANeedug", 900D);
            hmap.put("vitaminDNeedug", 10D);
            hmap.put("vitaminENeedmg", 8D);
            hmap.put("tiaminNeedmg", 1.2D);
            hmap.put("riboflavinNeedmg", 1.4D);
            hmap.put("niacinNeedmg", 16D);
            hmap.put("vitaminB6Needmg", 1.3D);
            hmap.put("folateNeedug", 300D);
            hmap.put("vitaminB12Needug", 2D);
            hmap.put("vitaminCNeedmg", 75D);
            hmap.put("calciumNeedmg", 900D);
            hmap.put("phosphorusNeedmg", 700D);
            hmap.put("potassiumNeedg", 3.1D);
            hmap.put("magnesiumNeedmg", 280D);
            hmap.put("ironNeedmg", 15D);
            hmap.put("zinkNeedmg", 7D);
            hmap.put("copperNeedmg", 0.9D);
            hmap.put("iodineNeedug", 150D);
            hmap.put("seleniumNeedug", 50D);

        } else if (age < 30) {

            hmap.put("vitaminANeedug", 700D);
            hmap.put("vitaminDNeedug", 10D);
            hmap.put("vitaminENeedmg", 8D);
            hmap.put("tiaminNeedmg", 1.1D);
            hmap.put("riboflavinNeedmg", 1.3D);
            hmap.put("niacinNeedmg", 15D);
            hmap.put("vitaminB6Needmg", 1.3D);
            hmap.put("folateNeedug", 400D);
            hmap.put("vitaminB12Needug", 2D);
            hmap.put("vitaminCNeedmg", 75D);
            hmap.put("calciumNeedmg", 800D);
            hmap.put("phosphorusNeedmg", 600D);
            hmap.put("potassiumNeedg", 3.1D);
            hmap.put("magnesiumNeedmg", 280D);
            hmap.put("ironNeedmg", 15D);
            hmap.put("zinkNeedmg", 7D);
            hmap.put("copperNeedmg", 0.9D);
            hmap.put("iodineNeedug", 150D);
            hmap.put("seleniumNeedug", 50D);


        } else if (age < 60) {

            hmap.put("vitaminANeedug", 700D);
            hmap.put("vitaminDNeedug", 10D);
            hmap.put("vitaminENeedmg", 8D);
            hmap.put("tiaminNeedmg", 1.1D);
            hmap.put("riboflavinNeedmg", 1.2D);
            hmap.put("niacinNeedmg", 14D);
            hmap.put("vitaminB6Needmg", 1.2D);
            hmap.put("folateNeedug", 400D);
            hmap.put("vitaminB12Needug", 2D);
            hmap.put("vitaminCNeedmg", 75D);
            hmap.put("calciumNeedmg", 800D);
            hmap.put("phosphorusNeedmg", 600D);
            hmap.put("potassiumNeedg", 3.1D);
            hmap.put("magnesiumNeedmg", 280D);
            hmap.put("ironNeedmg", 15D);
            hmap.put("zinkNeedmg", 7D);
            hmap.put("copperNeedmg", 0.9D);
            hmap.put("iodineNeedug", 150D);
            hmap.put("seleniumNeedug", 50D);


        } else if (age < 74) {

            hmap.put("vitaminANeedug", 700D);
            hmap.put("vitaminDNeedug", 10D);
            hmap.put("vitaminENeedmg", 8D);
            hmap.put("tiaminNeedmg", 1D);
            hmap.put("riboflavinNeedmg", 1.2D);
            hmap.put("niacinNeedmg", 13D);
            hmap.put("vitaminB6Needmg", 1.2D);
            hmap.put("folateNeedug", 300D);
            hmap.put("vitaminB12Needug", 2D);
            hmap.put("vitaminCNeedmg", 75D);
            hmap.put("calciumNeedmg", 800D);
            hmap.put("phosphorusNeedmg", 600D);
            hmap.put("potassiumNeedg", 3.1D);
            hmap.put("magnesiumNeedmg", 280D);
            hmap.put("ironNeedmg", 9D);
            hmap.put("zinkNeedmg", 7D);
            hmap.put("copperNeedmg", 0.9D);
            hmap.put("iodineNeedug", 150D);
            hmap.put("seleniumNeedug", 50D);


        } else if (age >= 74) {

            hmap.put("vitaminANeedug", 700D);
            hmap.put("vitaminDNeedug", 20D);
            hmap.put("vitaminENeedmg", 8D);
            hmap.put("tiaminNeedmg", 1D);
            hmap.put("riboflavinNeedmg", 1.2D);
            hmap.put("niacinNeedmg", 13D);
            hmap.put("vitaminB6Needmg", 1.2D);
            hmap.put("folateNeedug", 300D);
            hmap.put("vitaminB12Needug", 2D);
            hmap.put("vitaminCNeedmg", 75D);
            hmap.put("calciumNeedmg", 800D);
            hmap.put("phosphorusNeedmg", 600D);
            hmap.put("potassiumNeedg", 3.1D);
            hmap.put("magnesiumNeedmg", 280D);
            hmap.put("ironNeedmg", 9D);
            hmap.put("zinkNeedmg", 7D);
            hmap.put("copperNeedmg", 0.9D);
            hmap.put("iodineNeedug", 150D);
            hmap.put("seleniumNeedug", 50D);


        }


    }


}




