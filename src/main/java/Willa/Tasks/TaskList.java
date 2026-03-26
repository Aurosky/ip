package Willa.Tasks;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import Willa.Exception.WillaException;


public class TaskList {
    private List<Task> tasks;
    
    public TaskList() {
        this.tasks = new ArrayList<>();
    }
    
    public TaskList(List<Task> tasks) {
        this.tasks = tasks;
    }
    
    public List<Task> getAllTasks() {
        return tasks;
    }
    
    public int getSize() {
        return tasks.size();
    }
    
    public void addTask(Task task) {
        tasks.add(task);
    }
    
    public Task deleteTask(int index) throws WillaException {
        validateIndex(index);
        return tasks.remove(index);
    }
    
    public Task markTask(int index, boolean isDone) throws WillaException {
        validateIndex(index);
        Task t = tasks.get(index);
        if (isDone) t.markAsDone();
        else t.markAsNotDone();
        return t;
    }
    
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
    
    private void validateIndex(int index) throws WillaException {
        if (index < 0 || index >= tasks.size()) {
            throw new WillaException("Task index out of range.");
        }
    }
}