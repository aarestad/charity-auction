package com.peteraarestad.auction.repository;

import java.util.HashMap;
import java.util.Map;

public class BillDispenser {
    private final Map<Integer, Integer> billInventory = new HashMap<>();

    public BillDispenser() {
        this.restockInventory();
    }

    public void restockInventory() {
        billInventory.put(1, 10);
        billInventory.put(5, 10);
        billInventory.put(10, 10);
        billInventory.put(20, 10);
        billInventory.put(100, 10);
    }

    public String currentState() {
        return "Inventory:\n" +
                "$1," + billInventory.get(1) + "\n" +
                "$5," + billInventory.get(5) + "\n" +
                "$10," + billInventory.get(10) + "\n" +
                "$20," + billInventory.get(20) + "\n" +
                "$100," + billInventory.get(100) + "\n";
    }
}
