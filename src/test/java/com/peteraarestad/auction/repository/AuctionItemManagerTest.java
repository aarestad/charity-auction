package com.peteraarestad.auction.repository;

import com.peteraarestad.auction.model.AuctionItem;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class AuctionItemManagerTest {
    private AuctionItemManager auctionItemManager;

    @Before
    public void setUp() throws Exception {
        auctionItemManager = new AuctionItemManager();
    }

    @Test
    public void saveAndFind() throws Exception {
        assertThat(auctionItemManager.findById(1), is(nullValue()));

        AuctionItem auctionItem = new AuctionItem(1, "moo", 5);
        auctionItemManager.saveOrUpdate(auctionItem);

        assertThat(auctionItemManager.findById(1), is(auctionItem));
    }

    @Test
    public void setAndGetWinningItem() throws Exception {
        AuctionItem auctionItem1 = new AuctionItem(1, "moo", 5);
        AuctionItem auctionItem2 = new AuctionItem(2, "boo", 4);

        auctionItemManager.saveOrUpdate(auctionItem1);
        auctionItemManager.saveOrUpdate(auctionItem2);

        auctionItemManager.setWinningItem(1);
        assertThat(auctionItemManager.getWinningItem(), is(auctionItem1));
    }

    @Test(expected=IllegalArgumentException.class)
    public void setWinningItem_invalidItem() throws Exception {
        auctionItemManager.setWinningItem(1);
    }

    @Test
    public void currentState() throws Exception {
        AuctionItem auctionItem1 = new AuctionItem(1, "moo", 5);
        AuctionItem auctionItem2 = new AuctionItem(2, "boo", 4);

        auctionItemManager.saveOrUpdate(auctionItem1);
        auctionItemManager.saveOrUpdate(auctionItem2);

        auctionItemManager.setWinningItem(1);

        assertThat(auctionItemManager.currentState(), is("Items:\n" +
                "1,moo,5,won\n" +
                "2,boo,4,lost\n"));
    }

}