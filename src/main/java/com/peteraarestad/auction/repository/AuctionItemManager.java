package com.peteraarestad.auction.repository;

import com.peteraarestad.auction.model.AuctionItem;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * The auction item manager. Keeps track of the auction items and which one is the winning item
 */
public class AuctionItemManager {
    // The auction item repository. (Impl note: we use a SortedMap because it makes printing the current state easier
    // since the items are already sorted by primary key for us)
    private final SortedMap<Integer, AuctionItem> auctionItemRepository = new TreeMap<>();

    private int winningItem;

    /**
     * Find an auction item by its id
     * @param id the id of the item
     * @return the item or null if the item is not found
     */
    public AuctionItem findById(int id) {
        return auctionItemRepository.get(id);
    }

    /**
     * Save or update the auction item
     * @param auctionItem the auction item
     */
    public void saveOrUpdate(AuctionItem auctionItem) {
        auctionItemRepository.put(auctionItem.getId(), auctionItem);
    }

    /**
     * Set the id of the winning item
     * @param winningItem the winning item id
     * @throws IllegalArgumentException if the item id is not valid
     */
    public void setWinningItem(int winningItem) {
        if (findById(winningItem) == null) {
            throw new IllegalArgumentException("id " + winningItem + " is not a valid item id");
        }

        this.winningItem = winningItem;
    }

    /**
     * Get the current winning item
     * @return the current winning item
     */
    public AuctionItem getWinningItem() {
        return auctionItemRepository.get(winningItem);
    }

    /**
     * Returns a string description of the current state of this manager
     * @return the description
     */
    public String currentState() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Items:\n");

        for (AuctionItem auctionItem : auctionItemRepository.values()) {
            stringBuilder.append(auctionItem.getId()).append(",")
                    .append(auctionItem.getName()).append(",")
                    .append(auctionItem.getOdds()).append(",")
                    .append(auctionItem.getId() == winningItem ? "won" : "lost").append("\n");
        }

        return stringBuilder.toString();
    }
}
