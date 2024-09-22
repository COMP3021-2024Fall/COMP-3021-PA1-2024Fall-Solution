package hk.ust.comp3021;

import hk.ust.comp3021.rank.*;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

public class DispatchSystem {

    private static volatile DispatchSystem dispatchSystem;

    /// The field represents the current time stamp, we assume the current time stamp is 3600 seconds.
    private Long currentTimestamp = 3600L;

    /// The list stores the dishes parsed from the file.
    private List<Dish> availableDishes;

    /// The list stores the orders parsed from the file.
    private List<Order> availableOrders;

    /// The list stores the orders that is dispatched this time, and the orders should have a non-null rider field and calculated estimated time.
    private List<Order> dispatchedOrders;

    private DispatchSystem() {
        if (dispatchSystem != null) {
            throw new RuntimeException("Plase use the getInstance() method to get the singleton!");
        }

        this.availableOrders = new ArrayList<>();
        this.availableDishes = new ArrayList<>();
        this.dispatchedOrders = new ArrayList<>();
    }

    /// Task 1: Implement the getInstance() method for the singleton pattern.
    /// Hint: Try to make it thread-safe.
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

    public Boolean checkDishesInRestaurant(Restaurant restaurant, Long[] dishIds) {
        List<Long> restaurantDishIds = new ArrayList<>(restaurant.getDishes().stream().map(Dish::getId).toList());

        restaurantDishIds.retainAll(new ArrayList<>(List.of(dishIds)));

        return new HashSet<>(restaurantDishIds).containsAll(List.of(dishIds));
    }

    /// Task 2: Implement the parseAccounts() method to parse the accounts from the file.
    /// Hint: Do not forget to register the accounts into the static manager.
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
                if (accountType.equals(Constants.ACCOUNT_CUSTOMER)) {
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
                } else if (accountType.equals(Constants.ACCOUNT_RESTAURANT)) {
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
                } else if (accountType.equals(Constants.ACCOUNT_RIDER)) {
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

    /// Task 3: Implement the parseDishes() method to parse the dishes from the file.
    /// Hint: Do not forget to add the dishes to the corresponding restaurant and the availableDishes list.
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

    /// Task 4: Implement the parseOrders() method to parse the orders from the file.
    /// Hint: Do not forget to add the order to the availableOrders list and check if the dishes ordered are in the same restaurant. You can use getDishById(), checkDishesInRestaurant(), and etc. here.
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

                if (!checkDishesInRestaurant(restaurant, dishIds)) {
                    continue;
                }

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

    /// Task 5: Implement the getAvailablePendingOrders() method to get the available pending orders.
    /// Hint: The available pending orders should have the status of PENDING_ORDER, is payed, and the rider is null.
    public List<Order> getAvailablePendingOrders() {
        return availableOrders.stream().filter(order -> order.getStatus().equals(Constants.PENDING_ORDER) && order.getIsPayed() && order.getRider() == null).toList();
    }

    /// Task 6: Implement the getRankedPendingOrders() method to rank the pending orders.
    /// Hint: Use the comparators you defined before, and sort the pending orders in order of the customer type, order create time, and restaurant to customer distance.
    public List<Order> getRankedPendingOrders(List<Order> pendingOrders) {
        Comparator<Order> orderComparator = new CustomerPriorityRank()
                .thenComparing(new OrderCreateTimeRank())
                .thenComparing(new RestaurantToCustomerDistanceRank());

        return pendingOrders.stream().sorted(orderComparator).toList();
    }

    /// Task 7: Implement the getAvailableRiders() method to get the available riders to dispatch.
    /// Hint: The available riders should have the status of RIDER_ONLINE_ORDER.
    public List<Rider> getAvailableRiders() {
        return Rider.getRiders().stream().filter(rider -> rider.getStatus().equals(Constants.RIDER_ONLINE_ORDER)).toList();
    }

    /// Task 8: Implement the matchTheBestTask() method to choose the best rider for the order.
    /// Hint: The best rider should have the highest rank ranked in order of the distance between the rider and the restaurant, the rider's rating, and the rider's month task count.
    /// Use the comparators you defined before and you will use the Task class here.
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

        List<Task> tmpTaskList = tmpTasks.stream().sorted(taskComparator).toList();
        return tmpTaskList.stream().findFirst().orElse(null);
    }

    /// Task 9: Implement the dispatchFirstRound() method to dispatch the first round of orders.
    /// Hint: The strategy is that we assign the best rider to the orders ranked one by one until the orders or riders list is empty.
    /// Do not forget to change the status of the order and the rider, and calculate the estimated time for the order.
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

