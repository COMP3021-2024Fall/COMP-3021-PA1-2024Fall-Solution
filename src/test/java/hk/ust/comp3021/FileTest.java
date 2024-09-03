package hk.ust.comp3021;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FileTest {

    @Test
    void testIfFilesContentSame() {

        boolean same = true;
        try {
            BufferedReader reader1 = new BufferedReader(new FileReader("Dishes.txt"));
            BufferedReader reader2 = new BufferedReader(new FileReader("TimeoutOrders.txt"));

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

    }
}
