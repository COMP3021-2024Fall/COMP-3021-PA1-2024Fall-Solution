package hk.ust.comp3021;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Dish {
    private Long id;

    private String name;

    private String desc;

    private BigDecimal price;

    private Long restaurantId;

    @Override
    public String toString() {
        return "Dish(" +
                "id=" + id +
                ", name=" + name +
                ", desc=" + desc +
                ", price=" + price.setScale(2) +
                ", restaurantId=" + restaurantId +
                ')';
    }

}
