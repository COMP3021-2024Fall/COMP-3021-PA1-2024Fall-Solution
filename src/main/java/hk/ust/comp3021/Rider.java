package hk.ust.comp3021;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class Rider extends Account {

    private String gender;

    private Integer status;

    /// This field is the user rating of the rider, 0 to 10.
    private Double userRating;

    private Integer monthTaskCount;

    @Override
    public void register() {
        accountManager.addRider(this);
    }

    public static Rider getRiderById(Long id) {
        return accountManager.getRiderById(id);
    }

    public static List<Rider> getRiders() {
        return accountManager.getRegisteredRiders();
    }

}
