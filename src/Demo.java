import java.util.Scanner;
import java.util.concurrent.*;
import java.util.concurrent.Future;

public class Demo {
    static ExecutorService executor = Executors.newSingleThreadExecutor();
    static Scanner scanner = new Scanner(System.in);

    static String getInputWithTimeout(){
        Future<String> future = executor.submit(() -> scanner.next().trim().toLowerCase());
        try {
            return future.get(3, TimeUnit.SECONDS);
        } catch (Exception e){
            future.cancel(true);
            return null;
        }
    }

    public static void main(String[] args) {
        String word;
        while (true) {
            System.out.print("Enter a word (You have 3s): ");
            word = getInputWithTimeout();

            if (word == null) {
                System.out.println("\nNo words entered");
                System.exit(0);
            } else {
                System.out.println("You entered: " + word);
            }
        }
    }
}
