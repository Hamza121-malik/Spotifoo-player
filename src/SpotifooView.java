import java.util.ArrayList;
import java.util.List;

public class SpotifooView {
    
    public static void print(String value, boolean newline) {
        if (newline) {
            System.out.println(value);
        } else {
            System.out.print(value);
        }
    }

    public void showMainMenu(List<String> options) {
        print("Main Menu Options:", true);
        options.forEach(value -> print(value,true));
    }

    public void showOptionPrompt() {
        print("Choose an option and press Enter:", false);
    }

    public void showWelcomeMessage() {
        print("\033[0;32m" + "Welcome to the Spotifoo Music Player!" + "\033[0m", true);
        print("", true);
    }

    public static void showWarning() {
        print("\u001B[33m" + "!!!! Wrong Option Selected !!!!" + "\u001B[0m", true);
    }

    public static void showError() {
        print("\u001B[31m" + "!!!! This Song Could not be played !!!!" + "\u001B[0m", true);
    }

     public void flushScreen() {
        print("\033[4H\033[0J", false);
        System.out.flush();
    }
}
