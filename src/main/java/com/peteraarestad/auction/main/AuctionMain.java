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

public class AuctionMain {
    private static final AuctionItemManager auctionItemManager;
    private static final BillDispenser billDispenser;
    private static final CommandRouter commandRouter;

    static {
        auctionItemManager = new AuctionItemManager();
        initializeAuctionItemManager(auctionItemManager);

        billDispenser = new BillDispenser();
        billDispenser.restockInventory();

        commandRouter = new CommandRouter();
        commandRouter.addRoute("(?i)^q$", new QuitCommand());
        commandRouter.addRoute("(?i)^r$", new RestockCommand(billDispenser, auctionItemManager));
        commandRouter.addRoute("(?i)^w\\s+(\\d+)$", new SetWinnerCommand(billDispenser, auctionItemManager));
        commandRouter.addRoute("^(\\d+)\\s+(.+)$", new WagerCommand(billDispenser, auctionItemManager));
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
        out.println(new PrintStatusCommand(billDispenser, auctionItemManager).execute(newArrayList()));

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
