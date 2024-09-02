package hk.ust.comp3021;

import hk.ust.comp3021.rank.*;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

public class DispatchSystem {

    private static volatile DispatchSystem dispatchSystem;

    /// The field is to record the current account amount, used to represent the account id.
    private Long accountNumber = 0L;

    /// The field represents the current time stamp, we assume .
    private Long currentTimestamp = 3600L;

    private List<Dish> availableDishes;

    private List<Order> availableOrders;

    private List<Order> dispatchedOrders;

    private DispatchSystem() {
        if (dispatchSystem != null) {
            throw new RuntimeException("Plase use the getInstance() method to get the singleton!");
        }

        this.availableOrders = new ArrayList<>();
        this.availableDishes = new ArrayList<>();
        this.dispatchedOrders = new ArrayList<>();
    }

    public static DispatchSystem getInstance() {
        if (dispatchSystem == null) {
            synchronized (DispatchSystem.class) {
                if (dispatchSystem == null) {
                    dispatchSystem = new DispatchSystem();
                }
            }
        }

        return dispatchSystem;
    }

    public Dish getDishById(Long id) {
        return availableDishes.stream().filter(dish -> dish.getId().equals(id)).findFirst().orElse(null);
    }

    public void parseAccounts(String fileName) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
            // Read the file and parse the accounts.
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.isEmpty()) {
                    continue;
                }

                String[] fields = line.split(",");
                if (fields.length < 2) {
                    throw new IOException("The account file is not well formatted!");
                }

                for (int i = 0; i < fields.length; i++) {
                    fields[i] = fields[i].trim();
                }

                String accountType = fields[1];
                if (accountType.equals("CUSTOMER")) {
                    assert fields.length == 8;
                    Customer customer = new Customer();
                    customer.setId(Long.valueOf(fields[0]));
                    customer.setAccountType(fields[1]);
                    customer.setName(fields[2]);
                    customer.setContactNumber(fields[3]);
                    String[] location = fields[4].substring(1, fields[4].length() - 1).split(" ");
                    assert location.length == 2;
                    customer.setLocation(new Location(location[0], location[1]));
                    customer.setCustomerType(Integer.valueOf(fields[5]));
                    customer.setGender(fields[6]);
                    customer.setEmail(fields[7]);

                    customer.register();
                } else if (accountType.equals("RESTAURANT")) {
                    assert fields.length == 7;
                    Restaurant restaurant = new Restaurant();
                    restaurant.setId(Long.valueOf(fields[0]));
                    restaurant.setAccountType(fields[1]);
                    restaurant.setName(fields[2]);
                    restaurant.setContactNumber(fields[3]);
                    String[] location = fields[4].substring(1, fields[4].length() - 1).split(" ");
                    assert location.length == 2;
                    restaurant.setLocation(new Location(location[0], location[1]));
                    restaurant.setDistrict(fields[5]);
                    restaurant.setStreet(fields[6]);

                    restaurant.register();
                } else if (accountType.equals("RIDER")) {
                    assert fields.length == 9;
                    Rider rider = new Rider();
                    rider.setId(Long.valueOf(fields[0]));
                    rider.setAccountType(fields[1]);
                    rider.setName(fields[2]);
                    rider.setContactNumber(fields[3]);
                    String[] location = fields[4].substring(1, fields[4].length() - 1).split(" ");
                    assert location.length == 2;
                    rider.setLocation(new Location(location[0], location[1]));
                    rider.setGender(fields[5]);
                    rider.setStatus(Integer.valueOf(fields[6]));
                    rider.setUserRating(Double.valueOf(fields[7]));
                    rider.setMonthTaskCount(Integer.valueOf(fields[8]));

                    rider.register();
                } else {
                    throw new IOException("The account type is not supported!");
                }
            }
        }
    }

    public void parseDishes(String fileName) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
            // Read the file and parse the dishes.
            String line;
            while ((line = bufferedReader.readLine()) != null && !line.isEmpty()) {
                String[] fields = line.split(",");
                if (fields.length < 2) {
                    throw new IOException("The dish file is not well formatted!");
                }

                for (int i = 0; i < fields.length; i++) {
                    fields[i] = fields[i].trim();
                }

                assert fields.length == 5;
                Dish dish = new Dish();
                dish.setId(Long.valueOf(fields[0]));
                dish.setName(fields[1]);
                dish.setDesc(fields[2]);
                dish.setPrice(BigDecimal.valueOf(Double.parseDouble(fields[3])));

//                Account.AccountManager accountManager = Account.getAccountManager();
                Restaurant restaurant = Restaurant.getRestaurantById(Long.valueOf(fields[4]));
                if (restaurant == null) {
                    throw new IOException("The restaurant is not registered!");
                }

                dish.setRestaurantId(restaurant.getId());
                restaurant.addDish(dish);
                availableDishes.add(dish);

            }
        }
    }

    public void parseOrders(String fileName) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
            // Read the file and parse the orders.
            String line;
            while ((line = bufferedReader.readLine()) != null && !line.isEmpty()) {
                String[] fields = line.split(",");
                if (fields.length < 2) {
                    throw new IOException("The order file is not well formatted!");
                }

                for (int i = 0; i < fields.length; i++) {
                    fields[i] = fields[i].trim();
                }

                assert fields.length == 8;
                Order order = new Order();
                order.setId(Long.valueOf(fields[0]));
                order.setStatus(Integer.valueOf(fields[1]));

                Restaurant restaurant = Restaurant.getRestaurantById(Long.valueOf(fields[2]));
                if (restaurant == null) {
                    throw new IOException("The restaurant is not registered!");
                }
                order.setRestaurant(restaurant);

                Customer customer = Customer.getCustomerById(Long.valueOf(fields[3]));
                if (customer == null) {
                    throw new IOException("The customer is not registered!");
                }
                order.setCustomer(customer);

                order.setCreateTime(Long.valueOf(fields[4]));
                if (Integer.parseInt(fields[5]) == 0) {
                    order.setIsPayed(false);
                } else if (Integer.parseInt(fields[5]) == 1) {
                    order.setIsPayed(true);
                } else {
                    throw new IOException("The order is not payed!");
                }

                Long[] dishIds = Arrays.stream(fields[6].substring(1, fields[6].length() - 1).split(" ")).map(Long::valueOf).toArray(Long[]::new);

                for (Long dishId : dishIds) {
                    Dish dish = getDishById(dishId);
                    if (dish == null) {
                        throw new IOException("The dish is not available!");
                    }
                    order.addDish(dish);
                }

                availableOrders.add(order);
                if (fields[7].equals("NA")) {
                    order.setRider(null);
                } else {
                    Rider rider = Rider.getRiderById(Long.valueOf(fields[7]));
                    if (rider == null) {
                        throw new IOException("The rider is not registered!");
                    }
                    order.setRider(rider);
                }

            }
        }
    }

    public List<Order> getAvailablePendingOrders() {
        return availableOrders.stream().filter(order -> order.getStatus().equals(Constants.PENDING_ORDER) && order.getIsPayed() && order.getRider() == null).toList();
    }

    public List<Order> getRankedPendingOrders(List<Order> pendingOrders) {
        Comparator<Order> orderComparator = new CustomerPriorityRank()
                .thenComparing(new OrderCreateTimeRank())
                .thenComparing(new RestaurantToCustomerDistanceRank());

        return pendingOrders.stream().sorted(orderComparator).toList();
    }

    /// Get the current pool of available riders to dispatch.
    public List<Rider> getAvailableRiders() {
        return Rider.getRiders().stream().filter(rider -> rider.getStatus().equals(Constants.RIDER_ONLINE_ORDER)).toList();
    }

    /// Choose the best rider for the order.
    public Task matchTheBestTask(Order order, List<Rider> availableRiders) {
        if (availableRiders.isEmpty()) {
            return null;
        }

        List<Task> tmpTasks = new ArrayList<>();

        for (Rider rider: availableRiders) {
            Task task = new Task(order, rider);
            tmpTasks.add(task);
        }

        Comparator<Task> taskComparator = new RiderToRestaurantRank()
                .thenComparing(new RiderRatingRank())
                .thenComparing(new RiderMonthTaskCountRank());

        Task bestTask = tmpTasks.stream().sorted(taskComparator).findFirst().orElse(null);

        return bestTask;
    }

    public void dispatchFirstRound() {
        List<Order> pendingOrders = getAvailablePendingOrders();
        List<Order> rankedOrders = getRankedPendingOrders(pendingOrders);
        List<Rider> availableRiders = getAvailableRiders();
        List<Rider> tmpRiders = new LinkedList<>(availableRiders);

        for (Order order : rankedOrders) {
            Task task = matchTheBestTask(order, tmpRiders);
            if (task == null) {
                assert tmpRiders.isEmpty();
                break;
            }

            order.setRider(task.getRider());
            order.setStatus(Constants.DISPATCHED_ORDER);
            task.getRider().setStatus(Constants.RIDER_DELIVERING);

            try {
                tmpRiders.remove(task.getRider());
            } catch (Exception e) {
                e.printStackTrace();
            }

            order.calculateEstimatedTime();
            dispatchedOrders.add(order);
        }

    }

    public void writeDispatchedOrders(String fileName) throws IOException {
        // Write the dispatched orders to the file.
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName))) {
            for (Order order : dispatchedOrders) {
                bufferedWriter.write(order.getId() + ", " + order.getStatus() + ", " + order.getRestaurant().getId() + ", " + order.getCustomer().getId() + ", " + order.getCreateTime() + ", " + order.getIsPayed() + ", " + order.getOrderedDishes() + ", " + order.getRider().getId() + ", " + order.getEstimatedTime() + "\n");
            }
        }
    }

    /// This function is to simulate the delivery process of riders.
    public void handleOrderDelivery() {

    }

    public static void main(String[] args) {
        try {
            DispatchSystem dispatchSystem = DispatchSystem.getInstance();
            dispatchSystem.parseAccounts("Accounts.txt");
            dispatchSystem.parseDishes("Dishes.txt");
            dispatchSystem.parseOrders("Orders.txt");

            dispatchSystem.dispatchFirstRound();

            dispatchSystem.writeDispatchedOrders("DispatchedOrders.txt");
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        Account.AccountManager accountManager = Account.getAccountManager();
        // Parse the first-round orders.
        System.out.println("Hello world!");
    }

}
