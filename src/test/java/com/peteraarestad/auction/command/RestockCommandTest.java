package com.peteraarestad.auction.command;

import com.peteraarestad.auction.repository.AuctionItemManager;
import com.peteraarestad.auction.repository.BillDispenser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RestockCommandTest {
    @Mock BillDispenser billDispenser;
    @Mock AuctionItemManager auctionItemManager;

    @Test
    public void execute() throws Exception {
        RestockCommand restockCommand = new RestockCommand(billDispenser, auctionItemManager);

        when(billDispenser.currentState()).thenReturn("bill dispenser state");
        when(auctionItemManager.currentState()).thenReturn("item manager state");

        String result = restockCommand.execute(null);

        verify(billDispenser, times(1)).restockInventory();
        assertThat(result, is("bill dispenser state\nitem manager state"));
    }
}