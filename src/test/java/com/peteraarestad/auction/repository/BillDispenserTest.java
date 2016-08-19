package com.peteraarestad.auction.repository;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class BillDispenserTest {
    private BillDispenser billDispenser;

    @Before
    public void setUp() throws Exception {
        billDispenser = new BillDispenser();
    }

    @Test
    public void getPayout_insufficientFunds() throws Exception {
        billDispenser.restockInventory();

        assertThat(billDispenser.currentState(), is("Inventory:\n" +
                "$1,10\n" +
                "$5,10\n" +
                "$10,10\n" +
                "$20,10\n" +
                "$100,10\n"));

        assertThat(billDispenser.getPayout(5000), is(nullValue()));

        assertThat(billDispenser.currentState(), is("Inventory:\n" +
                "$1,10\n" +
                "$5,10\n" +
                "$10,10\n" +
                "$20,10\n" +
                "$100,10\n"));
    }

    @Test
    public void getPayout_sufficientFunds() throws Exception {
        billDispenser.restockInventory();

        Map<Integer, Integer> payout = billDispenser.getPayout(557);

        assertThat(billDispenser.currentState(), is("Inventory:\n" +
                "$1,8\n" +
                "$5,9\n" +
                "$10,9\n" +
                "$20,8\n" +
                "$100,5\n"));

        assertThat(payout.get(1), is(2));
        assertThat(payout.get(5), is(1));
        assertThat(payout.get(10), is(1));
        assertThat(payout.get(20), is(2));
        assertThat(payout.get(100), is(5));
    }
}