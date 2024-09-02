package hk.ust.comp3021;

import lombok.Data;

@Data
public class Task {

    /// The order status should be pending here.
    private Order order;

    private Rider rider;

    public Task(Order order, Rider rider) {
        this.order = order;
        this.rider = rider;
    }

}
