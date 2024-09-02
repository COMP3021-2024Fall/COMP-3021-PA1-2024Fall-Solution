package hk.ust.comp3021;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

//@Data
public abstract class Account {

    protected Long id;

    protected String accountType;

    protected String name;

    protected String contactNumber;

    protected Location location;

    @Data
    protected static class AccountManager {

        private List<Account> registeredAccounts;

        private List<Customer> registeredCustomers;

        private List<Restaurant> registeredRestaurants;

        private List<Rider> registeredRiders;

        public AccountManager() {
            this.registeredAccounts = new ArrayList<>();
            this.registeredRiders = new ArrayList<>();
            this.registeredCustomers = new ArrayList<>();
            this.registeredRestaurants = new ArrayList<>();
        }

        public Account getAccountById(Long id) {
//            return registeredAccounts.stream().filter(account -> account.getId().equals(id)).findFirst().orElse(null);
            for (Account account : registeredAccounts) {
                if (account.getId().equals(id)) {
                    return account;
                }
            }
            return null;
        }

        public void addCustomer(Customer customer) {
            registeredCustomers.add(customer);
            registeredAccounts.add(customer);
        }

        public Customer getCustomerById(Long id) {
            return registeredCustomers.stream().filter(restaurant -> restaurant.getId().equals(id)).findFirst().orElse(null);
        }

        public void addRestaurant(Restaurant restaurant) {
            registeredRestaurants.add(restaurant);
            registeredAccounts.add(restaurant);
        }

        public Restaurant getRestaurantById(Long id) {
            return registeredRestaurants.stream().filter(restaurant -> restaurant.getId().equals(id)).findFirst().orElse(null);
        }

        public void addRider(Rider rider) {
            registeredRiders.add(rider);
            registeredAccounts.add(rider);
        }

        public Rider getRiderById(Long id) {
            return registeredRiders.stream().filter(restaurant -> restaurant.getId().equals(id)).findFirst().orElse(null);
        }

    }

    protected static AccountManager accountManager = new AccountManager();

    public abstract void register();

    public static Account getAccountById(Long id) {
        return accountManager.getAccountById(id);
    }

    public static AccountManager getAccountManager() {
        return accountManager;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

}
