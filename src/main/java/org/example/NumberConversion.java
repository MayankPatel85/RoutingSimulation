package org.example;

/**
 * Converts number from decimal to binary and vise versa
 */
public class NumberConversion {

    /**
     * converts decimal number binary number
     * @param decimal decimal number
     * @return binary of decimal
     */
    static String decimalToBinary(String decimal) {
        StringBuilder binary = new StringBuilder();
        int decimalNumber = Integer.parseInt(decimal);
        while(decimalNumber != 0) {
            int remainder = decimalNumber % 2;
            decimalNumber = decimalNumber / 2;
            binary.append(remainder);
        }
        if(binary.length() < 8) {
            binary.append("0".repeat(8 - binary.length()));
        }
        return binary.reverse().toString();
    }

    /**
     * converts binary number to decimal
     * @param binary binary number
     * @return decimal of binary
     */
    static String binaryToDecimal(String binary) {
        int result = 0;
        int power = binary.length() - 1;
        for (int i = 0; i < binary.length(); i++) {
            result += Integer.parseInt(String.valueOf(binary.charAt(i))) * Math.pow(2, power);
            power -= 1;
        }
        return String.valueOf(result);
    }

}
