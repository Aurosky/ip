package Willa.Ui;

import java.util.Scanner;

/**
 * Interface interaction class responsible for handling user input and program output,
 * providing a unified console interaction style.
 */
public class Ui {
    /** A separator line for console output, used to distinguish different interaction content */
    private static final String LINE = "    ____________________________________________________________";
    /** Scanner instance used to read user input */
    private Scanner scanner;
    
    /**
     * Constructs a Ui instance and initializes a Scanner to read user input.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }
    
    /**
     * Reads the command string entered by the user from the console.
     * @return The complete command entered by the user
     */
    public String readCommand() {
        return scanner.nextLine();
    }
    
    /**
     * Prints a separator line in the console to enhance interactive visual effects.
     */
    public void showLine() {
        System.out.println(LINE);
    }
    
    /**
     * Displays the welcome screen when the program starts, featuring the logo and welcome message.
     */
    public void showWelcome() {
        String logo = " __        ___  _ _\n"
                + " \\ \\      / (_) | | __ _\n"
                + "  \\ \\ /\\ / /| | | |/ _` |\n"
                + "   \\ V  V / | | | | (_| |\n"
                + "    \\_/\\_/  |_|_|_|\\__,_|\n";
        System.out.println(LINE + "\n     Hello from\n" + logo + "\n     What can I do for you?");
    }
    
    /**
     * Displays error messages in the console, standardizing the error output format.
     * @param message The specific content of the error message
     */
    public void showError(String message) {
        System.out.println("     [OOPS] " + message);
    }
    
    /**
     * Displays general prompt messages in the console, standardizing the output format.
     * @param message The specific content of the general prompt
     */
    public void showMessage(String message) {
        System.out.println("     " + message);
    }
}
