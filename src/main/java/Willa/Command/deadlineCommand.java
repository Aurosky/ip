package Willa.Command;

import Willa.Exception.WillaException;
import Willa.Storage.Storage;
import Willa.Tasks.Deadline;
import Willa.Tasks.TaskList;
import Willa.Ui.Ui;

public class deadlineCommand extends Command {
    private final String description;
    private final String by;
    
    private deadlineCommand(String description, String by) {
        this.description = description;
        this.by = by;
    }
    
    public static deadlineCommand of(String args) throws WillaException {
        String[] parts = args.split(" /by ", 2);
        if (parts.length < 2) throw new WillaException("Use: deadline <desc> /by <time> " +
                "(format: yyyy-MM-dd or yyyy-MM-dd HHmm)");
        
        return new deadlineCommand(parts[0].trim(), parts[1].trim());
    }
    
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws WillaException {
        Deadline task = new Deadline(description, Deadline.parseBy(by));
        tasks.addTask(task);
        ui.showMessage("Added: " + task);
        storage.save(tasks.getAllTasks());
    }
}
