package com.peteraarestad.auction.main;

import com.peteraarestad.auction.model.AuctionItem;
import com.peteraarestad.auction.repository.AuctionItemManager;
import com.peteraarestad.auction.repository.BillDispenser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.SortedMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.peteraarestad.auction.repository.BillDispenser.BILL_DENOMINATIONS;
import static java.lang.System.out;

public class AuctionMain {
    private static final AuctionItemManager auctionItemManager;
    private static final BillDispenser billDispenser;

    private static final Pattern setWinningNumberCommand;
    private static final Pattern wagerCommand;

    static {
        auctionItemManager = new AuctionItemManager();
        initializeAuctionItemManager(auctionItemManager);

        billDispenser = new BillDispenser();
        billDispenser.restockInventory();

        setWinningNumberCommand = Pattern.compile("^[Ww]\\s+(\\d+)$");
        wagerCommand = Pattern.compile("^(\\d+)\\s+(.+)$");
    }

    private static void initializeAuctionItemManager(AuctionItemManager auctionItemManager) {
        auctionItemManager.saveOrUpdate(new AuctionItem(1, "XBox", 5));
        auctionItemManager.saveOrUpdate(new AuctionItem(2, "iPhone", 10));
        auctionItemManager.saveOrUpdate(new AuctionItem(3, "iPad", 9));
        auctionItemManager.saveOrUpdate(new AuctionItem(4, "Tivo", 4));
        auctionItemManager.saveOrUpdate(new AuctionItem(5, "Roku", 3));
        auctionItemManager.saveOrUpdate(new AuctionItem(6, "Keurig", 5));
        auctionItemManager.saveOrUpdate(new AuctionItem(7, "Walkman", 6));

        auctionItemManager.setWinningItem(1);
    }

    public static void main(String... args) {
        printStatus();

        try {
            BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
            String line;

            while ((line = buf.readLine()) != null) {
                parseAndExecuteCommand(line);
            }
        } catch (IOException e) {
            System.err.println("Problem reading input: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void printStatus() {
        out.println(billDispenser.currentState());
        out.println(auctionItemManager.currentState());
    }

    private static void parseAndExecuteCommand(String command) {
        if (command.equalsIgnoreCase("q")) {
            System.exit(0);
        }

        if (command.equalsIgnoreCase("r")) {
            billDispenser.restockInventory();
            printStatus();
            return;
        }

        Matcher winningNumberCommandMatcher = setWinningNumberCommand.matcher(command);

        if (winningNumberCommandMatcher.matches()) {
            int winningItem = Integer.parseInt(winningNumberCommandMatcher.group(1));

            AuctionItem wageredItem = auctionItemManager.findById(winningItem);

            if (wageredItem == null) {
                out.println("Invalid Item Number: " + winningItem);
                return;
            }

            auctionItemManager.setWinningItem(winningItem);
            printStatus();
            return;
        }

        Matcher wagerCommandMatcher = wagerCommand.matcher(command);

        if (wagerCommandMatcher.matches()) {
            int wageredItemId = Integer.parseInt(wagerCommandMatcher.group(1));

            String betAmountString = wagerCommandMatcher.group(2);

            if (!betAmountString.matches("^\\d+$")) {
                out.println("Invalid Bet: " + betAmountString);
                return;
            }

            int betAmount = Integer.parseInt(betAmountString);

            AuctionItem wageredItem = auctionItemManager.findById(wageredItemId);

            if (wageredItem == null) {
                out.println("Invalid Item Number: " + wageredItemId);
                return;
            }

            if (wageredItem.equals(auctionItemManager.getWinningItem())) {
                int amountWon = betAmount * wageredItem.getOdds();

                SortedMap<Integer, Integer> payout = billDispenser.getPayout(amountWon);

                if (payout == null) {
                    out.println("Insufficient Funds: " + amountWon);
                    return;
                }

                out.println("Payout: " + wageredItem.getName() + "," + amountWon);

                for (Integer denomination : BILL_DENOMINATIONS) {
                    Integer numberOfBills = payout.get(denomination);

                    out.println("$" + denomination + ", " + (numberOfBills != null ? numberOfBills : "0"));
                }

                printStatus();
                return;
            } else {
                out.println("No Payout: " + wageredItem.getName());
                printStatus();
                return;
            }
        }

        out.println("Invalid Command: " + command);
    }
}
