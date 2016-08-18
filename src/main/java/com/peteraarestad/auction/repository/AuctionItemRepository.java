package com.peteraarestad.auction.repository;

import com.peteraarestad.auction.model.AuctionItem;

import java.util.SortedMap;
import java.util.TreeMap;

public class AuctionItemRepository {
    private final SortedMap<Integer, AuctionItem> auctionItemRepository = new TreeMap<>();

    private int winningItem;

    public AuctionItem findById(int id) {
        return auctionItemRepository.get(id);
    }

    public void saveOrUpdate(AuctionItem auctionItem) {
        auctionItemRepository.put(auctionItem.getId(), auctionItem);
    }

    public void remove(AuctionItem auctionItem) {
        auctionItemRepository.remove(auctionItem.getId());
    }

    public void setWinningItem(int winningItem) {
        this.winningItem = winningItem;
    }

    public int getWinningItem() {
        return winningItem;
    }

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
