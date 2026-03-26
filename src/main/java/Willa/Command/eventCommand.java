package Willa.Command;

import Willa.Exception.WillaException;
import Willa.Tasks.*;
import Willa.Storage.Storage;
import Willa.Ui.Ui;

public class eventCommand extends Command {
    private final String description;
    private final String from;
    private final String to;
    
    private eventCommand(String description, String from, String to) {
        this.description = description;
        this.from = from;
        this.to = to;
    }
    
    public static eventCommand of(String args) throws WillaException {
        String[] parts = args.split(" /from | /to ", 3);
        if (parts.length < 3) {
            throw new WillaException("Use: event <desc> /from <start> /to <end> (format: yyyy-MM-dd HHmm)");
        }
        return new eventCommand(parts[0].trim(), parts[1].trim(), parts[2].trim());
    }
    
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws WillaException {
        Event task = new Event(description, Event.parseDateTime(from), Event.parseDateTime(to));
        tasks.addTask(task);
        ui.showMessage("Got it. I've added:\n    " + task
                + "\n     Now you have " + tasks.getSize() + " tasks.");
        storage.save(tasks.getAllTasks());
    }
}
