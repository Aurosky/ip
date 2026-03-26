package Willa.Command;

import Willa.Storage.Storage;
import Willa.Ui.Ui;
import Willa.Exception.WillaException;
import Willa.Tasks.TaskList;

/**
 * Command to terminate the application and display a farewell message.
 */
public class byeCommand extends Command {
    
    /**
     * Executes the farewell logic by displaying a goodbye message to the user.
     * * @param tasks   The list of tasks.
     * @param ui      The user interface for displaying messages.
     * @param storage The storage component (not used in this command).
     * @throws WillaException If an error occurs during execution.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws WillaException {
        ui.showMessage("Bye. Hope to see you again soon!");
    }
    
    /**
     * Signals that this command will cause the application to exit.
     * * @return true, as this is an exit command.
     */
    @Override
    public boolean isExit() {
        return true;
    }
}
