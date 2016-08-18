package com.peteraarestad.auction.command;

import com.peteraarestad.auction.repository.AuctionItemManager;
import com.peteraarestad.auction.repository.BillDispenser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CurrentStateCommandTest {
    @Mock
    BillDispenser billDispenser;

    @Mock
    AuctionItemManager auctionItemManager;

    @Test
    public void execute() throws Exception {
        when(billDispenser.currentState()).thenReturn("bill dispenser state");
        when(auctionItemManager.currentState()).thenReturn("auction item manager state");

        assertThat(new CurrentStateCommand(billDispenser, auctionItemManager).execute(Collections.emptyList()),
                is("bill dispenser state\nauction item manager state"));
    }
}