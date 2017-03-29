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




    public HashMap<Nutrient, Double> hmap = new HashMap<>();
    public HashMap<Nutrient, Double> overdoseValues = new HashMap<>();

    public User() {

        hmap.put(Nutrient.CaloriKcal, 2000D);
        hmap.put(Nutrient.Protein, 0.17*2000D/4);
        hmap.put(Nutrient.Carbohydrates, 0.50*2000D/4);
        hmap.put(Nutrient.Fat, 0.33*2000D/9);
        hmap.put(Nutrient.Fibre, 30D);

        hmap.put(Nutrient.VitaminAUG, 800D);
        hmap.put(Nutrient.VitaminDUG, 20D);
        hmap.put(Nutrient.VitaminEMG, 9D);
        hmap.put(Nutrient.ThiamineMG, 1.2D);
        hmap.put(Nutrient.RiboflavinMG, 1.35D);
        hmap.put(Nutrient.NiacinMG, 16D);
        hmap.put(Nutrient.VitaminB6MG, 1.4D);
        hmap.put(Nutrient.FolateUG, 300D);
        hmap.put(Nutrient.VitaminB12UG, 2D);
        hmap.put(Nutrient.VitaminCMG, 75D);
        hmap.put(Nutrient.CalciumMG, 800D);
        hmap.put(Nutrient.PhosphorusMG, 600D);
        hmap.put(Nutrient.PotassiumMG, 3300D);
        hmap.put(Nutrient.MagnesiumMG, 315D);
        hmap.put(Nutrient.IronMG, 9D);
        hmap.put(Nutrient.ZinkMG, 8D);
        hmap.put(Nutrient.CopperMG, 0.9D); //TODO: Don't exist on our database! Maybe under another name?
        hmap.put(Nutrient.IodineUG, 150D);
        hmap.put(Nutrient.SeleniumUG, 55D);

        calculateOverdoseValues(30);
    }

    public User(int dummyNr){
        if(dummyNr==1) {
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

    public User(Sex sex, double activityLevel, double weight, double height, int age, Goal goal, ArrayList<String> allergier){
        this.sex = sex;
        this.activityLevel = activityLevel;
        this.weight = weight;
        this.height = height;
        this.age = age;
        this.goal = goal;
        this.allergier = allergier;
        dailyCalories();
        calculateOverdoseValues(age);
    }

    
    private void dailyCalories() {
        double dc;

        if (sex == FEMALE) {
            dc = (activityLevel * (655.0955 + (9.5634 * weight) + (1.8496 * height) - (4.6756 * age)) + goal.getGoal());

            double proteinNeed = 0.15 * dc / 4;
            double fatNeed = 0.3 * dc / 9;
            double carbohydratesNeed = 0.55 * dc / 9;

            hmap.put(Nutrient.CaloriKcal, dc);
            hmap.put(Nutrient.Protein, proteinNeed);
            hmap.put(Nutrient.Carbohydrates, carbohydratesNeed);
            hmap.put(Nutrient.Fat, fatNeed);


        }
        if (sex == MALE) {
            dc = (activityLevel * (66.5 + (13.7516 * weight) + (5.0033 * height) - (6.7550 * age)) + goal.getGoal());

            double proteinNeed = 0.15 * dc / 4;
            double fatNeed = 0.3 * dc / 9;
            double carbohydratesNeed = 0.55 * dc / 9;

            hmap.put(Nutrient.CaloriKcal, dc);
            hmap.put(Nutrient.Protein, proteinNeed);
            hmap.put(Nutrient.Carbohydrates, carbohydratesNeed);
            hmap.put(Nutrient.Fat, fatNeed);

        }
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
            hmap.put(Nutrient.VitaminAUG, 300D);
            hmap.put(Nutrient.VitaminDUG, 10D);
            hmap.put(Nutrient.VitaminEMG, 3D);
            hmap.put(Nutrient.ThiamineMG, 0.4D);
            hmap.put(Nutrient.RiboflavinMG, 0.5D);
            hmap.put(Nutrient.NiacinMG, 5D);
            hmap.put(Nutrient.VitaminB6MG, 0.4D);
            hmap.put(Nutrient.FolateUG, 50D);
            hmap.put(Nutrient.VitaminB12UG, 0.5D);
            hmap.put(Nutrient.VitaminCMG, 20D);
            hmap.put(Nutrient.CalciumMG, 540D);
            hmap.put(Nutrient.PhosphorusMG, 420D);
            hmap.put(Nutrient.PotassiumMG, 1100D);
            hmap.put(Nutrient.MagnesiumMG, 80D);
            hmap.put(Nutrient.IronMG, 8D);
            hmap.put(Nutrient.ZinkMG, 5D);
            hmap.put(Nutrient.CopperMG, 0.3D);
            hmap.put(Nutrient.IodineUG, 50D);
            hmap.put(Nutrient.SeleniumUG, 15D);


        } else if (age < 2) {

            hmap.put(Nutrient.VitaminAUG, 300D);
            hmap.put(Nutrient.VitaminDUG, 10D);
            hmap.put(Nutrient.VitaminEMG, 4D);
            hmap.put(Nutrient.ThiamineMG, 0.5D);
            hmap.put(Nutrient.RiboflavinMG, 0.6D);
            hmap.put(Nutrient.NiacinMG, 7D);
            hmap.put(Nutrient.VitaminB6MG, 0.5D);
            hmap.put(Nutrient.FolateUG, 60D);
            hmap.put(Nutrient.VitaminB12UG, 0.6D);
            hmap.put(Nutrient.VitaminCMG, 25D);
            hmap.put(Nutrient.CalciumMG, 600D);
            hmap.put(Nutrient.PhosphorusMG, 470D);
            hmap.put(Nutrient.PotassiumMG, 1400D);
            hmap.put(Nutrient.MagnesiumMG, 85D);
            hmap.put(Nutrient.IronMG, 8D);
            hmap.put(Nutrient.ZinkMG, 5D);
            hmap.put(Nutrient.CopperMG, 0.3D);
            hmap.put(Nutrient.IodineUG, 70D);
            hmap.put(Nutrient.SeleniumUG, 20D);


        } else if (age < 5) {

            hmap.put(Nutrient.VitaminAUG, 350D);
            hmap.put(Nutrient.VitaminDUG, 10D);
            hmap.put(Nutrient.VitaminEMG, 5D);
            hmap.put(Nutrient.ThiamineMG, 0.6D);
            hmap.put(Nutrient.RiboflavinMG, 0.7D);
            hmap.put(Nutrient.NiacinMG, 9D);
            hmap.put(Nutrient.VitaminB6MG, 0.7D);
            hmap.put(Nutrient.FolateUG, 80D);
            hmap.put(Nutrient.VitaminB12UG, 0.8D);
            hmap.put(Nutrient.VitaminCMG, 30D);
            hmap.put(Nutrient.CalciumMG, 600D);
            hmap.put(Nutrient.PhosphorusMG, 470D);
            hmap.put(Nutrient.PotassiumMG, 1800D);
            hmap.put(Nutrient.MagnesiumMG, 120D);
            hmap.put(Nutrient.IronMG, 8D);
            hmap.put(Nutrient.ZinkMG, 6D);
            hmap.put(Nutrient.CopperMG, 0.4D);
            hmap.put(Nutrient.IodineUG, 90D);
            hmap.put(Nutrient.SeleniumUG, 25D);


        } else if (age <= 10) {

            hmap.put(Nutrient.VitaminAUG, 400D);
            hmap.put(Nutrient.VitaminDUG, 10D);
            hmap.put(Nutrient.VitaminEMG, 6D);
            hmap.put(Nutrient.ThiamineMG, 0.9D);
            hmap.put(Nutrient.RiboflavinMG, 1.1D);
            hmap.put(Nutrient.NiacinMG, 12D);
            hmap.put(Nutrient.VitaminB6MG, 1D);
            hmap.put(Nutrient.FolateUG, 130D);
            hmap.put(Nutrient.VitaminB12UG, 1.3D);
            hmap.put(Nutrient.VitaminCMG, 40D);
            hmap.put(Nutrient.CalciumMG, 700D);
            hmap.put(Nutrient.PhosphorusMG, 540D);
            hmap.put(Nutrient.PotassiumMG, 2000D);
            hmap.put(Nutrient.MagnesiumMG, 200D);
            hmap.put(Nutrient.IronMG, 9D);
            hmap.put(Nutrient.ZinkMG, 7D);
            hmap.put(Nutrient.CopperMG, 0.5D);
            hmap.put(Nutrient.IodineUG, 120D);
            hmap.put(Nutrient.SeleniumUG, 30D);


        }

    }

    private void getMaleRDI(int age) {

        if (age >= 10 && age < 13) {

            hmap.put(Nutrient.VitaminAUG, 600D);
            hmap.put(Nutrient.VitaminDUG, 10D);
            hmap.put(Nutrient.VitaminEMG, 8D);
            hmap.put(Nutrient.ThiamineMG, 1.1D);
            hmap.put(Nutrient.RiboflavinMG, 1.3D);
            hmap.put(Nutrient.NiacinMG, 15D);
            hmap.put(Nutrient.VitaminB6MG, 1.3D);
            hmap.put(Nutrient.FolateUG, 200D);
            hmap.put(Nutrient.VitaminB12UG, 2D);
            hmap.put(Nutrient.VitaminCMG, 50D);
            hmap.put(Nutrient.CalciumMG, 900D);
            hmap.put(Nutrient.PhosphorusMG, 700D);
            hmap.put(Nutrient.PotassiumMG, 3300D);
            hmap.put(Nutrient.MagnesiumMG, 280D);
            hmap.put(Nutrient.IronMG, 11D);
            hmap.put(Nutrient.ZinkMG, 11D);
            hmap.put(Nutrient.CopperMG, 0.7D);
            hmap.put(Nutrient.IodineUG, 150D);
            hmap.put(Nutrient.SeleniumUG, 40D);


        } else if (age < 17) {

            hmap.put(Nutrient.VitaminAUG, 900D);
            hmap.put(Nutrient.VitaminDUG, 10D);
            hmap.put(Nutrient.VitaminEMG, 10D);
            hmap.put(Nutrient.ThiamineMG, 1.4D);
            hmap.put(Nutrient.RiboflavinMG, 1.7D);
            hmap.put(Nutrient.NiacinMG, 19D);
            hmap.put(Nutrient.VitaminB6MG, 1.6D);
            hmap.put(Nutrient.FolateUG, 300D);
            hmap.put(Nutrient.VitaminB12UG, 2D);
            hmap.put(Nutrient.VitaminCMG, 75D);
            hmap.put(Nutrient.CalciumMG, 900D);
            hmap.put(Nutrient.PhosphorusMG, 700D);
            hmap.put(Nutrient.PotassiumMG, 3500D);
            hmap.put(Nutrient.MagnesiumMG, 350D);
            hmap.put(Nutrient.IronMG, 11D);
            hmap.put(Nutrient.ZinkMG, 12D);
            hmap.put(Nutrient.CopperMG, 0.9D);
            hmap.put(Nutrient.IodineUG, 150D);
            hmap.put(Nutrient.SeleniumUG, 60D);


        } else if (age < 30) {

            hmap.put(Nutrient.VitaminAUG, 900D);
            hmap.put(Nutrient.VitaminDUG, 10D);
            hmap.put(Nutrient.VitaminEMG, 10D);
            hmap.put(Nutrient.ThiamineMG, 1.4D);
            hmap.put(Nutrient.RiboflavinMG, 1.6D);
            hmap.put(Nutrient.NiacinMG, 19D);
            hmap.put(Nutrient.VitaminB6MG, 1.6D);
            hmap.put(Nutrient.FolateUG, 300D);
            hmap.put(Nutrient.VitaminB12UG, 2D);
            hmap.put(Nutrient.VitaminCMG, 75D);
            hmap.put(Nutrient.CalciumMG, 800D);
            hmap.put(Nutrient.PhosphorusMG, 600D);
            hmap.put(Nutrient.PotassiumMG, 3500D);
            hmap.put(Nutrient.MagnesiumMG, 350D);
            hmap.put(Nutrient.IronMG, 9D);
            hmap.put(Nutrient.ZinkMG, 9D);
            hmap.put(Nutrient.CopperMG, 0.9D);
            hmap.put(Nutrient.IodineUG, 150D);
            hmap.put(Nutrient.SeleniumUG, 60D);


        } else if (age < 60) {

            hmap.put(Nutrient.VitaminAUG, 900D);
            hmap.put(Nutrient.VitaminDUG, 10D);
            hmap.put(Nutrient.VitaminEMG, 10D);
            hmap.put(Nutrient.ThiamineMG, 1.3D);
            hmap.put(Nutrient.RiboflavinMG, 1.5D);
            hmap.put(Nutrient.NiacinMG, 18D);
            hmap.put(Nutrient.VitaminB6MG, 1.6D);
            hmap.put(Nutrient.FolateUG, 300D);
            hmap.put(Nutrient.VitaminB12UG, 2D);
            hmap.put(Nutrient.VitaminCMG, 75D);
            hmap.put(Nutrient.CalciumMG, 800D);
            hmap.put(Nutrient.PhosphorusMG, 600D);
            hmap.put(Nutrient.PotassiumMG, 3500D);
            hmap.put(Nutrient.MagnesiumMG, 350D);
            hmap.put(Nutrient.IronMG, 9D);
            hmap.put(Nutrient.ZinkMG, 9D);
            hmap.put(Nutrient.CopperMG, 0.9D);
            hmap.put(Nutrient.IodineUG, 150D);
            hmap.put(Nutrient.SeleniumUG, 60D);


        } else if (age < 74) {

            hmap.put(Nutrient.VitaminAUG, 900D);
            hmap.put(Nutrient.VitaminDUG, 10D);
            hmap.put(Nutrient.VitaminEMG, 10D);
            hmap.put(Nutrient.ThiamineMG, 1.2D);
            hmap.put(Nutrient.RiboflavinMG, 1.4D);
            hmap.put(Nutrient.NiacinMG, 16D);
            hmap.put(Nutrient.VitaminB6MG, 1.6D);
            hmap.put(Nutrient.FolateUG, 300D);
            hmap.put(Nutrient.VitaminB12UG, 2D);
            hmap.put(Nutrient.VitaminCMG, 75D);
            hmap.put(Nutrient.CalciumMG, 800D);
            hmap.put(Nutrient.PhosphorusMG, 600D);
            hmap.put(Nutrient.PotassiumMG, 3500D);
            hmap.put(Nutrient.MagnesiumMG, 350D);
            hmap.put(Nutrient.IronMG, 9D);
            hmap.put(Nutrient.ZinkMG, 9D);
            hmap.put(Nutrient.CopperMG, 0.9D);
            hmap.put(Nutrient.IodineUG, 150D);
            hmap.put(Nutrient.SeleniumUG, 60D);


        } else if (age >= 74) {

            hmap.put(Nutrient.VitaminAUG, 900D);
            hmap.put(Nutrient.VitaminDUG, 20D);
            hmap.put(Nutrient.VitaminEMG, 10D);
            hmap.put(Nutrient.ThiamineMG, 1.2D);
            hmap.put(Nutrient.RiboflavinMG, 1.3D);
            hmap.put(Nutrient.NiacinMG, 15D);
            hmap.put(Nutrient.VitaminB6MG, 1.6D);
            hmap.put(Nutrient.FolateUG, 300D);
            hmap.put(Nutrient.VitaminB12UG, 2D);
            hmap.put(Nutrient.VitaminCMG, 75D);
            hmap.put(Nutrient.CalciumMG, 800D);
            hmap.put(Nutrient.PhosphorusMG, 600D);
            hmap.put(Nutrient.PotassiumMG, 3500D);
            hmap.put(Nutrient.MagnesiumMG, 350D);
            hmap.put(Nutrient.IronMG, 9D);
            hmap.put(Nutrient.ZinkMG, 9D);
            hmap.put(Nutrient.CopperMG, 0.9D);
            hmap.put(Nutrient.IodineUG, 150D);
            hmap.put(Nutrient.SeleniumUG, 60D);

        }


    }

    private void getFemaleRDI(int age) {

        if (age >= 10 && age < 13) {

            hmap.put(Nutrient.VitaminAUG, 600D);
            hmap.put(Nutrient.VitaminDUG, 10D);
            hmap.put(Nutrient.VitaminEMG, 7D);
            hmap.put(Nutrient.ThiamineMG, 1D);
            hmap.put(Nutrient.RiboflavinMG, 1.2D);
            hmap.put(Nutrient.NiacinMG, 14D);
            hmap.put(Nutrient.VitaminB6MG, 1.1D);
            hmap.put(Nutrient.FolateUG, 300D);
            hmap.put(Nutrient.VitaminB12UG, 2D);
            hmap.put(Nutrient.VitaminCMG, 50D);
            hmap.put(Nutrient.CalciumMG, 900D);
            hmap.put(Nutrient.PhosphorusMG, 700D);
            hmap.put(Nutrient.PotassiumMG, 2900D);
            hmap.put(Nutrient.MagnesiumMG, 280D);
            hmap.put(Nutrient.IronMG, 11D);
            hmap.put(Nutrient.ZinkMG, 8D);
            hmap.put(Nutrient.CopperMG, 0.7D);
            hmap.put(Nutrient.IodineUG, 150D);
            hmap.put(Nutrient.SeleniumUG, 40D);


        } else if (age < 17) {

            hmap.put(Nutrient.VitaminAUG, 900D);
            hmap.put(Nutrient.VitaminDUG, 10D);
            hmap.put(Nutrient.VitaminEMG, 8D);
            hmap.put(Nutrient.ThiamineMG, 1.2D);
            hmap.put(Nutrient.RiboflavinMG, 1.4D);
            hmap.put(Nutrient.NiacinMG, 16D);
            hmap.put(Nutrient.VitaminB6MG, 1.3D);
            hmap.put(Nutrient.FolateUG, 300D);
            hmap.put(Nutrient.VitaminB12UG, 2D);
            hmap.put(Nutrient.VitaminCMG, 75D);
            hmap.put(Nutrient.CalciumMG, 900D);
            hmap.put(Nutrient.PhosphorusMG, 700D);
            hmap.put(Nutrient.PotassiumMG, 3100D);
            hmap.put(Nutrient.MagnesiumMG, 280D);
            hmap.put(Nutrient.IronMG, 15D);
            hmap.put(Nutrient.ZinkMG, 7D);
            hmap.put(Nutrient.CopperMG, 0.9D);
            hmap.put(Nutrient.IodineUG, 150D);
            hmap.put(Nutrient.SeleniumUG, 50D);

        } else if (age < 30) {

            hmap.put(Nutrient.VitaminAUG, 700D);
            hmap.put(Nutrient.VitaminDUG, 10D);
            hmap.put(Nutrient.VitaminEMG, 8D);
            hmap.put(Nutrient.ThiamineMG, 1.1D);
            hmap.put(Nutrient.RiboflavinMG, 1.3D);
            hmap.put(Nutrient.NiacinMG, 15D);
            hmap.put(Nutrient.VitaminB6MG, 1.3D);
            hmap.put(Nutrient.FolateUG, 400D);
            hmap.put(Nutrient.VitaminB12UG, 2D);
            hmap.put(Nutrient.VitaminCMG, 75D);
            hmap.put(Nutrient.CalciumMG, 800D);
            hmap.put(Nutrient.PhosphorusMG, 600D);
            hmap.put(Nutrient.PotassiumMG, 3100D);
            hmap.put(Nutrient.MagnesiumMG, 280D);
            hmap.put(Nutrient.IronMG, 15D);
            hmap.put(Nutrient.ZinkMG, 7D);
            hmap.put(Nutrient.CopperMG, 0.9D);
            hmap.put(Nutrient.IodineUG, 150D);
            hmap.put(Nutrient.SeleniumUG, 50D);


        } else if (age < 60) {

            hmap.put(Nutrient.VitaminAUG, 700D);
            hmap.put(Nutrient.VitaminDUG, 10D);
            hmap.put(Nutrient.VitaminEMG, 8D);
            hmap.put(Nutrient.ThiamineMG, 1.1D);
            hmap.put(Nutrient.RiboflavinMG, 1.2D);
            hmap.put(Nutrient.NiacinMG, 14D);
            hmap.put(Nutrient.VitaminB6MG, 1.2D);
            hmap.put(Nutrient.FolateUG, 400D);
            hmap.put(Nutrient.VitaminB12UG, 2D);
            hmap.put(Nutrient.VitaminCMG, 75D);
            hmap.put(Nutrient.CalciumMG, 800D);
            hmap.put(Nutrient.PhosphorusMG, 600D);
            hmap.put(Nutrient.PotassiumMG, 3100D);
            hmap.put(Nutrient.MagnesiumMG, 280D);
            hmap.put(Nutrient.IronMG, 15D);
            hmap.put(Nutrient.ZinkMG, 7D);
            hmap.put(Nutrient.CopperMG, 0.9D);
            hmap.put(Nutrient.IodineUG, 150D);
            hmap.put(Nutrient.SeleniumUG, 50D);


        } else if (age < 74) {

            hmap.put(Nutrient.VitaminAUG, 700D);
            hmap.put(Nutrient.VitaminDUG, 10D);
            hmap.put(Nutrient.VitaminEMG, 8D);
            hmap.put(Nutrient.ThiamineMG, 1D);
            hmap.put(Nutrient.RiboflavinMG, 1.2D);
            hmap.put(Nutrient.NiacinMG, 13D);
            hmap.put(Nutrient.VitaminB6MG, 1.2D);
            hmap.put(Nutrient.FolateUG, 300D);
            hmap.put(Nutrient.VitaminB12UG, 2D);
            hmap.put(Nutrient.VitaminCMG, 75D);
            hmap.put(Nutrient.CalciumMG, 800D);
            hmap.put(Nutrient.PhosphorusMG, 600D);
            hmap.put(Nutrient.PotassiumMG, 3100D);
            hmap.put(Nutrient.MagnesiumMG, 280D);
            hmap.put(Nutrient.IronMG, 9D);
            hmap.put(Nutrient.ZinkMG, 7D);
            hmap.put(Nutrient.CopperMG, 0.9D);
            hmap.put(Nutrient.IodineUG, 150D);
            hmap.put(Nutrient.SeleniumUG, 50D);


        } else if (age >= 74) {

            hmap.put(Nutrient.VitaminAUG, 700D);
            hmap.put(Nutrient.VitaminDUG, 20D);
            hmap.put(Nutrient.VitaminEMG, 8D);
            hmap.put(Nutrient.ThiamineMG, 1D);
            hmap.put(Nutrient.RiboflavinMG, 1.2D);
            hmap.put(Nutrient.NiacinMG, 13D);
            hmap.put(Nutrient.VitaminB6MG, 1.2D);
            hmap.put(Nutrient.FolateUG, 300D);
            hmap.put(Nutrient.VitaminB12UG, 2D);
            hmap.put(Nutrient.VitaminCMG, 75D);
            hmap.put(Nutrient.CalciumMG, 800D);
            hmap.put(Nutrient.PhosphorusMG, 600D);
            hmap.put(Nutrient.PotassiumMG, 3100D);
            hmap.put(Nutrient.MagnesiumMG, 280D);
            hmap.put(Nutrient.IronMG, 9D);
            hmap.put(Nutrient.ZinkMG, 7D);
            hmap.put(Nutrient.CopperMG, 0.9D);
            hmap.put(Nutrient.IodineUG, 150D);
            hmap.put(Nutrient.SeleniumUG, 50D);

        }


    }

    private void calculateOverdoseValues(int age) {
        overdoseValues.put(Nutrient.CaloriKcal,hmap.get(Nutrient.CaloriKcal)*1.2D);
        overdoseValues.put(Nutrient.Protein,hmap.get(Nutrient.Protein)*2D);
        overdoseValues.put(Nutrient.Carbohydrates,hmap.get(Nutrient.Carbohydrates)*2D);
        overdoseValues.put(Nutrient.Fat,hmap.get(Nutrient.Fat)*2D);
        overdoseValues.put(Nutrient.Fibre,hmap.get(Nutrient.Fibre)*2D);

        overdoseValues.put(Nutrient.VitaminAUG,7500D);
        overdoseValues.put(Nutrient.VitaminDUG,100D);
        overdoseValues.put(Nutrient.VitaminEMG,Double.POSITIVE_INFINITY);
        overdoseValues.put(Nutrient.ThiamineMG,500D);
        overdoseValues.put(Nutrient.RiboflavinMG,300D);
        overdoseValues.put(Nutrient.NiacinMG,50D);
        overdoseValues.put(Nutrient.VitaminB6MG,200D);
        overdoseValues.put(Nutrient.FolateUG,Double.POSITIVE_INFINITY);
        overdoseValues.put(Nutrient.VitaminB12UG,Double.POSITIVE_INFINITY);
        overdoseValues.put(Nutrient.VitaminCMG,Double.POSITIVE_INFINITY);
        overdoseValues.put(Nutrient.CalciumMG,2500D);
        overdoseValues.put(Nutrient.PhosphorusMG,4000D);
        overdoseValues.put(Nutrient.PotassiumMG,15000D);
        overdoseValues.put(Nutrient.MagnesiumMG,1000D);
        overdoseValues.put(Nutrient.IronMG,100D);
        overdoseValues.put(Nutrient.CopperMG,5D);
        overdoseValues.put(Nutrient.IodineUG,10000D);
        overdoseValues.put(Nutrient.SeleniumUG,300D);

        if(age <= 17) {
            overdoseValues.put(Nutrient.CopperMG,4D);
        }

        if(age <= 10) {
            overdoseValues.put(Nutrient.VitaminAUG,200D*weight);
            overdoseValues.put(Nutrient.VitaminDUG,50D);

            if(age <= 3) {
                overdoseValues.put(Nutrient.CopperMG,1D);
            } else if(age <= 6) {
                overdoseValues.put(Nutrient.CopperMG,2D);
            } else {
                overdoseValues.put(Nutrient.CopperMG,3D);
            }
        }
    }

}




