package com.money.recomandation;

import java.util.ArrayList;

/**
 * Created by skuba on 07.04.2016.
 */
public class MathHelper {

    static public double getMX(ArrayList<Double> categoryValues) {
        double result = 0;
        for (Double value : categoryValues) {
            result += value;
        }
        return result / categoryValues.size();
    }

    static public double getDX(ArrayList<Double> categoryValues) {
        double result = 0;
        double mx = getMX(categoryValues);
        for (Double value : categoryValues) {
            result += Math.pow((value - mx), 2);
        }
        return result / categoryValues.size() - 1;
    }

    static public double getCorrelationCoefficient(ArrayList<Double> firstCategoryValues, ArrayList<Double> secondCategoryValues) {
        double sumOfMultiply = 0;
        double sumOfSquareFirstCategoryValues = 0;
        double sumOfSquareSecondCategoryValues = 0;
        double sumFirstCategoryValues = 0;
        double sumSecondCategoryValues = 0;
        double count = firstCategoryValues.size();
        for (int i = 0; i < count; i++) {
            sumOfMultiply += firstCategoryValues.get(i) * secondCategoryValues.get(i);
            sumOfSquareFirstCategoryValues += firstCategoryValues.get(i) * firstCategoryValues.get(i);
            sumOfSquareSecondCategoryValues += secondCategoryValues.get(i) * secondCategoryValues.get(i);
            sumFirstCategoryValues += firstCategoryValues.get(i);
            sumSecondCategoryValues += secondCategoryValues.get(i);
        }
        double correlationCoefficient = (count * sumOfMultiply - sumFirstCategoryValues * sumSecondCategoryValues) /
                (
                        Math.sqrt(
                                (count * sumOfSquareFirstCategoryValues - (Math.pow(sumFirstCategoryValues, 2))) *
                                        (count * sumOfSquareSecondCategoryValues - (Math.pow(sumSecondCategoryValues, 2)))
                        )
                );
        return correlationCoefficient;
    }
}
