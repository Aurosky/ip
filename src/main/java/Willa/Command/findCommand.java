package Willa.Command;

import java.util.Map;

import Willa.Storage.Storage;
import Willa.Ui.Ui;
import Willa.Exception.WillaException;
import Willa.Tasks.Task;
import Willa.Tasks.TaskList;

/**
 * Command to search for and display tasks that match a specific keyword.
 */
public class findCommand extends Command {
    private final String keyword;
    
    /**
     * Constructs a findCommand with the specified search term.
     * @param keyword The search keyword (case-insensitive).
     */
    public findCommand(String keyword) {
        this.keyword = keyword;
    }
    
    /**
     * Executes the search, retrieves matching tasks from the list, and displays them via the UI.
     * @param tasks   The task list to search within.
     * @param ui      The user interface to display the results.
     * @param storage The storage component (not used in this command).
     * @throws WillaException If a search-related error occurs.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws WillaException {
        Map<Integer, Task> matchingTasks = tasks.findTasksByKeyword(keyword);
        ui.showMessage("Here are the matching tasks in your list:");
        if (matchingTasks.isEmpty()) {
            ui.showMessage("  No matching tasks found.");
        } else {
            for (Map.Entry<Integer, Task> entry : matchingTasks.entrySet()) {
                ui.showMessage("  " + entry.getKey() + "." + entry.getValue());
            }
        }
    }
}
