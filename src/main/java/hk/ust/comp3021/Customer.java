package hk.ust.comp3021;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Customer extends Account {

    private Integer customerType;

    private String gender;

    private String email;

    @Override
    public void register() {
        accountManager.addCustomer(this);
    }

    public static Customer getCustomerById(Long id) {
        return accountManager.getCustomerById(id);
    }

}
