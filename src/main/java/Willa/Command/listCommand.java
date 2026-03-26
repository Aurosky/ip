package Willa.Command;

import Willa.Exception.WillaException;
import Willa.Storage.Storage;
import Willa.Tasks.TaskList;
import Willa.Ui.Ui;

/**
 * Command to display all tasks currently stored in the task list.
 */
public class listCommand extends Command {
    
    /**
     * Executes the listing logic by iterating through the task list and printing each task via the UI.
     * @param tasks   The task list to be displayed.
     * @param ui      The user interface to output the list.
     * @param storage The storage component (not used in this command).
     * @throws WillaException If an error occurs during execution.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws WillaException {
        ui.showMessage("Here are the tasks in your list:");
        for (int i = 0; i < tasks.getSize(); i++) {
            ui.showMessage((i + 1) + "." + tasks.getAllTasks().get(i));
        }
    }
}
