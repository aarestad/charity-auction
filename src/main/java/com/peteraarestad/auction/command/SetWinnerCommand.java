package com.peteraarestad.auction.command;

import com.peteraarestad.auction.model.AuctionItem;
import com.peteraarestad.auction.repository.AuctionItemManager;
import com.peteraarestad.auction.repository.BillDispenser;

import java.util.List;

public class SetWinnerCommand implements Command {
    private BillDispenser billDispenser;
    private AuctionItemManager auctionItemManager;

    public SetWinnerCommand(BillDispenser billDispenser, AuctionItemManager auctionItemManager) {
        this.billDispenser = billDispenser;
        this.auctionItemManager = auctionItemManager;
    }

    @Override
    public String execute(List<String> args) {
        int winningItem = Integer.parseInt(args.get(0));

        AuctionItem wageredItem = auctionItemManager.findById(winningItem);

        if (wageredItem == null) {
            return "Invalid Item Number: " + winningItem;
        }

        auctionItemManager.setWinningItem(winningItem);
        return new CurrentStateCommand(billDispenser, auctionItemManager).execute(args);
    }
}
