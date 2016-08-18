package com.peteraarestad.auction.model;

public class AuctionItem {
    private final int id;

    private final String name;

    private final int odds;

    public AuctionItem(int id, String name, int odds) {
        this.id = id;
        this.name = name;
        this.odds = odds;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getOdds() {
        return odds;
    }
}
