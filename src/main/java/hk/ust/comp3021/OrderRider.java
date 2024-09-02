package hk.ust.comp3021;

import lombok.Data;

@Data
public class OrderRider {
    private Order order;

    private Rider rider;

    private Double estimatedTime;

}
