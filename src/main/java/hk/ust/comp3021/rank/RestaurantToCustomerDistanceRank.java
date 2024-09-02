package hk.ust.comp3021.rank;

import hk.ust.comp3021.Order;

public class RestaurantToCustomerDistanceRank implements PendingOrderRank {

    /// The further the restaurant is from the customer, the higher the rank.
    @Override
    public int compare(Order source, Order target) {
        Double sourceDistance = source.getCustomer().getLocation().distanceTo(target.getRestaurant().getLocation());
        Double targetDistance = target.getCustomer().getLocation().distanceTo(target.getRestaurant().getLocation());

        return Double.compare(targetDistance, sourceDistance);
    }

}
