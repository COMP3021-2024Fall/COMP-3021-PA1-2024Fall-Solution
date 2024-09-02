package hk.ust.comp3021.rank;

import hk.ust.comp3021.Order;

public class OrderCreateTimeRank implements PendingOrderRank {

    /// The earlier the order is created, the higher the rank.
    @Override
    public int compare(Order source, Order target) {
        return Long.compare(source.getCreateTime(), target.getCreateTime());
    }

}
