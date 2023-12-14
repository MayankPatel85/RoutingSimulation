package org.example;

public class Main {
    public static void main(String[] args) {
        Routing routing = new Routing();
        // generating routing table from file
        routing.generateRoutingTableFromFile("RoutingTable.txt");
        // performing routing operation
        routing.performRouting("RandomPackets.txt");
    }
}