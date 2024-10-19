package hk.ust.comp3021;

import org.junit.jupiter.api.Test;

import java.io.*;
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

                if (same && reader1.readLine() == null) {
                    if (reader2.readLine() != null) {
                        same = false;
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
            try {
                DispatchSystem dispatchSystem = DispatchSystem.getInstance();
                dispatchSystem.parseAccounts("Accounts" + testCaseNumber + ".txt");
                dispatchSystem.parseDishes("Dishes" + testCaseNumber + ".txt");
                dispatchSystem.parseOrders("Orders" + testCaseNumber + ".txt");
                List<Account> allAccounts = dispatchSystem.getAccounts();
                dispatchSystem.writeAccounts("AccountsTest" + testCaseNumber + ".txt", allAccounts);
                boolean p11 = testIfFilesContentSame("AccountsTest" + testCaseNumber + ".txt", "AccountsTest" + testCaseNumber + "Oracle.txt");
                if (p11) {
                    addTotal(20);
                }

                List<Dish> allDishes = dispatchSystem.getDishes();
                dispatchSystem.writeDishes("DishesTest" + testCaseNumber + ".txt", allDishes);
                boolean b12 = testIfFilesContentSame("DishesTest" + testCaseNumber + ".txt", "DishesTest" + testCaseNumber + "Oracle.txt");
                if (b12) {
                    addTotal(10);
                }

                List<Order> allOrders = dispatchSystem.getAvailableOrders();
                dispatchSystem.writeOrders("OrdersTest" + testCaseNumber + ".txt", allOrders);
                boolean b13 = testIfFilesContentSame("OrdersTest" + testCaseNumber + ".txt", "OrdersTest" + testCaseNumber + "Oracle.txt");
                if (b13) {
                    addTotal(10);
                }

                dispatchSystem.dispatchFirstRound();

                dispatchSystem.writeOrders("FirstRoundDispatchedOrdersTest" + testCaseNumber + ".txt", dispatchSystem.getDispatchedOrders());
                boolean p2 = testIfFilesContentSame("FirstRoundDispatchedOrdersTest" + testCaseNumber + ".txt", "FirstRoundDispatchedOrdersTest" + testCaseNumber + "Oracle.txt");
                if (p2) {
                    addTotal(50);
                }

                List<Order> timeoutOrders = dispatchSystem.getTimeoutDispatchedOrders();
                dispatchSystem.writeOrders("TimeoutDispatchedOrdersTest" + testCaseNumber + ".txt", timeoutOrders);
                boolean p3 = testIfFilesContentSame("TimeoutDispatchedOrdersTest" + testCaseNumber + ".txt", "TimeoutDispatchedOrdersTest" + testCaseNumber + "Oracle.txt");
                if (p3) {
                    addTotal(10);
                }

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
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Grade: " + counter.calculateGrade());

        counter.print();
    }

    @Test
    void test1() {
        GradeCounter counter = new GradeCounter();
        try {
            counter.gradeTest1();
            counter.print();
            System.out.println("Grade: " + counter.calculateGrade());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("grades.txt"))) {
            bufferedWriter.write(counter.getTotal() + "");
//            bufferedWriter.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Test
    void test2() {
        GradeCounter counter = new GradeCounter();
        try {
            counter.gradeTest2();
            counter.print();
            System.out.println("Grade: " + counter.calculateGrade());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("grades.txt", true))) {
            bufferedWriter.write(" " + counter.getTotal());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    void test3() {
        GradeCounter counter = new GradeCounter();
        try {
            counter.gradeTest3();
            counter.print();
            System.out.println("Grade: " + counter.calculateGrade());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("grades.txt", true))) {
            bufferedWriter.write(" " + counter.getTotal());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    void test4() {
        GradeCounter counter = new GradeCounter();
        try{
            counter.gradeTest4();
            counter.print();
            System.out.println("Grade: " + counter.calculateGrade());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("grades.txt", true))) {
            bufferedWriter.write(" " + counter.getTotal());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    void test5() {
        GradeCounter counter = new GradeCounter();
        try {
            counter.gradeTest5();
            counter.print();
            System.out.println("Grade: " + counter.calculateGrade());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("grades.txt", true))) {
            bufferedWriter.write(" " + counter.getTotal());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    void test6() {
        GradeCounter counter = new GradeCounter();
        try {
            counter.gradeTest6();
            counter.print();
            System.out.println("Grade: " + counter.calculateGrade());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("grades.txt", true))) {
            bufferedWriter.write(" " + counter.getTotal());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    void test7() {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("grades.txt"))) {
            String line = bufferedReader.readLine();
            String[] grades = line.split(" ");
            assert grades.length == 6;

            Integer[] intGrades = new Integer[grades.length];
            for (int i = 0; i < grades.length; i++) {
                intGrades[i] = Integer.parseInt(grades[i]);
            }

            double finalGrade = 0L;

            for (int i = 0; i < intGrades.length; i++) {
                finalGrade += intGrades[i];
            }

            finalGrade /= intGrades.length;

            System.out.println("Final Grade: " + finalGrade);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
