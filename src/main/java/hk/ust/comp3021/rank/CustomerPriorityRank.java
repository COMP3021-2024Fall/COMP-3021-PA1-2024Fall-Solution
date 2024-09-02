package hk.ust.comp3021.rank;

import hk.ust.comp3021.Order;

public class CustomerPriorityRank implements PendingOrderRank {

    @Override
    public int compare(Order source, Order target) {
        return Integer.compare(source.getCustomer().getCustomerType(), target.getCustomer().getCustomerType());
    }

}
