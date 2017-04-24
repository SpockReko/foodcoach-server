package helpers;

import models.food.Category;

/**
 * Contains constant values to be used throughout the application.
 * If you need to use a value that will always be the same everywhere, put it here.
 *
 * @author Fredrik Kindstrom
 */
public class Constants {

    public static final double KCAL_FACTOR = 4.184;

    public static double CO2(Category category) {
        switch (category) {
            case MALACEOUS_FRUITS:
            case CITRUS_FRUITS:
            case OTHER_FRUITS:
            case CANNED_FRUITS:
            case LEAF_VEGETABLES:
            case FRUIT_VEGETABLES:
            case CANNED_VEGETABLES:
            case BERRIES:
            case CABBAGES:
            case EDIBLE_FUNGI:
            case ONION_FAMILY_VEGETABLES:
                return 0.1;
            case ROOT_VEGETABLES_AND_TUBERS:
                return 0.02;
            case JUICE_DRINKS:
            case JUICES:
                return 0.1;
            case COFFEE:
            case TEA:
                return 0.3;
            case OTHER_ALCOHOLIC_BEVERAGES:
            case SPIRITS:
            case BEER:
            case WINE:
                return 0.3;
            case PULSES_VEGETABLES:
                return 0.07;
            case NUTS_AND_SEEDS:
                return 0.15;
            case OTHER_GRAINS:
            case WHEAT:
            case OATS_AND_BARLEY:
            case SUGAR_AND_SYRUPS:
            case STARCHES:
            case SWEETENERS:
            case CRISPBREADS:
            case RYE:
                return 0.06;
            case RICE:
                return 0.2;
            case PASTA_AND_MACARONI:
                return 0.08;
            case POTATOES:
            case POTATO_PRODUCTS:
                return 0.01;
            case MILK:
            case INFANT_FORMULAS_AND_HUMAN_MILK:
            case FERMENTED_MILK_PRODUCTS:
            case OTHER_MILK_PRODUCTS:
                return 0.1;
            case CHICKEN_EGGS:
            case OTHER_EGGS:
                return 0.15;
            case CHEESE:
                return 0.8;
            case BUTTER_AND_BUTTER_SPREADS:
            case ANIMAL_FATS:
                return 0.8;
            case MARGARINE_FAT_SPREAD_UNDER_55:
            case MARGARINE_FAT_SPREAD_OVER_55:
            case OTHER_FAT_PRODUCTS:
                return 0.15;
            case CREAM:
                return 0.4;
            case CONDIMENTS:
            case DRIED_HERBS:
            case MISCELLANEOUS_INGREDIENTS:
            case DRIED_SPICES:
            case SALT:
            case PRODUCTS_FOR_NUTRITIONAL_SUPPORT:
                return 0.1;
            case OILS:
            case FATS_FOR_COOKING_AND_INDUSTRIA:
                return 0.15;
            case FISH:
            case FISH_PRODUCTS:
            case CRUSTACEANS_AND_MOLLUSCS:
                return 0.3;
            case BEEF:
                return 2.6;
            case PORK:
            case MEAT_PRODUCTS:
            case OFFAL:
                return 0.6;
            case LAMB:
                return 2.1;
            case BIRDS:
                return 0.3;
            case SAUSAGES:
                return 0.7;
            case GAME_MEAT:
                return 0.05;
            case SOYA_PRODUCTS:
                return 0.4;
            case CHOCOLATE:
            case ICE_CREAM:
            case NON_CHOCOLATE_CONFECTIONERY:
            case OTHER_SUGAR_PRODUCTS:
            case SNACKS:
                return 0.2;
            case SOFT_DRINKS:
                return 00.3;
            case WATER:
                return 0.0;
            default:
                throw new IllegalArgumentException("No such category");
        }
    }
}
