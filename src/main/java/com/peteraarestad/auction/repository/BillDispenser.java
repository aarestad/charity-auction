package com.peteraarestad.auction.repository;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.*;

public class BillDispenser {
    private final Map<Integer, Integer> billInventory = new HashMap<>();

    public static final ImmutableList<Integer> BILL_DENOMINATIONS = ImmutableList.of(1, 5, 10, 20, 100);

    private static final int INITIAL_INVENTORY = 10;

    public void restockInventory() {
        for (int denomination : BILL_DENOMINATIONS) {
            billInventory.put(denomination, INITIAL_INVENTORY);
        }
    }

    public SortedMap<Integer, Integer> getPayout(int payoutAmount) {
        SortedMap<Integer, Integer> payout = new TreeMap<>();

        int amountLeftToPay = payoutAmount;

        for (Integer denomination : Lists.reverse(BILL_DENOMINATIONS)) {
            int numOfBills = amountLeftToPay / denomination;

            if (numOfBills > 0) {
                int billsInInventory = billInventory.get(denomination);

                int billsToWithdraw = Math.min(billsInInventory, numOfBills);

                payout.put(denomination, billsToWithdraw);
                amountLeftToPay -= denomination * billsToWithdraw;
            }
        }

        if (amountLeftToPay > 0) {
            return null;
        }

        for (Map.Entry<Integer, Integer> payoutDenomination : payout.entrySet()) {
            billInventory.put(payoutDenomination.getKey(), billInventory.get(payoutDenomination.getKey()) - payoutDenomination.getValue());
        }

        return payout;
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
