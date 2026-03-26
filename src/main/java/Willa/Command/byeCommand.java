package Willa.Command;

import Willa.Storage.Storage;
import Willa.Ui.Ui;
import Willa.Exception.WillaException;
import Willa.Tasks.TaskList;

/**
 * Prints the farewell message and signals the main loop to exit.
 */
public class byeCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws WillaException {
        ui.showMessage("Bye. Hope to see you again soon!");
    }
    
    @Override
    public boolean isExit() {
        return true;
    }
}
