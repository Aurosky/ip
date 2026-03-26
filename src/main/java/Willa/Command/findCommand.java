package Willa.Command;

import java.util.Map;

import Willa.Storage.Storage;
import Willa.Ui.Ui;
import Willa.Exception.WillaException;
import Willa.Tasks.Task;
import Willa.Tasks.TaskList;

/**
 * Searches for tasks whose descriptions contain the given keyword.
 */
public class findCommand extends Command {
    private final String keyword;
    
    /**
     * @param keyword The search keyword (case-insensitive)
     */
    public findCommand(String keyword) {
        this.keyword = keyword;
    }
    
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

