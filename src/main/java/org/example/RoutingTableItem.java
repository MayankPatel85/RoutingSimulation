package org.example;

/**
 * Model for Routing Table item
 */
public class RoutingTableItem {
    String mask;
    String destinationAddress;
    String nextHop;
    String outgoingInterface;

    public RoutingTableItem(String mask, String destinationAddress, String nextHop, String outgoingInterface) {
        this.mask = mask;
        this.destinationAddress = destinationAddress;
        this.nextHop = nextHop;
        this.outgoingInterface = outgoingInterface;
    }
}
