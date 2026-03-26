package Willa.Command;

import Willa.Exception.WillaException;
import Willa.Storage.Storage;
import Willa.Tasks.TaskList;
import Willa.Ui.Ui;

/**
 * Lists all tasks currently in the task list.
 */
public class listCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws WillaException {
        ui.showMessage("Here are the tasks in your list:");
        for (int i = 0; i < tasks.getSize(); i++) {
            ui.showMessage((i + 1) + "." + tasks.getAllTasks().get(i));
        }
    }
}

