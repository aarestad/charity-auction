package com.peteraarestad.auction.model;

/**
 * An auction item. Immutable.
 */
public class AuctionItem {
    /**
     * The item's ID
     */
    private final int id;

    /**
     * The item's name
     */
    private final String name;

    /**
     * The item's "odds" used to calculate its payout
     */
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AuctionItem that = (AuctionItem) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
