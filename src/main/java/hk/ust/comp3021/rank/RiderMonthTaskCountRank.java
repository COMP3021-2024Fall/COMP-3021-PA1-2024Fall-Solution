package hk.ust.comp3021.rank;

import hk.ust.comp3021.Task;

public class RiderMonthTaskCountRank implements TaskRank {

    /// The less the number of tasks the rider has in the month, the higher the rank.
    @Override
    public int compare(Task source, Task target) {
        return Integer.compare(source.getRider().getMonthTaskCount(), target.getRider().getMonthTaskCount());
    }

}
