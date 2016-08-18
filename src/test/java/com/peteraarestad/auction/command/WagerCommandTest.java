package com.peteraarestad.auction.command;

import com.peteraarestad.auction.model.AuctionItem;
import com.peteraarestad.auction.repository.AuctionItemManager;
import com.peteraarestad.auction.repository.BillDispenser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.OngoingStubbing;

import java.util.SortedMap;
import java.util.TreeMap;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WagerCommandTest {
    @Mock
    BillDispenser billDispenser;

    @Mock
    AuctionItemManager auctionItemManager;

    @InjectMocks
    WagerCommand wagerCommand;

    @Before
    public void setUp() {
        when(billDispenser.currentState()).thenReturn("bill dispenser state");
        when(auctionItemManager.currentState()).thenReturn("item manager state");
    }

    @Test
    public void execute_reallyHugeItemNumber() throws Exception {
        assertThat(wagerCommand.execute(newArrayList("12345678901234567890", "1")), is("Invalid Item Number: 12345678901234567890\nbill dispenser state\nitem manager state"));
    }

    @Test
    public void execute_badItemNumber() throws Exception {
        when(auctionItemManager.findById(anyInt())).thenReturn(null);

        assertThat(wagerCommand.execute(newArrayList("1", "1")), is("Invalid Item Number: 1\nbill dispenser state\nitem manager state"));
    }

    @Test
    public void execute_reallyHugeBet() throws Exception {
        givenAnAuctionItemWithId(1);

        assertThat(wagerCommand.execute(newArrayList("1", "12345678901234567890")), is("Invalid Bet: 12345678901234567890\nbill dispenser state\nitem manager state"));
    }

    @Test
    public void execute_nonIntegralOrZeroBet() throws Exception {
        givenAnAuctionItemWithId(1);

        assertThat(wagerCommand.execute(newArrayList("1", "12.34")), is("Invalid Bet: 12.34\nbill dispenser state\nitem manager state"));
        assertThat(wagerCommand.execute(newArrayList("1", "bob")), is("Invalid Bet: bob\nbill dispenser state\nitem manager state"));
        assertThat(wagerCommand.execute(newArrayList("1", "0")), is("Invalid Bet: 0\nbill dispenser state\nitem manager state"));
    }

    @Test
    public void execute_wrongItem() throws Exception {
        givenAnAuctionItemWithId(1);
        givenAWinningItemWithIdAndPayout(2, 5);

        assertThat(wagerCommand.execute(newArrayList("1", "50")), is("No Payout: some item\nbill dispenser state\nitem manager state"));
    }

    @Test
    public void execute_insufficientFunds() throws Exception {
        givenAWinningItemWithIdAndPayout(1, 5);

        when(billDispenser.getPayout(anyInt())).thenReturn(null);

        assertThat(wagerCommand.execute(newArrayList("1", "50")), is("Insufficient Funds: $250\nbill dispenser state\nitem manager state"));
    }

    @Test
    public void execute_payout() throws Exception {
        givenAWinningItemWithIdAndPayout(1, 5);

        SortedMap<Integer, Integer> payout = new TreeMap<>();
        payout.put(1, 5);
        payout.put(5, 4);
        payout.put(10, 3);

        when(billDispenser.getPayout(250)).thenReturn(payout);

        assertThat(wagerCommand.execute(newArrayList("1", "50")), is("Payout: winner!, $250\n" +
                "Dispensing:\n" +
                "$1, 5\n" +
                "$5, 4\n" +
                "$10, 3\n" +
                "$20, 0\n" +
                "$100, 0\n" +
                "bill dispenser state\nitem manager state"));
    }

    private void givenAnAuctionItemWithId(int id) {
        when(auctionItemManager.findById(id)).thenReturn(new AuctionItem(id, "some item", 1));
    }

    private void givenAWinningItemWithIdAndPayout(int id, int payout) {
        AuctionItem item = new AuctionItem(id, "winner!", payout);

        when(auctionItemManager.findById(id)).thenReturn(item);
        when(auctionItemManager.getWinningItem()).thenReturn(item);
    }
}