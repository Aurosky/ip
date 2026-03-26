package Willa.Command;

import Willa.Exception.WillaException;
import Willa.Tasks.*;
import Willa.Storage.Storage;
import Willa.Ui.Ui;

/**
 * Command to add an event task with a specified start and end time to the task list.
 */
public class eventCommand extends Command {
    private final String description;
    private final String from;
    private final String to;
    
    /**
     * Private constructor for eventCommand.
     * @param description Description of the event.
     * @param from The start time string.
     * @param to The end time string.
     */
    private eventCommand(String description, String from, String to) {
        this.description = description;
        this.from = from;
        this.to = to;
    }
    
    /**
     * Parses the arguments to extract description, start time, and end time.
     * @param args The raw command arguments containing /from and /to delimiters.
     * @return A new eventCommand instance.
     * @throws WillaException If any required part of the command is missing.
     */
    public static eventCommand of(String args) throws WillaException {
        String[] parts = args.split(" /from | /to ", 3);
        if (parts.length < 3) {
            throw new WillaException("Use: event <desc> /from <start> /to <end> (format: yyyy-MM-dd HHmm)");
        }
        return new eventCommand(parts[0].trim(), parts[1].trim(), parts[2].trim());
    }
    
    /**
     * Parses the date strings, creates an Event task, and updates the list and storage.
     * @param tasks   The task list to update.
     * @param ui      The user interface to provide feedback.
     * @param storage The storage component for persistence.
     * @throws WillaException If the date/time format is invalid.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws WillaException {
        Event task = new Event(description, Event.parseDateTime(from), Event.parseDateTime(to));
        tasks.addTask(task);
        ui.showMessage("Got it. I've added:\n    " + task
                + "\n     Now you have " + tasks.getSize() + " tasks.");
        storage.save(tasks.getAllTasks());
    }
}
