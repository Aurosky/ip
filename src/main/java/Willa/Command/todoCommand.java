package Willa.Command;

import Willa.Exception.WillaException;
import Willa.Storage.Storage;
import Willa.Tasks.TaskList;
import Willa.Tasks.Todo;
import Willa.Ui.Ui;

/**
 * Command to add a basic Todo task without any date or time constraints to the task list.
 */
public class todoCommand extends Command {
    private final String description;
    
    /**
     * Private constructor for todoCommand.
     * @param description The content of the todo task.
     */
    private todoCommand(String description) {
        this.description = description;
    }
    
    /**
     * Validates arguments and creates a new todoCommand instance.
     * @param args The task description string.
     * @return A new todoCommand instance.
     * @throws WillaException If the description is empty.
     */
    public static todoCommand of(String args) throws WillaException {
        if (args.isEmpty()) throw new WillaException("Todo description cannot be empty.");
        return new todoCommand(args);
    }
    
    /**
     * Creates the Todo task, adds it to the list, displays a confirmation, and saves the data.
     * @param tasks   The task list to update.
     * @param ui      The user interface for feedback.
     * @param storage The storage component for persistence.
     * @throws WillaException If an execution error occurs.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws WillaException {
        Todo task = new Todo(description);
        tasks.addTask(task);
        ui.showMessage("Added: " + task);
        storage.save(tasks.getAllTasks());
    }
}
