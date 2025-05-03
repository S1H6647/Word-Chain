import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Demo2 {
    static Scanner scanner = new Scanner(System.in);
    static ExecutorService executor = Executors.newSingleThreadExecutor();
    static Set<String> words = new HashSet<>();
    static String lastChar1, lastChar2;


    public static String getInputWithTimeout(int seconds){
        Future<String> future = executor.submit(() -> scanner.nextLine().trim().toLowerCase());
        try{
            return future.get(seconds, TimeUnit.SECONDS);
        }
        catch(Exception e){
            future.cancel(true);
            return null;
        }
    }

    static void winner(int user1point, int user2point){
        if (user1point > user2point){
            System.out.println("User1 is the winner!");
        }
        else {
            System.out.println("User2 is the winner!");
        }
    }

    static boolean validWord(String word){
        boolean isOkay = false;
        if (word.length() != 5){
            System.out.println("❌ Invalid length. Please enter a word of 5 letters!");
        }
        else if (words.contains(word)){
            System.out.println("❌ Word already used. Try again!");
        }
        else {
            lastChar1 = word.substring(4);
            words.add(word);
            return !isOkay;
        }
        return isOkay;
    }

    static boolean validWord(String word, String lastChar){
        boolean isOkay = false;
        if (word.length() != 5){
            System.out.println("❌ Invalid length. Please enter a word of 5 letters!");
        }
        else if (words.contains(word)){
            System.out.println("❌ Word already used. Try again!");
        }
        else if (!word.startsWith(lastChar)) {
            System.out.println("❌ Invalid start. Word must begin with '" + lastChar1 + "'.");
        }
        else {
            lastChar1 = word.substring(4);
            words.add(word);
            return !isOkay;
        }
        return isOkay;
    }

    public static void main(String[] args) {
        String user1, user2;

        System.out.println("*******************************");
        System.out.println("WELCOME TO JAVA WORD CHAIN GAME");
        System.out.println("*******************************\n");

        System.out.println("Turn: User1");
        System.out.print("Enter an English word containing 5 letters: ");

        while (true){
            while (true) {
                user1 = scanner.nextLine().trim().toLowerCase();
                if (validWord(user1)){
                    break;
                }
                System.out.println("\nUser1 entered: " + user1);
            }

            while (true){
                System.out.println(words);
                System.out.println("\nTurn: User2");
                System.out.println("****************************************");
                System.out.print("Enter the word starting with: '"+ lastChar1 + "'.\n");
                System.out.println("****************************************");
                System.out.print("=> ");

                user2 = getInputWithTimeout(5);

                if (user2 == null){
                    System.out.println("\nGame Over! Time out");
                    System.exit(0);
                }
                else {
                    if (user2.length() == 5){
                        lastChar2 = user2.substring(4);
                        words.add(user2);
                        System.out.println("\nUser2 entered: "+user2);
                        break;
                    }
                    else if (!user2.startsWith(lastChar1)){
                        System.out.println("❌ Invalid start. Word must begin with '" + lastChar1 + "'.");
                    }
                    else if (words.contains(user2)){
                        System.out.println("❌ Word already used. Try again!");
                    }
                    else{
                        System.out.println("❌ Invalid length. Please enter a word of 5 letters!");
                    }
                }
            }
            System.out.println(words);
            System.out.println("Turn: User1");
            System.out.println("****************************************");
            System.out.println("Enter the word starting with: '" + lastChar2 + "'.");
            System.out.println("****************************************");
            System.out.print("=> ");
            user1 = getInputWithTimeout(10);
            if (user1 == null) {
                System.out.println("\nGame Over! Timeout");
                System.exit(0);
            }

//        winner(user1point, user2point);
        }
    }
}