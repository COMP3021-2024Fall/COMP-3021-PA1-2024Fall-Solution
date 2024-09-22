package hk.ust.comp3021;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class FileTest {

    private static class GradeCounter {
        private int total = 0;
        private int pass = 0;

        public void print() {
            System.out.println("Pass: " + pass + ", Total: " + total);
        }

        int getPass() {
            return pass;
        }

        int getTotal() {
            return total;
        }

        void addTotal(int subTotal) {
            this.total += subTotal;
            pass++;
        }

        Double calculateGrade() {
            return (double) total / pass;
        }

        boolean testIfFilesContentSame(String file1, String file2) {

            boolean same = true;
            try {
                BufferedReader reader1 = new BufferedReader(new FileReader(file1));
                BufferedReader reader2 = new BufferedReader(new FileReader(file2));

                String line;

                while ((line = reader1.readLine()) != null) {
                    if (!line.equals(reader2.readLine())) {
                        same = false;
                        break;
                    }
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (!same) {
                System.out.println("Files are not same");
            } else {
                System.out.println("Files are same");
            }

            return same;
        }

        void gradeTest1Template(int testCaseNumber) {
            int subTotal = 0;
            try {
                DispatchSystem dispatchSystem = DispatchSystem.getInstance();
                dispatchSystem.parseAccounts("Accounts" + testCaseNumber + ".txt");
                dispatchSystem.parseDishes("Dishes" + testCaseNumber + ".txt");
                dispatchSystem.parseOrders("Orders" + testCaseNumber + ".txt");
                List<Account> allAccounts = dispatchSystem.getAccounts();
                dispatchSystem.writeAccounts("AccountsTest" + testCaseNumber + ".txt", allAccounts);
                boolean p11 = testIfFilesContentSame("AccountsTest" + testCaseNumber + ".txt", "AccountsTest" + testCaseNumber + "Oracle.txt");
                if (p11) {
                    subTotal += 20;
                }

                List<Dish> allDishes = dispatchSystem.getDishes();
                dispatchSystem.writeDishes("DishesTest" + testCaseNumber + ".txt", allDishes);
                boolean b12 = testIfFilesContentSame("DishesTest" + testCaseNumber + ".txt", "DishesTest" + testCaseNumber + "Oracle.txt");
                if (b12) {
                    subTotal += 10;
                }

                List<Order> allOrders = dispatchSystem.getAvailableOrders();
                dispatchSystem.writeOrders("OrdersTest" + testCaseNumber + ".txt", allOrders);
                boolean b13 = testIfFilesContentSame("OrdersTest" + testCaseNumber + ".txt", "OrdersTest" + testCaseNumber + "Oracle.txt");
                if (b13) {
                    subTotal += 10;
                }

                dispatchSystem.dispatchFirstRound();

                dispatchSystem.writeOrders("firstRoundDispatchedOrdersTest" + testCaseNumber + ".txt", dispatchSystem.getDispatchedOrders());
                boolean p2 = testIfFilesContentSame("firstRoundDispatchedOrdersTest" + testCaseNumber + ".txt", "firstRoundDispatchedOrdersTest" + testCaseNumber + "Oracle.txt");
                if (p2) {
                    subTotal += 50;
                }

                List<Order> timeoutOrders = dispatchSystem.getTimeoutDispatchedOrders();
                dispatchSystem.writeOrders("timeoutDispatchedOrdersTest" + testCaseNumber + ".txt", timeoutOrders);
                boolean p3 = testIfFilesContentSame("timeoutDispatchedOrdersTest" + testCaseNumber + ".txt", "timeoutDispatchedOrdersTest" + testCaseNumber + "Oracle.txt");
                if (p3) {
                    subTotal += 10;
                }

                addTotal(subTotal);
            } catch (IOException exception) {
                exception.printStackTrace();
            }

        }

        void gradeTest1() {
            gradeTest1Template(1);
        }
        void gradeTest2() {
            gradeTest1Template(2);
        }

        void gradeTest3() {
            gradeTest1Template(3);
        }

        void gradeTest4() {
            gradeTest1Template(4);
        }

        void gradeTest5() {
            gradeTest1Template(5);
        }

        void gradeTest6() {
            gradeTest1Template(6);
        }

        void gradeTest7() {
            gradeTest1Template(7);
        }

        void gradeTest8() {
            gradeTest1Template(8);
        }

    }

    @Test
    void generalGradeTest() {
        GradeCounter counter = new GradeCounter();
        try {
        counter.gradeTest1();
        counter.gradeTest2();
        counter.gradeTest3();
        counter.gradeTest4();
        counter.gradeTest5();
        counter.gradeTest6();
        counter.gradeTest7();
        counter.gradeTest8();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Grade: " + counter.calculateGrade());

        counter.print();
    }


}
