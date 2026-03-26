package Willa.Tasks;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import Willa.Exception.WillaException;

/**
 * Manages an in-memory list of tasks.
 * Provides methods to add, delete, mark, and search for tasks within the collection.
 */
public class TaskList {
    private List<Task> tasks;
    
    /**
     * Constructs an empty TaskList.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }
    
    /**
     * Constructs a TaskList with a predefined list of tasks.
     * @param tasks The initial list of Task objects.
     */
    public TaskList(List<Task> tasks) {
        this.tasks = tasks;
    }
    
    /**
     * Retrieves all tasks currently in the list.
     * @return A list containing all Task objects.
     */
    public List<Task> getAllTasks() {
        return tasks;
    }
    
    /**
     * Returns the total number of tasks in the list.
     * @return The size of the task list.
     */
    public int getSize() {
        return tasks.size();
    }
    
    /**
     * Adds a new task to the end of the list.
     * @param task The Task object to be added.
     */
    public void addTask(Task task) {
        tasks.add(task);
    }
    
    /**
     * Removes a task from the list at the specified index.
     * @param index The zero-based index of the task to be removed.
     * @return The task that was removed.
     * @throws WillaException If the index is out of bounds.
     */
    public Task deleteTask(int index) throws WillaException {
        validateIndex(index);
        return tasks.remove(index);
    }
    
    /**
     * Updates the completion status of a task at the specified index.
     * @param index The zero-based index of the task.
     * @param isDone True to mark as done, false to mark as undone.
     * @return The updated Task object.
     * @throws WillaException If the index is out of bounds.
     */
    public Task markTask(int index, boolean isDone) throws WillaException {
        validateIndex(index);
        Task t = tasks.get(index);
        if (isDone) t.markAsDone();
        else t.markAsNotDone();
        return t;
    }
    
    /**
     * Searches for tasks whose descriptions contain the specified keyword.
     * @param keyword The search term (case-insensitive).
     * @return A map where keys are 1-based indices and values are the matching tasks.
     */
    public Map<Integer, Task> findTasksByKeyword(String keyword) {
        Map<Integer, Task> matches = new LinkedHashMap<>();
        for (int i = 0; i < tasks.size(); i++) {
            Task t = tasks.get(i);
            if (t.getDescription().toLowerCase().contains(keyword.toLowerCase())) {
                matches.put(i + 1, t);
            }
        }
        return matches;
    }
    
    /**
     * Validates if the provided index is within the valid range of the task list.
     * @param index The zero-based index to check.
     * @throws WillaException If the index is invalid.
     */
    private void validateIndex(int index) throws WillaException {
        if (index < 0 || index >= tasks.size()) {
            throw new WillaException("Task index out of range.");
        }
    }
}
