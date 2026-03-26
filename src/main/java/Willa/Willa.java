package Willa;

import Willa.Command.Command;
import Willa.Ui.Ui;
import Willa.Exception.WillaException;
import Willa.Parser.Parser;
import Willa.Storage.Storage;
import Willa.Tasks.TaskList;

/**
 * The main class of the Willa task management program, responsible for initializing core components
 * and starting the main program loop.
 * Integrates components such as Storage, TaskList, Ui, and Parser to provide complete task management functionality.
 */
public class Willa {
    /** Data Persistence Component Instance */
    private Storage storage;
    
    /** Task List Management Component Instance */
    private TaskList tasks;
    
    /** UI Interaction Component Instance */
    private Ui ui;
    
    /**
     * Constructs a Willa program instance, initializes core components, and loads historical tasks.
     * @param filePath The storage path for task data files
     */
    public Willa(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (WillaException e) {
            ui.showError("Loading error. Starting with empty list.");
            tasks = new TaskList();
        }
    }
    
    /**
     * The main loop that starts the Willa program, handling user input until the bye command is entered to exit.
     */
    public void run() {
        ui.showWelcome();
        ui.showLine();
        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                ui.showLine();
                Command command = Parser.parse(fullCommand);
                command.execute(tasks, ui, storage);
                isExit = command.isExit();
            } catch (Exception e) {
                ui.showError(e.getMessage());
            }
            ui.showLine();
        }
    }
    
    /**
     * Entry point method for the program. Creates a Willa instance and starts the program.
     * @param args Command-line arguments (not used)
     */
    public static void main(String[] args) {
        new Willa("./data/willa.txt").run();
    }
}
