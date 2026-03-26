package Willa.Command;

import Willa.Exception.WillaException;
import Willa.Storage.Storage;
import Willa.Tasks.Deadline;
import Willa.Tasks.TaskList;
import Willa.Ui.Ui;

/**
 * Command to create and add a Deadline task to the task list.
 */
public class deadlineCommand extends Command {
    private final String description;
    private final String by;
    
    /**
     * Private constructor for deadlineCommand.
     * @param description The description of the deadline.
     * @param by The deadline time string.
     */
    private deadlineCommand(String description, String by) {
        this.description = description;
        this.by = by;
    }
    
    /**
     * Parses arguments and creates a new deadlineCommand instance.
     * @param args The raw command arguments.
     * @return A new deadlineCommand instance.
     * @throws WillaException If arguments are missing or incorrectly formatted.
     */
    public static deadlineCommand of(String args) throws WillaException {
        String[] parts = args.split(" /by ", 2);
        if (parts.length < 2) {
            throw new WillaException("Use: deadline <desc> /by <time> " +
                    "(format: yyyy-MM-dd or yyyy-MM-dd HHmm)");
        }
        return new deadlineCommand(parts[0].trim(), parts[1].trim());
    }
    
    /**
     * Executes the command by parsing the date, adding the task, and saving to storage.
     * @param tasks   The task list to update.
     * @param ui      The user interface for display.
     * @param storage The storage component for persistence.
     * @throws WillaException If the date format is invalid.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws WillaException {
        Deadline task = new Deadline(description, Deadline.parseBy(by));
        tasks.addTask(task);
        ui.showMessage("Added: " + task);
        storage.save(tasks.getAllTasks());
    }
}
