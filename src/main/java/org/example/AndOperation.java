package org.example;

/**
 * Performs AND operation on 1 bit, 8 bits and 4 bytes
 */
public class AndOperation {

    /**
     * performs AND operation on one bit
     * e.x.-> 1 AND 1=1 , 1 AND 0=0
     * @param bit1 first bit
     * @param bit2 second bit
     * @return result from AND operation
     */
    Character performAND(Character bit1, Character bit2) {
        if(bit1 == '1' && bit2 == '1') {
            return '1';
        }
        return '0';
    }

    /**
     * performs AND operation on two bytes
     * ex.-> 192.168.2.1 AND 255.255.255.0 = 192.168.2.0
     * @param byte1 first byte
     * @param byte2 second byte
     * @return result from AND in decimal
     */
    String performAND(String byte1, String byte2) {
        StringBuilder result = new StringBuilder();
        // splitting first byte by . to get 4 bytes
        String[] bytesFromByte1 = byte1.split("\\.");
        // splitting second byte by . to get 4 bytes
        String[] bytesFromByte2 = byte2.split("\\.");

        for (int i = 0; i < 4; i++) {
            // converting byte to binary from decimal
            String byteFromByte1 = NumberConversion.decimalToBinary(bytesFromByte1[i]);
            String byteFromByte2 = NumberConversion.decimalToBinary(bytesFromByte2[i]);

            // appending extra zero if binary length is less than 8
            if(byteFromByte1.length() < 8) {
                byteFromByte1 = byteFromByte1 + "0".repeat(8 - byteFromByte1.length());
            }
            if(byteFromByte2.length() < 8) {
                byteFromByte2 = byteFromByte2 + "0".repeat(8 - byteFromByte2.length());
            }

            // performing operation on 8 bits and converting it to decimal
            result.append(NumberConversion.binaryToDecimal(performAndOn8bits(byteFromByte1, byteFromByte2)));

            // adding . after 1st, 2nd and 3rd byte
            if(i != 3) {
                result.append(".");
            }

        }
        return result.toString();
    }

    /**
     * performs AND operation on 8 bits
     * ex.-> 11111111 AND 11111111 = 11111111
     * @param byte1 first byte
     * @param byte2 second byte
     * @return result from AND operation
     */
    String performAndOn8bits(String byte1, String byte2) {
        StringBuilder result = new StringBuilder();
        // performing AND on single bits and appending to result
        for (int i = 0; i < 8; i++) {
            result.append(performAND(byte1.charAt(i), byte2.charAt(i)));
        }
        return result.toString();
    }

}
