package hk.ust.comp3021;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class Restaurant extends Account {

    private String district;

    private String street;

    /// Do not assign this when parsing restaurant data from file.
    private List<Dish> dishes;

    public Restaurant() {
        super();
        dishes = new ArrayList<>();
    }

    @Override
    public void register() {
        accountManager.addRestaurant(this);
    }

    public void addDish(Dish dish) {
        this.dishes.add(dish);
    }

    public static Restaurant getRestaurantById(Long id) {
        return accountManager.getRestaurantById(id);
    }

}
