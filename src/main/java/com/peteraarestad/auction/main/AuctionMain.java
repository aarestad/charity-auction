package com.peteraarestad.auction.main;

import com.peteraarestad.auction.command.*;
import com.peteraarestad.auction.model.AuctionItem;
import com.peteraarestad.auction.repository.AuctionItemManager;
import com.peteraarestad.auction.repository.BillDispenser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static com.google.common.collect.Lists.newArrayList;
import static java.lang.System.out;

/**
 * The main driver of the auction app. Application configuration is done in the <code>static</code> block. The main method
 * prints the initial state of the dispenser and manager, and then processes each command, one per line. "Qq" or ctrl-C
 * will exit the program.
 */
public class AuctionMain {
    private static final AuctionItemManager auctionItemManager;
    private static final BillDispenser billDispenser;
    private static final CommandRouter commandRouter;

    static {
        auctionItemManager = new AuctionItemManager();

        // Initialize the items and the initial winning item
        auctionItemManager.saveOrUpdate(new AuctionItem(1, "XBox", 5));
        auctionItemManager.saveOrUpdate(new AuctionItem(2, "iPhone", 10));
        auctionItemManager.saveOrUpdate(new AuctionItem(3, "iPad", 9));
        auctionItemManager.saveOrUpdate(new AuctionItem(4, "Tivo", 4));
        auctionItemManager.saveOrUpdate(new AuctionItem(5, "Roku", 3));
        auctionItemManager.saveOrUpdate(new AuctionItem(6, "Keurig", 5));
        auctionItemManager.saveOrUpdate(new AuctionItem(7, "Walkman", 6));
        auctionItemManager.setWinningItem(1);

        billDispenser = new BillDispenser();

        // Initialize the dispenser
        billDispenser.restockInventory();

        commandRouter = new CommandRouter();

        // Command input: "Q" or "q" only
        commandRouter.addRoute("^[Qq]$", new QuitCommand());

        // Command input: "R" or "r" only
        commandRouter.addRoute("^[Rr]$", new RestockCommand(billDispenser, auctionItemManager));

        // Command input: "W" or "w" followed by an integer
        commandRouter.addRoute("^[Ww]\\s+(\\d+)$", new SetWinnerCommand(billDispenser, auctionItemManager));

        // Command input: an integer followed by another non-whitespace token
        commandRouter.addRoute("^(\\d+)\\s+(.+)$", new WagerCommand(billDispenser, auctionItemManager));
    }

    public static void main(String... args) {
        out.println(new CurrentStateCommand(billDispenser, auctionItemManager).execute(newArrayList()));

        try {
            BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
            String line;

            while ((line = buf.readLine()) != null) {
                out.println(commandRouter.parseAndExecuteCommand(line));
            }
        } catch (IOException e) {
            System.err.println("Problem reading input: " + e.getMessage());
            System.exit(1);
        }
    }
}
