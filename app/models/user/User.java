package models.user;

import com.avaje.ebean.Model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.*;

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




    public HashMap<RDI, Double> hmap = new HashMap<>();

    public User() {
        /*this.sex = Sex.MALE;
        this.activityLevel = 2.0;
        this.weight = 90.0;
        this.height = 190.2;
        this.age = 25;
        this.goal = Goal.DECREASE;
        dailyCalories();
        getMaleRDI(age);*/

        hmap.put(RDI.CaloriKcal, 2000D);
        hmap.put(RDI.Protein, 0.17*2000D);
        hmap.put(RDI.Carbohydrates, 0.50*2000D);
        hmap.put(RDI.Fat, 0.33*2000D);

        hmap.put(RDI.VitaminAUG, 800D);
        hmap.put(RDI.VitaminDUG, 20D);
        hmap.put(RDI.VitaminDUG, 9D);
        hmap.put(RDI.ThiamineMG, 1.2D);
        hmap.put(RDI.RiboflavinMG, 1.35D);
        hmap.put(RDI.NiacinMG, 16D);
        hmap.put(RDI.VitaminB6MG, 1.4D);
        hmap.put(RDI.FolateUG, 300D);
        hmap.put(RDI.VitaminB12UG, 2D);
        hmap.put(RDI.VitaminCMG, 75D);
        hmap.put(RDI.CalciumMG, 800D);
        hmap.put(RDI.PhosphorusMG, 600D);
        hmap.put(RDI.PotassiumG, 3.3D);
        hmap.put(RDI.Magnesium, 315D);
        hmap.put(RDI.IronMG, 9D);
        hmap.put(RDI.ZinkMG, 8D);
        hmap.put(RDI.CopperMG, 0.9D);
        hmap.put(RDI.IodineUG, 150D);
        hmap.put(RDI.SeleniumUG, 55D);
    }


    public User(Sex sex, double activityLevel, double weight, double height, int age, Goal goal, ArrayList<String> allergier){
        this.sex = sex;
        this.activityLevel = activityLevel;
        this.weight = weight;
        this.height = height;
        this.age = age;
        this.goal = goal;
        this.allergier = allergier;
        dailyCalories();

    }

    
    private void dailyCalories() {
        double dc;

        if (sex == FEMALE) {
            dc = (activityLevel * (655.0955 + (9.5634 * weight) + (1.8496 * height) - (4.6756 * age)) + goal.getGoal());

            double proteinNeed = 0.15 * dc / 4;
            double fatNeed = 0.3 * dc / 9;
            double carbohydratesNeed = 0.55 * dc / 9;

            hmap.put(RDI.CaloriKcal, dc);
            hmap.put(RDI.Protein, proteinNeed);
            hmap.put(RDI.Carbohydrates, carbohydratesNeed);
            hmap.put(RDI.Fat, fatNeed);


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

            hmap.put(RDI.CaloriKcal, dc);
            hmap.put(RDI.Protein, proteinNeed);
            hmap.put(RDI.Carbohydrates, carbohydratesNeed);
            hmap.put(RDI.Fat, fatNeed);


            if (age < 10)
                getChildRDI(age);
            else
                getMaleRDI(age);


        }

    }

    private void getRDI() throws Exception {
        if(age<=10){
            getChildRDI(age);
        }else if(sex.equals(Sex.MALE)){
            getMaleRDI(age);
        }else if(sex.equals(Sex.FEMALE)){
            getFemaleRDI(age);
        }else{
            throw new InputMismatchException();
        }
    }

    //TODO: Improvments, change all the strings to RDI enums instead.
    private void getChildRDI(int age) {

        if (age < 1) {
            hmap.put(RDI.VitaminAUG, 300D);
            hmap.put(RDI.VitaminDUG, 10D);
            hmap.put(RDI.VitaminDUG, 3D);
            hmap.put(RDI.ThiamineMG, 0.4D);
            hmap.put(RDI.RiboflavinMG, 0.5D);
            hmap.put(RDI.NiacinMG, 5D);
            hmap.put(RDI.VitaminB6MG, 0.4D);
            hmap.put(RDI.FolateUG, 50D);
            hmap.put(RDI.VitaminB12UG, 0.5D);
            hmap.put(RDI.VitaminCMG, 20D);
            hmap.put(RDI.CalciumMG, 540D);
            hmap.put(RDI.PhosphorusMG, 420D);
            hmap.put(RDI.PotassiumG, 1.1D);
            hmap.put(RDI.Magnesium, 80D);
            hmap.put(RDI.IronMG, 8D);
            hmap.put(RDI.ZinkMG, 5D);
            hmap.put(RDI.CopperMG, 0.3D);
            hmap.put(RDI.IodineUG, 50D);
            hmap.put(RDI.SeleniumUG, 15D);


        } else if (age < 2) {

            hmap.put(RDI.VitaminAUG, 300D);
            hmap.put(RDI.VitaminDUG, 10D);
            hmap.put(RDI.VitaminDUG, 4D);
            hmap.put(RDI.ThiamineMG, 0.5D);
            hmap.put(RDI.RiboflavinMG, 0.6D);
            hmap.put(RDI.NiacinMG, 7D);
            hmap.put(RDI.VitaminB6MG, 0.5D);
            hmap.put(RDI.FolateUG, 60D);
            hmap.put(RDI.VitaminB12UG, 0.6D);
            hmap.put(RDI.VitaminCMG, 25D);
            hmap.put(RDI.CalciumMG, 600D);
            hmap.put(RDI.PhosphorusMG, 470D);
            hmap.put(RDI.PotassiumG, 1.4D);
            hmap.put(RDI.Magnesium, 85D);
            hmap.put(RDI.IronMG, 8D);
            hmap.put(RDI.ZinkMG, 5D);
            hmap.put(RDI.CopperMG, 0.3D);
            hmap.put(RDI.IodineUG, 70D);
            hmap.put(RDI.SeleniumUG, 20D);


        } else if (age < 5) {

            hmap.put(RDI.VitaminAUG, 350D);
            hmap.put(RDI.VitaminDUG, 10D);
            hmap.put(RDI.VitaminDUG, 5D);
            hmap.put(RDI.ThiamineMG, 0.6D);
            hmap.put(RDI.RiboflavinMG, 0.7D);
            hmap.put(RDI.NiacinMG, 9D);
            hmap.put(RDI.VitaminB6MG, 0.7D);
            hmap.put(RDI.FolateUG, 80D);
            hmap.put(RDI.VitaminB12UG, 0.8D);
            hmap.put(RDI.VitaminCMG, 30D);
            hmap.put(RDI.CalciumMG, 600D);
            hmap.put(RDI.PhosphorusMG, 470D);
            hmap.put(RDI.PotassiumG, 1.8D);
            hmap.put(RDI.Magnesium, 120D);
            hmap.put(RDI.IronMG, 8D);
            hmap.put(RDI.ZinkMG, 6D);
            hmap.put(RDI.CopperMG, 0.4D);
            hmap.put(RDI.IodineUG, 90D);
            hmap.put(RDI.SeleniumUG, 25D);


        } else if (age <= 10) {

            hmap.put(RDI.VitaminAUG, 400D);
            hmap.put(RDI.VitaminDUG, 10D);
            hmap.put(RDI.VitaminDUG, 6D);
            hmap.put(RDI.ThiamineMG, 0.9D);
            hmap.put(RDI.RiboflavinMG, 1.1D);
            hmap.put(RDI.NiacinMG, 12D);
            hmap.put(RDI.VitaminB6MG, 1D);
            hmap.put(RDI.FolateUG, 130D);
            hmap.put(RDI.VitaminB12UG, 1.3D);
            hmap.put(RDI.VitaminCMG, 40D);
            hmap.put(RDI.CalciumMG, 700D);
            hmap.put(RDI.PhosphorusMG, 540D);
            hmap.put(RDI.PotassiumG, 2D);
            hmap.put(RDI.Magnesium, 200D);
            hmap.put(RDI.IronMG, 9D);
            hmap.put(RDI.ZinkMG, 7D);
            hmap.put(RDI.CopperMG, 0.5D);
            hmap.put(RDI.IodineUG, 120D);
            hmap.put(RDI.SeleniumUG, 30D);


        }

    }


    private void getMaleRDI(int age) {

        if (age >= 10 && age < 13) {

            hmap.put(RDI.VitaminAUG, 600D);
            hmap.put(RDI.VitaminDUG, 10D);
            hmap.put(RDI.VitaminDUG, 8D);
            hmap.put(RDI.ThiamineMG, 1.1D);
            hmap.put(RDI.RiboflavinMG, 1.3D);
            hmap.put(RDI.NiacinMG, 15D);
            hmap.put(RDI.VitaminB6MG, 1.3D);
            hmap.put(RDI.FolateUG, 200D);
            hmap.put(RDI.VitaminB12UG, 2D);
            hmap.put(RDI.VitaminCMG, 50D);
            hmap.put(RDI.CalciumMG, 900D);
            hmap.put(RDI.PhosphorusMG, 700D);
            hmap.put(RDI.PotassiumG, 3.3D);
            hmap.put(RDI.Magnesium, 280D);
            hmap.put(RDI.IronMG, 11D);
            hmap.put(RDI.ZinkMG, 11D);
            hmap.put(RDI.CopperMG, 0.7D);
            hmap.put(RDI.IodineUG, 150D);
            hmap.put(RDI.SeleniumUG, 40D);


        } else if (age < 17) {

            hmap.put(RDI.VitaminAUG, 900D);
            hmap.put(RDI.VitaminDUG, 10D);
            hmap.put(RDI.VitaminDUG, 10D);
            hmap.put(RDI.ThiamineMG, 1.4D);
            hmap.put(RDI.RiboflavinMG, 1.7D);
            hmap.put(RDI.NiacinMG, 19D);
            hmap.put(RDI.VitaminB6MG, 1.6D);
            hmap.put(RDI.FolateUG, 300D);
            hmap.put(RDI.VitaminB12UG, 2D);
            hmap.put(RDI.VitaminCMG, 75D);
            hmap.put(RDI.CalciumMG, 900D);
            hmap.put(RDI.PhosphorusMG, 700D);
            hmap.put(RDI.PotassiumG, 3.5D);
            hmap.put(RDI.Magnesium, 350D);
            hmap.put(RDI.IronMG, 11D);
            hmap.put(RDI.ZinkMG, 12D);
            hmap.put(RDI.CopperMG, 0.9D);
            hmap.put(RDI.IodineUG, 150D);
            hmap.put(RDI.SeleniumUG, 60D);


        } else if (age < 30) {

            hmap.put(RDI.VitaminAUG, 900D);
            hmap.put(RDI.VitaminDUG, 10D);
            hmap.put(RDI.VitaminDUG, 10D);
            hmap.put(RDI.ThiamineMG, 1.4D);
            hmap.put(RDI.RiboflavinMG, 1.6D);
            hmap.put(RDI.NiacinMG, 19D);
            hmap.put(RDI.VitaminB6MG, 1.6D);
            hmap.put(RDI.FolateUG, 300D);
            hmap.put(RDI.VitaminB12UG, 2D);
            hmap.put(RDI.VitaminCMG, 75D);
            hmap.put(RDI.CalciumMG, 800D);
            hmap.put(RDI.PhosphorusMG, 600D);
            hmap.put(RDI.PotassiumG, 3.5D);
            hmap.put(RDI.Magnesium, 350D);
            hmap.put(RDI.IronMG, 9D);
            hmap.put(RDI.ZinkMG, 9D);
            hmap.put(RDI.CopperMG, 0.9D);
            hmap.put(RDI.IodineUG, 150D);
            hmap.put(RDI.SeleniumUG, 60D);


        } else if (age < 60) {

            hmap.put(RDI.VitaminAUG, 900D);
            hmap.put(RDI.VitaminDUG, 10D);
            hmap.put(RDI.VitaminDUG, 10D);
            hmap.put(RDI.ThiamineMG, 1.3D);
            hmap.put(RDI.RiboflavinMG, 1.5D);
            hmap.put(RDI.NiacinMG, 18D);
            hmap.put(RDI.VitaminB6MG, 1.6D);
            hmap.put(RDI.FolateUG, 300D);
            hmap.put(RDI.VitaminB12UG, 2D);
            hmap.put(RDI.VitaminCMG, 75D);
            hmap.put(RDI.CalciumMG, 800D);
            hmap.put(RDI.PhosphorusMG, 600D);
            hmap.put(RDI.PotassiumG, 3.5D);
            hmap.put(RDI.Magnesium, 350D);
            hmap.put(RDI.IronMG, 9D);
            hmap.put(RDI.ZinkMG, 9D);
            hmap.put(RDI.CopperMG, 0.9D);
            hmap.put(RDI.IodineUG, 150D);
            hmap.put(RDI.SeleniumUG, 60D);


        } else if (age < 74) {

            hmap.put(RDI.VitaminAUG, 900D);
            hmap.put(RDI.VitaminDUG, 10D);
            hmap.put(RDI.VitaminDUG, 10D);
            hmap.put(RDI.ThiamineMG, 1.2D);
            hmap.put(RDI.RiboflavinMG, 1.4D);
            hmap.put(RDI.NiacinMG, 16D);
            hmap.put(RDI.VitaminB6MG, 1.6D);
            hmap.put(RDI.FolateUG, 300D);
            hmap.put(RDI.VitaminB12UG, 2D);
            hmap.put(RDI.VitaminCMG, 75D);
            hmap.put(RDI.CalciumMG, 800D);
            hmap.put(RDI.PhosphorusMG, 600D);
            hmap.put(RDI.PotassiumG, 3.5D);
            hmap.put(RDI.Magnesium, 350D);
            hmap.put(RDI.IronMG, 9D);
            hmap.put(RDI.ZinkMG, 9D);
            hmap.put(RDI.CopperMG, 0.9D);
            hmap.put(RDI.IodineUG, 150D);
            hmap.put(RDI.SeleniumUG, 60D);


        } else if (age >= 74) {

            hmap.put(RDI.VitaminAUG, 900D);
            hmap.put(RDI.VitaminDUG, 20D);
            hmap.put(RDI.VitaminDUG, 10D);
            hmap.put(RDI.ThiamineMG, 1.2D);
            hmap.put(RDI.RiboflavinMG, 1.3D);
            hmap.put(RDI.NiacinMG, 15D);
            hmap.put(RDI.VitaminB6MG, 1.6D);
            hmap.put(RDI.FolateUG, 300D);
            hmap.put(RDI.VitaminB12UG, 2D);
            hmap.put(RDI.VitaminCMG, 75D);
            hmap.put(RDI.CalciumMG, 800D);
            hmap.put(RDI.PhosphorusMG, 600D);
            hmap.put(RDI.PotassiumG, 3.5D);
            hmap.put(RDI.Magnesium, 350D);
            hmap.put(RDI.IronMG, 9D);
            hmap.put(RDI.ZinkMG, 9D);
            hmap.put(RDI.CopperMG, 0.9D);
            hmap.put(RDI.IodineUG, 150D);
            hmap.put(RDI.SeleniumUG, 60D);

        }


    }

    private void getFemaleRDI(int age) {

        if (age >= 10 && age < 13) {

            hmap.put(RDI.VitaminAUG, 600D);
            hmap.put(RDI.VitaminDUG, 10D);
            hmap.put(RDI.VitaminDUG, 7D);
            hmap.put(RDI.ThiamineMG, 1D);
            hmap.put(RDI.RiboflavinMG, 1.2D);
            hmap.put(RDI.NiacinMG, 14D);
            hmap.put(RDI.VitaminB6MG, 1.1D);
            hmap.put(RDI.FolateUG, 300D);
            hmap.put(RDI.VitaminB12UG, 2D);
            hmap.put(RDI.VitaminCMG, 50D);
            hmap.put(RDI.CalciumMG, 900D);
            hmap.put(RDI.PhosphorusMG, 700D);
            hmap.put(RDI.PotassiumG, 2.9D);
            hmap.put(RDI.Magnesium, 280D);
            hmap.put(RDI.IronMG, 11D);
            hmap.put(RDI.ZinkMG, 8D);
            hmap.put(RDI.CopperMG, 0.7D);
            hmap.put(RDI.IodineUG, 150D);
            hmap.put(RDI.SeleniumUG, 40D);


        } else if (age < 17) {

            hmap.put(RDI.VitaminAUG, 900D);
            hmap.put(RDI.VitaminDUG, 10D);
            hmap.put(RDI.VitaminDUG, 8D);
            hmap.put(RDI.ThiamineMG, 1.2D);
            hmap.put(RDI.RiboflavinMG, 1.4D);
            hmap.put(RDI.NiacinMG, 16D);
            hmap.put(RDI.VitaminB6MG, 1.3D);
            hmap.put(RDI.FolateUG, 300D);
            hmap.put(RDI.VitaminB12UG, 2D);
            hmap.put(RDI.VitaminCMG, 75D);
            hmap.put(RDI.CalciumMG, 900D);
            hmap.put(RDI.PhosphorusMG, 700D);
            hmap.put(RDI.PotassiumG, 3.1D);
            hmap.put(RDI.Magnesium, 280D);
            hmap.put(RDI.IronMG, 15D);
            hmap.put(RDI.ZinkMG, 7D);
            hmap.put(RDI.CopperMG, 0.9D);
            hmap.put(RDI.IodineUG, 150D);
            hmap.put(RDI.SeleniumUG, 50D);

        } else if (age < 30) {

            hmap.put(RDI.VitaminAUG, 700D);
            hmap.put(RDI.VitaminDUG, 10D);
            hmap.put(RDI.VitaminDUG, 8D);
            hmap.put(RDI.ThiamineMG, 1.1D);
            hmap.put(RDI.RiboflavinMG, 1.3D);
            hmap.put(RDI.NiacinMG, 15D);
            hmap.put(RDI.VitaminB6MG, 1.3D);
            hmap.put(RDI.FolateUG, 400D);
            hmap.put(RDI.VitaminB12UG, 2D);
            hmap.put(RDI.VitaminCMG, 75D);
            hmap.put(RDI.CalciumMG, 800D);
            hmap.put(RDI.PhosphorusMG, 600D);
            hmap.put(RDI.PotassiumG, 3.1D);
            hmap.put(RDI.Magnesium, 280D);
            hmap.put(RDI.IronMG, 15D);
            hmap.put(RDI.ZinkMG, 7D);
            hmap.put(RDI.CopperMG, 0.9D);
            hmap.put(RDI.IodineUG, 150D);
            hmap.put(RDI.SeleniumUG, 50D);


        } else if (age < 60) {

            hmap.put(RDI.VitaminAUG, 700D);
            hmap.put(RDI.VitaminDUG, 10D);
            hmap.put(RDI.VitaminDUG, 8D);
            hmap.put(RDI.ThiamineMG, 1.1D);
            hmap.put(RDI.RiboflavinMG, 1.2D);
            hmap.put(RDI.NiacinMG, 14D);
            hmap.put(RDI.VitaminB6MG, 1.2D);
            hmap.put(RDI.FolateUG, 400D);
            hmap.put(RDI.VitaminB12UG, 2D);
            hmap.put(RDI.VitaminCMG, 75D);
            hmap.put(RDI.CalciumMG, 800D);
            hmap.put(RDI.PhosphorusMG, 600D);
            hmap.put(RDI.PotassiumG, 3.1D);
            hmap.put(RDI.Magnesium, 280D);
            hmap.put(RDI.IronMG, 15D);
            hmap.put(RDI.ZinkMG, 7D);
            hmap.put(RDI.CopperMG, 0.9D);
            hmap.put(RDI.IodineUG, 150D);
            hmap.put(RDI.SeleniumUG, 50D);


        } else if (age < 74) {

            hmap.put(RDI.VitaminAUG, 700D);
            hmap.put(RDI.VitaminDUG, 10D);
            hmap.put(RDI.VitaminDUG, 8D);
            hmap.put(RDI.ThiamineMG, 1D);
            hmap.put(RDI.RiboflavinMG, 1.2D);
            hmap.put(RDI.NiacinMG, 13D);
            hmap.put(RDI.VitaminB6MG, 1.2D);
            hmap.put(RDI.FolateUG, 300D);
            hmap.put(RDI.VitaminB12UG, 2D);
            hmap.put(RDI.VitaminCMG, 75D);
            hmap.put(RDI.CalciumMG, 800D);
            hmap.put(RDI.PhosphorusMG, 600D);
            hmap.put(RDI.PotassiumG, 3.1D);
            hmap.put(RDI.Magnesium, 280D);
            hmap.put(RDI.IronMG, 9D);
            hmap.put(RDI.ZinkMG, 7D);
            hmap.put(RDI.CopperMG, 0.9D);
            hmap.put(RDI.IodineUG, 150D);
            hmap.put(RDI.SeleniumUG, 50D);


        } else if (age >= 74) {

            hmap.put(RDI.VitaminAUG, 700D);
            hmap.put(RDI.VitaminDUG, 20D);
            hmap.put(RDI.VitaminDUG, 8D);
            hmap.put(RDI.ThiamineMG, 1D);
            hmap.put(RDI.RiboflavinMG, 1.2D);
            hmap.put(RDI.NiacinMG, 13D);
            hmap.put(RDI.VitaminB6MG, 1.2D);
            hmap.put(RDI.FolateUG, 300D);
            hmap.put(RDI.VitaminB12UG, 2D);
            hmap.put(RDI.VitaminCMG, 75D);
            hmap.put(RDI.CalciumMG, 800D);
            hmap.put(RDI.PhosphorusMG, 600D);
            hmap.put(RDI.PotassiumG, 3.1D);
            hmap.put(RDI.Magnesium, 280D);
            hmap.put(RDI.IronMG, 9D);
            hmap.put(RDI.ZinkMG, 7D);
            hmap.put(RDI.CopperMG, 0.9D);
            hmap.put(RDI.IodineUG, 150D);
            hmap.put(RDI.SeleniumUG, 50D);


        }


    }


}




