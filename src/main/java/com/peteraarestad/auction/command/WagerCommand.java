package com.peteraarestad.auction.command;

import com.peteraarestad.auction.model.AuctionItem;
import com.peteraarestad.auction.repository.AuctionItemManager;
import com.peteraarestad.auction.repository.BillDispenser;

import java.util.List;
import java.util.SortedMap;

import static com.peteraarestad.auction.repository.BillDispenser.BILL_DENOMINATIONS;

public class WagerCommand implements Command {
    private BillDispenser billDispenser;
    private AuctionItemManager auctionItemManager;

    public WagerCommand(BillDispenser billDispenser, AuctionItemManager auctionItemManager) {
        this.billDispenser = billDispenser;
        this.auctionItemManager = auctionItemManager;
    }

    @Override
    public String executeCommand(List<String> args) {
        int wageredItemId = Integer.parseInt(args.get(0));

        String betAmountString = args.get(1);

        if (!betAmountString.matches("^\\d+$")) {
            return "Invalid Bet: " + betAmountString;
        }

        int betAmount = Integer.parseInt(betAmountString);

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

            responseBuilder.append("Payout: ").append(wageredItem.getName()).append(",").append(amountWon).append("\n");

            for (Integer denomination : BILL_DENOMINATIONS) {
                Integer numberOfBills = payout.get(denomination);

                responseBuilder.append("$").append(denomination).append(", ")
                        .append(numberOfBills != null ? numberOfBills : "0").append("\n");
            }

            responseBuilder.append(new PrintStatusCommand(billDispenser, auctionItemManager).executeCommand(args));

            return responseBuilder.toString();
        }

        return "No Payout: " + wageredItem.getName();
    }
}
