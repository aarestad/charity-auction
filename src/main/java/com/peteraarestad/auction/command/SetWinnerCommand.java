package com.peteraarestad.auction.command;

import com.peteraarestad.auction.model.AuctionItem;
import com.peteraarestad.auction.repository.AuctionItemManager;
import com.peteraarestad.auction.repository.BillDispenser;

import java.util.List;

/**
 * A Command to set the winning item. Returns an error message if the item number was invalid, or the
 * current state of the dispenser and manager if successful.
 */
public class SetWinnerCommand implements Command {
    private BillDispenser billDispenser;
    private AuctionItemManager auctionItemManager;

    public SetWinnerCommand(BillDispenser billDispenser, AuctionItemManager auctionItemManager) {
        this.billDispenser = billDispenser;
        this.auctionItemManager = auctionItemManager;
    }

    @Override
    public String execute(List<String> args) {
        int winningItem;

        try {
            winningItem = Integer.parseInt(args.get(0));
        } catch (NumberFormatException nfe) {
            return "Invalid Item Number: " + args.get(0) + "\n" + currentState();
        }

        AuctionItem wageredItem = auctionItemManager.findById(winningItem);

        if (wageredItem == null) {
            return "Invalid Item Number: " + winningItem + "\n" + currentState();
        }

        auctionItemManager.setWinningItem(winningItem);
        return currentState();
    }

    private String currentState() {
        return new CurrentStateCommand(billDispenser, auctionItemManager).execute(null);
    }
}
