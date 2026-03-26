package Willa.Command;

import Willa.Exception.WillaException;
import Willa.Storage.Storage;
import Willa.Tasks.Task;
import Willa.Tasks.TaskList;
import Willa.Ui.Ui;

public class markCommand extends Command {
    private final int index;
    private final boolean isDone;
    
    private markCommand(int index, boolean isDone) {
        this.index = index;
        this.isDone = isDone;
    }
    
    public static markCommand of(String args, boolean isDone) throws WillaException {
        if (args.isEmpty()) throw new WillaException("Missing task number.");
        try {
            return new markCommand(Integer.parseInt(args) - 1, isDone);
        } catch (NumberFormatException e) {
            throw new WillaException("Invalid task number.");
        }
    }
    
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws WillaException {
        if (index < 0 || index >= tasks.getSize()) throw new WillaException("Index out of bounds.");
        Task t = tasks.markTask(index, isDone);
        ui.showMessage((isDone ? "Done: " : "Not done: ") + t);
        storage.save(tasks.getAllTasks());
    }
}

