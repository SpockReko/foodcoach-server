package helpers;

import models.recipe.Amount;

/**
 * Created by fredrikkindstrom on 2017-04-28.
 */
public class BooleanHelper {

    public static boolean isUnitBetter(Amount.Unit u1, Amount.Unit u2) {
        return
            u1.getType() == Amount.Unit.Type.SINGLE &&
                (u2.getType() == Amount.Unit.Type.VOLUME ||
                    u2.getType() == Amount.Unit.Type.MASS);
    }

    public static boolean isQuantityBetter(int indexOfUnit, int indexOfQuantity, int currentIndex) {
        return Math.abs(indexOfQuantity - indexOfUnit) > Math.abs(currentIndex - indexOfUnit);
    }
}
