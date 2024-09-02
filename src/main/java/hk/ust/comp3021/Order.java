package hk.ust.comp3021;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Order {
    private Long id;

    private Integer status;

    private Restaurant restaurant;

    private Customer customer;

    /// The time stamp when the order is created.
    private Long createTime;

    private Boolean isPayed;

    private List<Dish> orderedDishes;

    /// Make it null when not dispatched.
    private Rider rider = null;

    private Double estimatedTime;

    public Order() {
        this.status = 0;
        this.isPayed = false;
        orderedDishes = new ArrayList<>();
    }

    public void addDish(Dish dish) {
        this.orderedDishes.add(dish);
    }

}
