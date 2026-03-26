package Willa.Command;

import Willa.Exception.WillaException;
import Willa.Storage.Storage;
import Willa.Tasks.Task;
import Willa.Tasks.TaskList;
import Willa.Ui.Ui;

/**
 * Command to mark a task as completed or not completed.
 */
public class markCommand extends Command {
    private final int index;
    private final boolean isDone;
    
    /**
     * Private constructor for markCommand.
     * @param index Zero-based index of the task.
     * @param isDone True to mark as done, false to mark as undone.
     */
    private markCommand(int index, boolean isDone) {
        this.index = index;
        this.isDone = isDone;
    }
    
    /**
     * Parses the task number and creates a markCommand instance.
     * @param args The task number string.
     * @param isDone Completion status to be set.
     * @return A new markCommand instance.
     * @throws WillaException If the argument is missing or not a valid integer.
     */
    public static markCommand of(String args, boolean isDone) throws WillaException {
        if (args.isEmpty()) throw new WillaException("Missing task number.");
        try {
            return new markCommand(Integer.parseInt(args) - 1, isDone);
        } catch (NumberFormatException e) {
            throw new WillaException("Invalid task number.");
        }
    }
    
    /**
     * Executes the status update, provides feedback, and saves the updated list.
     * @param tasks   The task list to update.
     * @param ui      The user interface for feedback.
     * @param storage The storage component for persistence.
     * @throws WillaException If the index is out of the valid task list range.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws WillaException {
        if (index < 0 || index >= tasks.getSize()) throw new WillaException("Index out of bounds.");
        Task t = tasks.markTask(index, isDone);
        ui.showMessage((isDone ? "Done: " : "Not done: ") + t);
        storage.save(tasks.getAllTasks());
    }
}
