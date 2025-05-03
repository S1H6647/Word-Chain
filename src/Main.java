import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    static ExecutorService executor = Executors.newSingleThreadExecutor();
    static Set<String> words = new HashSet<>();
    static char lastChar1, lastChar2;

    static void enterWord(char lastChar){
        System.out.println("****************************************");
        System.out.print("Enter the word starting with: '"+ lastChar + "'\n");
        System.out.println("****************************************");
        System.out.print("=> ");
    }

    static String getInputWithTimeout(){
        Future<String> future = executor.submit(() -> scanner.nextLine().trim().toLowerCase().replaceAll("[^a-z]",""));
        try{
            return future.get(5, TimeUnit.SECONDS);
        }
        catch(Exception e){
            future.cancel(true);
            return null;
        }
    }

    static boolean validWord(String word) {
        if (word.length() != 5) {
            System.out.println("❌ Invalid. Please enter a word of 5 letters!");
        } else if (words.contains(word)) {
            System.out.println("❌ Word already used. Try again!");
        } else {
            lastChar1 = word.charAt(4);
            words.add(word);
            return true;
        }
        return false;
    }

    static boolean validWord(String word, char lastChar){
        if (word.length() != 5){
            System.out.println("❌ Invalid. Please enter a word of 5 letters!");
        }
        else if (words.contains(word)){
            System.out.println("❌ Word already used. Try again!");
        }
        else if (!word.startsWith(String.valueOf(lastChar))) {
            System.out.println("❌ Invalid start. Word must begin with '" + lastChar + "'.");
        }
        else {
            lastChar1 = word.charAt(4);
            words.add(word);
            return true;
        }
        return false;
    }

    static void winner(int player1point, int player2point){
        System.out.println("\n======Game Over======");
        System.out.println("Final Score:");
        System.out.println("Player1: " + player1point);
        System.out.println("Player2: " + player2point);
        if (player1point > player2point){
            System.out.println("🏆 Player1 is the winner!");
        }
        else if (player1point < player2point){
            System.out.println("🏆 Player2 is the winner!");
        }
        else {
            System.out.println("The game is a tie!");
        }
    }

    static boolean continuePlaying(){
        System.out.print("\nDo you want to continue playing? (Enter or Y to continue): ");
        String input = scanner.nextLine().trim().toLowerCase();
        if (input.isEmpty() || input.equals("y")) {
            System.out.println("Starting new game...");
            return true;
        }
        else return false;
    }

    static void playGame(){
        String player1, player2;
        int player1point = 0;
        int player2point = 0;

        words.clear();

        while (true) {
            System.out.print("Enter an English word containing 5 letters: ");
            player1 = scanner.nextLine().trim().toLowerCase().replaceAll("[^a-z]","");
            if (player1.equals("exit")){
                System.out.println("👋 Exiting the game...");
                System.exit(0);
            }
            if (validWord(player1)){
                player1point++;
                break;
            }
        }

        while (true){
            while (true){
                System.out.println("\nLast word: "+ player1);
                System.out.println(words);
                enterWord(lastChar1);

                player2 = getInputWithTimeout();

                if (player2 == null) {
                    System.out.println("\nGame Over! Time out");
                    winner(player1point, player2point);

                }
                else {
                    if (player2.equals("exit")){
                        winner(player1point, player2point);
                        return;
                    }
                    if(validWord(player2,lastChar1)){
                        player2point++;
                        lastChar2 = player2.charAt(4);
                        break;
                    }
                }
            }

            while (true){
                System.out.println("\nLast word: " + player2);
                System.out.println(words);
                enterWord(lastChar2);

                player1 = getInputWithTimeout();
                if (player1 == null) {
                    System.out.println("\nGame Over! Timeout");
                    winner(player1point, player2point);
                    System.exit(0);
                }
                else {
                    if (player1.equals("exit")){
                        winner(player1point, player2point);
                        return;
                    }
                    if (validWord(player1)){
                        player1point++;
                        System.out.println("\nLast word: " + player1);
                        break;
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("*******************************");
        System.out.println("WELCOME TO JAVA WORD CHAIN GAME");
        System.out.println("*******************************\n");
        System.out.println("Note: You have 5s to enter the word!");
        System.out.println("Note: Type exit to exit the game!");

        do {
            playGame();
        } while (continuePlaying());

        System.out.println("👋 Thank you for playing!");
        executor.shutdownNow();
        try {
            if (!executor.awaitTermination(2, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
    }
}