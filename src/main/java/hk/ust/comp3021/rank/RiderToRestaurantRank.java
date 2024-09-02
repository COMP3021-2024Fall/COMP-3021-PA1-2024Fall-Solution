package hk.ust.comp3021.rank;

import hk.ust.comp3021.Task;

public class RiderToRestaurantRank implements TaskRank {

    /// The less the distance between the rider and the restaurant, the higher the rank.
    @Override
    public int compare(Task source, Task target) {
        Double sourceDistance = source.getOrder().getRestaurant().getLocation().distanceTo(source.getRider().getLocation());
        Double targetDistance = target.getOrder().getRestaurant().getLocation().distanceTo(target.getRider().getLocation());

        return Double.compare(sourceDistance, targetDistance);
    }

}
