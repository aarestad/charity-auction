package com.peteraarestad.auction.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;

public class BillDispenser {
    private final Map<Integer, Integer> billInventory = new HashMap<>();

    private static final int[] BILL_DENOMINATIONS = {1, 5, 10, 20, 100};

    public BillDispenser() {
        this.restockInventory();
    }

    public void restockInventory() {
        for (int denomination : BILL_DENOMINATIONS) {
            billInventory.put(denomination, 10);
        }
    }

    public SortedMap<Integer, Integer> getPayout(int payoutAmount) {
        return null;
    }

    public String currentState() {
        StringBuilder stringBuilder = new StringBuilder("Inventory:\n");

        for (int denomination : BILL_DENOMINATIONS) {
            stringBuilder.append("$").append(denomination).append(",")
                    .append(billInventory.get(denomination)).append("\n");
        }

        return stringBuilder.toString();
    }
}
