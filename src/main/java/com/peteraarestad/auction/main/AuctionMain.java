package com.peteraarestad.auction.main;

import com.peteraarestad.auction.model.AuctionItem;
import com.peteraarestad.auction.repository.AuctionItemRepository;
import com.peteraarestad.auction.repository.BillDispenser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuctionMain {
    private static final AuctionItemRepository auctionItemRepository;

    private static final BillDispenser billDispenser;

    private static final Pattern setWinningNumberCommand;

    private static final Pattern wagerCommand;

    static {
        auctionItemRepository = new AuctionItemRepository();
        initializeAuctionItemRepo(auctionItemRepository);
        billDispenser = new BillDispenser();

        setWinningNumberCommand = Pattern.compile("^w\\s+(\\d+)$(?i)");
        wagerCommand = Pattern.compile("^(\\d+)\\s+(.+)$");
    }

    public static void main(String... args) {
        System.out.println(billDispenser.currentState());
        System.out.println(auctionItemRepository.currentState());

        try {
            InputStreamReader inR = new InputStreamReader(System.in);
            BufferedReader buf = new BufferedReader(inR);
            String line;

            while ((line = buf.readLine()) != null) {
                parseAndExecuteCommand(line);
            }
        } catch (IOException e) {
            System.err.println("Problem reading input: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void parseAndExecuteCommand(String command) {
        if (command.equalsIgnoreCase("q")) {
            System.exit(0);
        }

        if (command.equalsIgnoreCase("r")) {
            billDispenser.restockInventory();
            return;
        }

        Matcher winningNumberCommandMatcher = setWinningNumberCommand.matcher(command);

        if (winningNumberCommandMatcher.matches()) {
            int winningItem = Integer.parseInt(winningNumberCommandMatcher.group(1));
            auctionItemRepository.setWinningItem(winningItem);
            return;
        }

        Matcher wagerCommandMatcher = wagerCommand.matcher(command);

        if (wagerCommandMatcher.matches()) {
            int wageredItem = Integer.parseInt(wagerCommandMatcher.group(1));

            String betAmountString = wagerCommandMatcher.group(2);

            if (!betAmountString.matches("^\\d+$")) {
                System.out.println("Invalid bet: " + betAmountString);
                return;
            }

            int betAmount = Integer.parseInt(betAmountString);

            if (wageredItem == auctionItemRepository.getWinningItem()) {
                AuctionItem wonItem = auctionItemRepository.findById(wageredItem);
                
                int amountWon = betAmount * wonItem.getOdds();

                System.out.println("Payout: " + wonItem.getName() + ", " + amountWon);
            }
        }
    }

    private static void initializeAuctionItemRepo(AuctionItemRepository auctionItemRepository) {
        auctionItemRepository.saveOrUpdate(new AuctionItem(1, "XBox", 5));
        auctionItemRepository.saveOrUpdate(new AuctionItem(2, "iPhone", 10));
        auctionItemRepository.saveOrUpdate(new AuctionItem(3, "iPad", 9));
        auctionItemRepository.saveOrUpdate(new AuctionItem(4, "Tivo", 4));
        auctionItemRepository.saveOrUpdate(new AuctionItem(5, "Roku", 3));
        auctionItemRepository.saveOrUpdate(new AuctionItem(6, "Keurig", 5));
        auctionItemRepository.saveOrUpdate(new AuctionItem(7, "Walkman", 6));

        auctionItemRepository.setWinningItem(1);
    }
}
