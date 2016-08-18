package com.peteraarestad.auction.command;

import com.peteraarestad.auction.model.AuctionItem;
import com.peteraarestad.auction.repository.AuctionItemManager;
import com.peteraarestad.auction.repository.BillDispenser;

import java.util.List;
import java.util.SortedMap;

import static com.peteraarestad.auction.repository.BillDispenser.BILL_DENOMINATIONS;

/**
 * A Command to make a bet. On success, returns a payout message indicating the bills paid out and the new state of the
 * bill dispenser and item manager.
 *
 * Possible error messages:
 *
 * Invalid Bet: {bet amount} if the bet amount was not a valid integer (must be positive and below INT_MAX)
 * Invalid Item Number: {item id} if the item id was not valid
 * Insufficient Funds: {payout amount} if the final payout amount could not be fulfilled wit the bill dispenser's
 *                     cash on hand
 * No Payout: {item name} if the item name specified was not the winning item
 */
public class WagerCommand implements Command {
    private BillDispenser billDispenser;
    private AuctionItemManager auctionItemManager;

    public WagerCommand(BillDispenser billDispenser, AuctionItemManager auctionItemManager) {
        this.billDispenser = billDispenser;
        this.auctionItemManager = auctionItemManager;
    }

    @Override
    public String execute(List<String> args) {
        int wageredItemId = Integer.parseInt(args.get(0));

        String betAmountString = args.get(1);

        int betAmount;

        try {
            betAmount = Integer.parseInt(betAmountString);
        } catch (NumberFormatException nfe) {
            return "Invalid Bet: " + betAmountString;
        }

        if (betAmount == 0) {
            return "Invalid Bet: " + betAmountString;
        }

        AuctionItem wageredItem = auctionItemManager.findById(wageredItemId);

        if (wageredItem == null) {
            return "Invalid Item Number: " + wageredItemId;
        }

        if (wageredItem.equals(auctionItemManager.getWinningItem())) {
            int amountWon = betAmount * wageredItem.getOdds();

            SortedMap<Integer, Integer> payout = billDispenser.getPayout(amountWon);

            if (payout == null) {
                return "Insufficient Funds: " + amountWon;
            }

            StringBuilder responseBuilder = new StringBuilder();

            responseBuilder.append("Payout: ").append(wageredItem.getName()).append(",$").append(amountWon).append("\n");

            responseBuilder.append("Dispensing:\n");

            for (Integer denomination : BILL_DENOMINATIONS) {
                Integer numberOfBills = payout.get(denomination);

                responseBuilder.append("$").append(denomination).append(", ")
                        .append(numberOfBills != null ? numberOfBills : "0").append("\n");
            }

            responseBuilder.append(new CurrentStateCommand(billDispenser, auctionItemManager).execute(args));

            return responseBuilder.toString();
        }

        return "No Payout: " + wageredItem.getName();
    }
}
