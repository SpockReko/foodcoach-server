package tools;

/**
 * Created by emmafahlen on 2017-02-14.
 */
public class TestITF {
    static IngredientToFood food = new IngredientToFood();

    public static void main(String[] args) {
        System.out.println(food.ingToFood("Avocado").getName());
    }

}
