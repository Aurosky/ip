package Willa.Storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import Willa.Exception.WillaException;
import Willa.Tasks.*;

/**
 * Handles data persistence by loading and saving the task list to a local file.
 * Supports Todo, Deadline, and Event task types through string-based serialization.
 */
public class Storage {
    private String filePath;
    
    /**
     * Constructs a Storage instance with a specified file path.
     * @param filePath The path to the data file (e.g., "./data/willa.txt").
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }
    
    /**
     * Loads tasks from the file. Parses each line into its respective Task object.
     * @return A list of tasks retrieved from the file.
     * @throws WillaException If the file is corrupted or contains unknown task types.
     */
    public List<Task> load() throws WillaException {
        List<Task> loadedTasks = new ArrayList<>();
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) return loadedTasks;
        
        try {
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                String[] p = line.split(" \\| ");
                Task t;
                switch (p[0]) {
                case "T" -> t = new Todo(p[2]);
                case "D" -> t = new Deadline(p[2], Deadline.parseBy(p[3]));
                case "E" -> t = new Event(p[2], Event.parseDateTime(p[3]), Event.parseDateTime(p[4]));
                default -> throw new WillaException("Unknown task type in file.");
                }
                if (p[1].equals("1")) t.markAsDone();
                loadedTasks.add(t);
            }
        } catch (Exception e) {
            throw new WillaException("Error loading file: " + e.getMessage());
        }
        return loadedTasks;
    }
    
    /**
     * Saves the current list of tasks to the file.
     * Creates the directory if it does not exist before writing.
     * @param tasks The list of Task objects to be persisted.
     */
    public void save(List<Task> tasks) {
        try {
            Path path = Paths.get(filePath);
            if (path.getParent() != null) Files.createDirectories(path.getParent());
            List<String> lines = new ArrayList<>();
            for (Task t : tasks) {
                StringBuilder sb = new StringBuilder();
                String type = (t instanceof Todo) ? "T" : (t instanceof Deadline) ? "D" : "E";
                sb.append(type).append(" | ").append(t.isDone() ? "1" : "0").append(" | ").append(t.getDescription());
                
                if (t instanceof Deadline) {
                    sb.append(" | ").append(((Deadline) t).getByAsString());
                } else if (t instanceof Event) {
                    Event e = (Event) t;
                    sb.append(" | ").append(e.getFromAsString()).append(" | ").append(e.getToAsString());
                }
                lines.add(sb.toString());
            }
            Files.write(path, lines);
        } catch (IOException e) {
            System.out.println("Error saving tasks: " + e.getMessage());
        }
    }
}
