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

}
