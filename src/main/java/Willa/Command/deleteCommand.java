package Willa.Command;

import Willa.Exception.WillaException;
import Willa.Storage.Storage;
import Willa.Tasks.Task;
import Willa.Tasks.TaskList;
import Willa.Ui.Ui;

/**
 * Command to remove a task from the task list based on its index.
 */
public class deleteCommand extends Command {
    private final int index;
    
    /**
     * Private constructor for deleteCommand.
     * @param index Zero-based index of the task to be deleted.
     */
    private deleteCommand(int index) {
        this.index = index;
    }
    
    /**
     * Parses the task number from arguments and creates a deleteCommand instance.
     * @param args The task number as a string.
     * @return A new deleteCommand instance.
     * @throws WillaException If the argument is empty or not a valid integer.
     */
    public static deleteCommand of(String args) throws WillaException {
        if (args.isEmpty()) {
            throw new WillaException("Please specify a task number to delete.");
        }
        try {
            return new deleteCommand(Integer.parseInt(args) - 1);
        } catch (NumberFormatException e) {
            throw new WillaException("Invalid task number for deletion.");
        }
    }
    
    /**
     * Executes the task deletion, updates the UI, and persists the change.
     * @param tasks   The task list to modify.
     * @param ui      The user interface to display feedback.
     * @param storage The storage component to save the updated list.
     * @throws WillaException If the index is out of bounds.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws WillaException {
        Task removed = tasks.deleteTask(index);
        ui.showMessage("Noted. I've removed this task:\n    " + removed);
        ui.showMessage("Now you have " + tasks.getSize() + " tasks in the list.");
        storage.save(tasks.getAllTasks());
    }
}
