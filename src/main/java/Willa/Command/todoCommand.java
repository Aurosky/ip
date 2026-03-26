package Willa.Command;

import Willa.Exception.WillaException;
import Willa.Storage.Storage;
import Willa.Tasks.TaskList;
import Willa.Tasks.Todo;
import Willa.Ui.Ui;

public class todoCommand extends Command {
    private final String description;
    
    private todoCommand(String description) { // 设为私有，强制使用 of()
        this.description = description;
    }
    
    public static todoCommand of(String args) throws WillaException {
        if (args.isEmpty()) throw new WillaException("Todo description cannot be empty.");
        return new todoCommand(args);
    }
    
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws WillaException {
        Todo task = new Todo(description);
        tasks.addTask(task);
        ui.showMessage("Added: " + task);
        storage.save(tasks.getAllTasks());
    }
}