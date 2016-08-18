package com.peteraarestad.auction.command;

import com.peteraarestad.auction.repository.AuctionItemManager;
import com.peteraarestad.auction.repository.BillDispenser;

import java.util.List;

public class PrintStatusCommand implements Command {
    private BillDispenser billDispenser;
    private AuctionItemManager auctionItemManager;

    public PrintStatusCommand(BillDispenser billDispenser, AuctionItemManager auctionItemManager) {
        this.billDispenser = billDispenser;
        this.auctionItemManager = auctionItemManager;
    }

    @Override
    public String executeCommand(List<String> args) {
        return billDispenser.currentState() + "\n" + auctionItemManager.currentState();
    }
}
