package com.peteraarestad.auction.repository;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.*;

/**
 * The bill dispenser responsible for payouts
 */
public class BillDispenser {
    private final Map<Integer, Integer> billInventory = new HashMap<>();

    /**
     * Available bill denominations
     */
    public static final ImmutableList<Integer> BILL_DENOMINATIONS = ImmutableList.of(1, 5, 10, 20, 100);

    /**
     * The inventory of each bill that will be set when restocking the inventory
     */
    private static final int INITIAL_INVENTORY = 10;

    /**
     * Restock the inventory using the bill denominations and initial inventory constants
     */
    public void restockInventory() {
        for (int denomination : BILL_DENOMINATIONS) {
            billInventory.put(denomination, INITIAL_INVENTORY);
        }
    }

    /**
     * Try to satisfy a payout amount. If successful, return a map with the number of each bill used in the payout,
     * and update our inventory to reflect the payout.
     * If we can't satisfy the request, return null and leave the inventory untouched.
     *
     * @param payoutAmount the payout amount requested
     * @return a map describing the payout, or null if it cannot be satisfied
     */
    public Map<Integer, Integer> getPayout(int payoutAmount) {
        if (payoutAmount <= 0) {
            return null;
        }

        Map<Integer, Integer> payout = new HashMap<>();

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

    /**
     * Describe the current state of the dispenser
     * @return a string describing the bill inventory
     */
    public String currentState() {
        StringBuilder stringBuilder = new StringBuilder("Inventory:\n");

        for (int denomination : BILL_DENOMINATIONS) {
            stringBuilder.append("$").append(denomination).append(",")
                    .append(billInventory.get(denomination)).append("\n");
        }

        return stringBuilder.toString();
    }
}
