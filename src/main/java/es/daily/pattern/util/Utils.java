package es.daily.pattern.util;

import java.math.BigDecimal;

/**
 * @author AndresParMont
 */
public class Utils {
    public static boolean check(BigDecimal firstNum, Operator operator, BigDecimal secondNum) {
        switch (operator) {
            case EQUALS:
                return firstNum.compareTo(secondNum) == 0;
            case LESS_THAN:
                return firstNum.compareTo(secondNum) < 0;
            case LESS_THAN_OR_EQUALS:
                return firstNum.compareTo(secondNum) <= 0;
            case GREATER_THAN:
                return firstNum.compareTo(secondNum) > 0;
            case GREATER_THAN_OR_EQUALS:
                return firstNum.compareTo(secondNum) >= 0;
        }

        throw new IllegalArgumentException("Will never reach here");
    }

    public enum Operator {
        LESS_THAN, LESS_THAN_OR_EQUALS, GREATER_THAN, GREATER_THAN_OR_EQUALS, EQUALS
    }

}
