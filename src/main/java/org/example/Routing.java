package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Creates routing table and performs routing
 */
public class Routing {
    // for storing routing information
    ArrayList<RoutingTableItem> routingTable = new ArrayList<>();
    AndOperation andOperation = new AndOperation();

    /**
     * generates routing table from the file
     * @param fileName name of the file
     */
    void generateRoutingTableFromFile(String fileName) {
        File file = new File(fileName);
        try {
            Scanner scanner = new Scanner(file);
            while(scanner.hasNextLine()) {
                // splitting destination address and no of ones in mask
                // ex.- 192.168.1.0/24 -> 192.168.1.0 and 24 ones
                String[] destinationAndMask = scanner.nextLine().split("/");
                // adding entry in routing table
                // with mask, destination address, next hop and interface
                routingTable.add(new RoutingTableItem(generateMaskFromNoOfOnes(destinationAndMask[1]), destinationAndMask[0], scanner.nextLine(), scanner.nextLine()));
            }
        } catch (FileNotFoundException exception) {
            System.out.println("Error reading routing table from file");
        }
    }

    /**
     * performs routing operation and saves the output in RoutingOutput.txt file
     * @param fileName name of the file
     */
    void performRouting(String fileName) {
        File file = new File(fileName);
        File outputFile = new File("RoutingOutput.txt");
        try {
            Scanner scanner = new Scanner(file);
            FileWriter fileWriter = new FileWriter(outputFile);
            // reading packet from RandomPacket.txt file
            while (scanner.hasNextLine()) {
                String currentPacket = scanner.nextLine();
                // searching in routing table
                fileWriter.write(searchInTable(currentPacket) + "\n");
            }
            fileWriter.close();
        } catch (Exception exception) {
            System.out.println("Error reading packet file or writing to output file.");
        }
    }

    /**
     * check the paket is malformed or loopback, first performs AND operation with mask 255.255.255.255 for host specific address then performs AND operation with mask based on Class A,B or C address
     * @param currentPacket the current packet ex.-192.168.2.2
     * @return result based on routing table
     */
    String searchInTable(String currentPacket) {
        // splitting packet by . to get bytes
        String[] currentPacketBytes = currentPacket.split("\\.");
        // for storing the result
        String result = "";

        // if the first byte is 255 or 127
        // then returning that it is malformed or loopback address
        if(currentPacketBytes[0].equals("255")) {
            return currentPacket + " is malformed; discarded";
        } else if (currentPacketBytes[0].equals("127")) {
            return currentPacket + " is loopback;" + " discarded";
        }

        // performing and operation for host specific entry
        String hostSpecific = andOperation.performAND(currentPacket, "255.255.255.255");
        // searching for host specific entry
        String hostSpecificFound = searchEntryInTable(currentPacket, hostSpecific);

        // converting first binary from decimal to binary
        // to know whether it is class A,B or C address for applying appropriate masks
        String firstByteBinary = NumberConversion.decimalToBinary(currentPacketBytes[0]);

        // if host specific entry is not found
        if(!hostSpecificFound.isEmpty()) {
            return hostSpecificFound;
            // for class A address
        } else if (firstByteBinary.charAt(0) == '0') {
            String and = andOperation.performAND(currentPacket, "255.0.0.0");
            result = searchEntryInTable(currentPacket, and);
            // for class B address
        } else if (firstByteBinary.startsWith("10")) {
            String and = andOperation.performAND(currentPacket, "255.255.0.0");
            result = searchEntryInTable(currentPacket, and);
            // for class C address
        } else if(firstByteBinary.startsWith("110")) {
            String and = andOperation.performAND(currentPacket, "255.255.255.0");
            result = searchEntryInTable(currentPacket, and);
        }

        // if no match found in routing table
        // then forwarding on default 0.0.0.0 entry
        if(result.isEmpty()) {
            String and = andOperation.performAND(currentPacket, "0.0.0.0");
            result = searchEntryInTable(currentPacket, and);
        }

        return result;
    }

    /**
     * search for destination address in table for given packet
     * @param packet the packet ex.-192.168.2.1
     * @param destinationAddress the destination address after AND operation
     * @return result from routing table or empty string if not found
     */
    String searchEntryInTable(String packet, String destinationAddress) {
        for (int j = 0; j < routingTable.size(); j++) {
            // splitting the packet by . to get the bytes
            String[] destinationFromTable = routingTable.get(j).destinationAddress.split("/");
            // if host specific entry is present in routing table
            if(destinationFromTable[0].equals(destinationAddress)) {
                // if it is directly connected
                if(routingTable.get(j).nextHop.equals("-")) {
                    return packet + " will be forwarded on the directly connected network " + "on interface " + routingTable.get(j).outgoingInterface;
                } else {
                    return packet + " is forwarded to " + routingTable.get(j).nextHop + " out on interface " + routingTable.get(j).outgoingInterface;
                }
            }
        }
        return "";
    }

    /**
     * generates mask based on number of ones, ex-> 24 = 255.255.255.255
     * @param ones number of ones
     * @return the mask
     */
    static String generateMaskFromNoOfOnes(String ones) {
        // generating 1s
        String binaryFromOnes = "1".repeat(Integer.parseInt(ones));
        // adding 0s if number of 1s is less than 32
        if(binaryFromOnes.length() < 32) {
            binaryFromOnes += "0".repeat(32 - binaryFromOnes.length());
        }
        // getting the first byte
        String firstByte = binaryFromOnes.substring(0, 8);
        // getting the second byte
        String secondByte = binaryFromOnes.substring(8, 16);
        // getting the third byte
        String thirdByte = binaryFromOnes.substring(16, 24);
        // getting the fourth byte
        String forthByte = binaryFromOnes.substring(24, 32);
        // converting each byte to decimal and returning the mask
        return NumberConversion.binaryToDecimal(firstByte) + "." + NumberConversion.binaryToDecimal(secondByte) + "." + NumberConversion.binaryToDecimal(thirdByte) + "." + NumberConversion.binaryToDecimal(forthByte);
    }

}
