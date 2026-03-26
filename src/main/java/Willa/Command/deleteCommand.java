package Willa.Command;

import Willa.Exception.WillaException;
import Willa.Storage.Storage;
import Willa.Tasks.Task;
import Willa.Tasks.TaskList;
import Willa.Ui.Ui;

public class deleteCommand extends Command {
    private final int index;
    
    private deleteCommand(int index) {
        this.index = index;
    }
    
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
    
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws WillaException {
        Task removed = tasks.deleteTask(index);
        ui.showMessage("Noted. I've removed this task:\n    " + removed);
        ui.showMessage("Now you have " + tasks.getSize() + " tasks in the list.");
        storage.save(tasks.getAllTasks());
    }
}
