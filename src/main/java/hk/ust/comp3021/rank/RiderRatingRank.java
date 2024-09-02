package hk.ust.comp3021.rank;

import hk.ust.comp3021.Task;

public class RiderRatingRank implements TaskRank {

    /// The higher the rating of the rider, the higher the rank.
    @Override
    public int compare(Task source, Task target) {
        return Double.compare(target.getRider().getUserRating(), source.getRider().getUserRating());
    }

}
