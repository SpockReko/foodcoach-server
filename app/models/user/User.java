package models.user;

import com.avaje.ebean.Model;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;

import static models.user.User.Sex.FEMALE;
import static models.user.User.Sex.MALE;


/**
 * Created by Stolof on 17/02/17.
 */
@Entity
@Table(name="Users")
public class User extends Model{

    @Column(nullable = false) public String firstName;
    public String lastName;
    public String email;
    @Enumerated(EnumType.STRING) public Sex sex;
    public Date birthDate;
    public Float weight;
    public Float height;
    public Integer age;
    public Float activityLevel;
    @Enumerated(EnumType.STRING) public Goal goal;


    public Timestamp dateEntered;
    @Id public long id;

    public enum Sex {
        MALE,FEMALE
    }
    public enum Goal {
        DECREASE(-500), STAY(0), INCREASE(500);

        private final int value;
        Goal(int value){
            this.value = value;
        }
        public int getGoal(){
            return value;
        }


    }

   /* public float dailyCalori () {
        double dc = 0;


        HashMap<String, Double> hmap = new HashMap<String, Double>();


        if (sex == MALE) {
            dc = (activityLevel * (655.0955 + (9.5634 * weight) + (1.8496 * height) - (4.6756 * age))
                    + goal.getGoal());
        }

        double proteinNeed = 0.15 * dc / 4;
        double fatNeed = 0.3 * dc / 9;
        double carbohydratesNeed = 0.55 * dc / 9;


        if (age < 10) {

            hmap.put("vitaminDNeedug", 10D);
            hmap.put("tiaminNeedmg", 0.9D);
            hmap.put("niacinNeedmg", 12D);
            hmap.put("folateNeedug", 130D);
            hmap.put("vitaminENeedmg", 6D);
            hmap.put("riboflavinNeedmg", 1.1D);
            hmap.put("vitaminB6Needmg", 1D);
            hmap.put("vitaminB12Needug", 1.3D);
            hmap.put("vitaminCNeedmg", 40D);
            hmap.put("calciumNeedmg", 7D);
            hmap.put("vitaminANeedug", 400D);
            hmap.put("phosphorusNee", 540D);
            hmap.put("zinkNeedmg", 7D);
            hmap.put("potassiumNee", 2D);
            hmap.put("copperNeedmg", 0D);
            hmap.put("magnesiumNe", 200D);
            hmap.put("iodineNeedu", 0D);
            hmap.put("ironNeedmg", 9D);
            hmap.put("seleniumNeedug", 30D);
            hmap.put("proteinNeed", proteinNeed);
            hmap.put("carbohydratesNeed", carbohydratesNeed);
            hmap.put("fatNeed", fatNeed);
            hmap.put("bmr", dc);


        } else if (age < 13) {

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
            hmap.put("proteinNeed", proteinNeed);
            hmap.put("carbohydratesNeed", carbohydratesNeed);
            hmap.put("fatNeed", fatNeed);
            hmap.put("bmr", dc);

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
            hmap.put("proteinNeed", proteinNeed);
            hmap.put("carbohydratesNeed", carbohydratesNeed);
            hmap.put("fatNeed", fatNeed);
            hmap.put("bmr", dc);

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
            hmap.put("proteinNeed", proteinNeed);
            hmap.put("carbohydratesNeed", carbohydratesNeed);
            hmap.put("fatNeed", fatNeed);
            hmap.put("bmr", dc);

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
            hmap.put("proteinNeed", proteinNeed);
            hmap.put("carbohydratesNeed", carbohydratesNeed);
            hmap.put("fatNeed", fatNeed);
            hmap.put("bmr", dc);

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
            hmap.put("proteinNeed", proteinNeed);
            hmap.put("carbohydratesNeed", carbohydratesNeed);
            hmap.put("fatNeed", fatNeed);
            hmap.put("bmr", dc);

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
            hmap.put("proteinNeed", proteinNeed);
            hmap.put("carbohydratesNeed", carbohydratesNeed);
            hmap.put("fatNeed", fatNeed);
            hmap.put("bmr", dc);

        }

        return (float) dc;


            /*
          if (sex == FEMALE) {
            dc = (activityLevel * (66.5 + (13.7516 * weight) + (5.0033 * height) - (6.7550 * age)) + goal.getGoal() );


            return (float) dc;
        }

    }
    */
}


