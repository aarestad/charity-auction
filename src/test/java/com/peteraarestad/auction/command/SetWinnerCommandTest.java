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

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SetWinnerCommandTest {
    @Mock BillDispenser billDispenser;

    @Mock AuctionItemManager auctionItemManager;

    @InjectMocks SetWinnerCommand setWinnerCommand;

    @Before
    public void setUp() {
        when(billDispenser.currentState()).thenReturn("bill dispenser state");
        when(auctionItemManager.currentState()).thenReturn("item manager state");
    }

    @Test
    public void execute_horriblyLongId() throws Exception {
        String result = setWinnerCommand.execute(newArrayList("12345678901234567890"));

        assertThat(result, is("Invalid Item Number: 12345678901234567890\nbill dispenser state\nitem manager state"));
    }

    @Test
    public void execute_itemNotFound() throws Exception {
        when(auctionItemManager.findById(anyInt())).thenReturn(null);

        String result = setWinnerCommand.execute(newArrayList("1"));

        assertThat(result, is("Invalid Item Number: 1\nbill dispenser state\nitem manager state"));
    }

    @Test
    public void execute_itemFound() throws Exception {
        when(auctionItemManager.findById(anyInt())).thenReturn(new AuctionItem(1, "moo", 1));

        String result = setWinnerCommand.execute(newArrayList("1"));

        verify(auctionItemManager).setWinningItem(1);

        assertThat(result, is("bill dispenser state\nitem manager state"));
    }
}