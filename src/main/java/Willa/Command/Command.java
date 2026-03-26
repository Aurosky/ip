package Willa.Command;

import Willa.Storage.Storage;
import Willa.Ui.Ui;
import Willa.Exception.WillaException;
import Willa.Tasks.TaskList;

/**
 * Abstract base class for all commands.
 * Each concrete command implements execute() to perform its specific action.
 */
public abstract class Command {
    /**
     * Executes the command.
     * @param tasks  The task list to operate on
     * @param ui     The UI instance for output
     * @param storage The storage instance for persistence
     * @throws WillaException if execution fails
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws WillaException;
    
    /**
     * Returns whether this command should exit the program.
     * Override in ByeCommand to return true.
     */
    public boolean isExit() {
        return false;
    }
}

