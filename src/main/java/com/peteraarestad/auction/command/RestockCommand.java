package com.peteraarestad.auction.command;

import com.peteraarestad.auction.repository.AuctionItemManager;
import com.peteraarestad.auction.repository.BillDispenser;

import java.util.List;

/**
 * A Command the restocks the bill dispenser. Returns the current state of the dispenser and manager.
 */
public class RestockCommand implements Command {
    private BillDispenser billDispenser;
    private AuctionItemManager auctionItemManager;

    public RestockCommand(BillDispenser billDispenser, AuctionItemManager auctionItemManager) {
        this.billDispenser = billDispenser;
        this.auctionItemManager = auctionItemManager;
    }

    @Override
    public String execute(List<String> args) {
        billDispenser.restockInventory();

        return new CurrentStateCommand(billDispenser, auctionItemManager).execute(args);
    }
}