        availableRiders = tmpRiders;

    }

    /// You should use the method to output orders for us to check the correctness of your implementation.
    public void writeOrders(String fileName, List<Order> orders) throws IOException {
        List<Order> orderedOrders = orders.stream().sorted(new Comparator<Order>() {
            @Override
            public int compare(Order o1, Order o2) {
                return o1.getId().compareTo(o2.getId());
            }
        }).toList();

        // Write the dispatched orders to the file.
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName))) {
            for (Order order : orderedOrders) {
                bufferedWriter.write(order.getId() + ", " + order.getStatus() + ", " + order.getRestaurant() + ", "
                        + order.getCustomer() + ", " + order.getCreateTime() + ", " + order.getIsPayed() + ", " +
                        order.getOrderedDishes() + ", " + order.getRider() + ", " + String.format("%.4f", order.getEstimatedTime()) + "\n");
            }
        }
    }

    public void writeAccounts(String fileName, List<Account> accounts) throws IOException {
        List<Account> orderedAccounts = accounts.stream().sorted(new Comparator<Account>() {
            @Override
            public int compare(Account o1, Account o2) {
                return o1.getId().compareTo(o2.getId());
            }
        }).toList();

        // Write the dispatched orders to the file.
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName))) {
            for (Account account : orderedAccounts) {
                bufferedWriter.write(account.toString() + "\n");
            }
        }
    }

    public void writeDishes(String fileName, List<Dish> dishes) throws IOException {
        List<Dish> orderedDishes = dishes.stream().sorted(new Comparator<Dish>() {
            @Override
            public int compare(Dish o1, Dish o2) {
                return o1.getId().compareTo(o2.getId());
            }
        }).toList();

        // Write the dispatched orders to the file.
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName))) {
            for (Dish dish : orderedDishes) {
                bufferedWriter.write(dish.getId() + ", " + dish.getName() + ", " + dish.getDesc() + ", "
                        + dish.getPrice() + ", " + dish.getRestaurantId() + "\n");
            }
        }
    }

    /// Task 10: Implement the getTimeoutDispatchedOrders() method to get the timeout dispatched orders.
    /// Hint: Do not forget to take the current time stamp into consideration.
    public List<Order> getTimeoutDispatchedOrders() {
        return dispatchedOrders.stream().filter(order -> (order.getEstimatedTime() + currentTimestamp - order.getCreateTime()) > Constants.DELIVERY_TIME_LIMIT).toList();
    }

    /// Do not modify the function.
    public List<Order> getAvailableOrders() {
        return availableOrders;
    }

    /// Do not modify the function.
    public List<Order> getDispatchedOrders() {
        return dispatchedOrders;
    }

    public List<Account> getAccounts() {
        Account.AccountManager manager = Account.getAccountManager();
        return manager.getRegisteredAccounts();
    }

    public List<Dish> getDishes() {
        return availableDishes;
    }

    /// Finish the main method to test your implementation.a
    public static void main(String[] args) {
        /**
        try {
            DispatchSystem dispatchSystem = DispatchSystem.getInstance();
            dispatchSystem.parseAccounts("Accounts.txt");
            dispatchSystem.parseDishes("Dishes.txt");
            dispatchSystem.parseOrders("Orders.txt");
            dispatchSystem.writeOrders("availableOrders.txt", dispatchSystem.availableOrders);

            dispatchSystem.dispatchFirstRound();

            dispatchSystem.writeOrders("firstRoundDispatchedOrders.txt", dispatchSystem.dispatchedOrders);
            List<Order> timeoutOrders = dispatchSystem.getTimeoutDispatchedOrders();

            dispatchSystem.writeOrders("timeoutDispatchedOrders.txt", timeoutOrders);

        } catch (IOException exception) {
            exception.printStackTrace();
        }
         */

        try {
            DispatchSystem dispatchSystem = DispatchSystem.getInstance();
            dispatchSystem.parseAccounts("Accounts.txt");
            dispatchSystem.parseDishes("Dishes.txt");
            dispatchSystem.parseOrders("Orders.txt");
            List<Account> allAccounts = dispatchSystem.getAccounts();
            dispatchSystem.writeAccounts("AccountsTest1.txt", allAccounts);

            List<Dish> allDishes = dispatchSystem.getDishes();
            dispatchSystem.writeDishes("DishesTest1.txt", allDishes);

            List<Order> allOrders = dispatchSystem.getAvailableOrders();
            dispatchSystem.writeOrders("OrdersTest1.txt", allOrders);

            dispatchSystem.dispatchFirstRound();

            dispatchSystem.writeOrders("firstRoundDispatchedOrdersTest1.txt", dispatchSystem.getDispatchedOrders());

            List<Order> timeoutOrders = dispatchSystem.getTimeoutDispatchedOrders();
            dispatchSystem.writeOrders("timeoutDispatchedOrdersTest1.txt", timeoutOrders);
        } catch (IOException exception) {
            exception.printStackTrace();
        }

    }

}
