import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import java.util.Map;
import java.util.LinkedHashMap;

/**
 * Self-defined exception, used to handle business exceptions within the Willa task management program.
 * Inherits from Exception and carries specific error message information.
 */
class WillaException extends Exception {
    /**
     * Constructs an instance of the WillaException exception.
     * @param message Detailed exception message informing the user of the specific error cause
     */
    public WillaException(String message) {
        super(message);
    }
}

/**
 * Base class for all task types, defining common properties and behaviors.
 * Includes task descriptions, completion status, and common methods.
 */
class Task {
    /** task description */
    protected String description;
    /** Task completion status: true indicates completed, false indicates incomplete */
    protected boolean isDone;
    
    /**
     * Constructs a Task instance, initializes the task description, sets the completion status to incomplete.
     * @param description The specific description of the task
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }
    
    /**
     * Retrieves the status icon for a task to display in the console.
     * @return “X” indicates completed, space indicates incomplete
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }
    
    /**
     * Marks the task as completed.
     */
    public void markAsDone() {
        this.isDone = true;
    }
    /**
     * Marks the task as incompleted.
     */
    public void markAsNotDone() {
        this.isDone = false;
    }
    
    /**
     * Override the toString method to return the task's string representation (including status icon and description).
     * @return A formatted task string in the format “[status icon] description”.
     */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}

/**
 * Task class, inheriting from the Task base class, a basic task with no time constraints.
 */
class Todo extends Task {
    /**
     * Constructs a Todo task instance.
     * @param description The specific description of the task
     */
    public Todo(String description) {
        super(description);
    }
    
    /**
     * Override the toString method to add the Todo task type identifier [T].
     * @return A formatted Todo task string in the format “[T][status icon] description”.
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}

/**
 * Deadline task class, inheriting from the Task base class, representing tasks with a deadline.
 * Extends the deadline property of type LocalDateTime and provides date parsing/formatting methods.
 */
class Deadline extends Task {
    /** Task deadline (including date and time) */
    protected LocalDateTime by;
    
    /** Date parser and formatter supporting yyyy-MM-dd or yyyy-MM-dd HHmm formats */
    private static final DateTimeFormatter PARSE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd[ HHmm]");
    
    /** Date display formatter, format: MMM dd yyyy hh:mm a (e.g., Oct 02 2019 06:00 PM) */
    private static final DateTimeFormatter DISPLAY_FORMATTER = DateTimeFormatter.ofPattern("MMM dd yyyy hh:mm a");
    
    /**
     * Constructs an instance of a Deadline task.
     * @param description The specific description of the deadline task
     * @param by The deadline time of the task (LocalDateTime type)
     */
    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }
    
    /**
     * Parses a string-formatted deadline into a LocalDateTime object for use when loading data into Storage.
     * @param byStr String-formatted deadline, supporting yyyy-MM-dd or yyyy-MM-dd HHmm formats
     * @return The parsed LocalDateTime object
     * @throws WillaException Throws an exception when the input date format is invalid
     */
    public static LocalDateTime parseBy(String byStr) throws WillaException {
        try {
            return LocalDateTime.parse(byStr, PARSE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new WillaException("Invalid date format! Please use yyyy-MM-dd or yyyy-MM-dd HHmm (e.g., 2019-12-02 or 2019-12-02 1800)");
        }
    }
    
    /**
     * Converts a LocalDateTime-type deadline into a standard string for use when storing data in Storage.
     * @return A standard-formatted deadline string (yyyy-MM-dd or yyyy-MM-dd HHmm)
     */
    public String getByAsString() {
        return by.format(PARSE_FORMATTER);
    }
    
    /**
     * Override the toString method to add the Deadline task's type identifier [D] and a formatted deadline.
     * @return A formatted Deadline task string in the format “[D][status icon] description (by: formatted time)”
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by.format(DISPLAY_FORMATTER) + ")";
    }
}

/**
 * Event task class, inheriting from the Task base class, representing event-based tasks with start and end times.
 */
class Event extends Task {
    /** Start time of the event (string format) */
    protected String from;
    
    /** End time of the event (string format) */
    protected String to;
    
    /**
     * Constructs an Event task instance.
     * @param description The specific description of the event task
     * @param from The start time of the event
     * @param to The end time of the event
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }
    
    /**
     * Override the toString method to include the Event task's type identifier [E] and start/end times.
     * @return A formatted Event task string in the format “[E][status icon] description (from: start time to: end time)”
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}

/**
 * Interface interaction class responsible for handling user input and program output, providing a unified console interaction style.
 */
class Ui {
    /** A separator line for console output, used to distinguish different interaction content */
    private static final String LINE = "    ____________________________________________________________";
    /** Scanner instance used to read user input */
    private Scanner scanner;
    
    /**
     * Constructs a Ui instance and initializes a Scanner to read user input.
     */
    public Ui() { this.scanner = new Scanner(System.in); }
    
    /**
     * Reads the command string entered by the user from the console.
     * @return The complete command entered by the user
     */
    public String readCommand() { return scanner.nextLine(); }
    
    /**
     * Prints a separator line in the console to enhance interactive visual effects.
     */
    public void showLine() { System.out.println(LINE); }
    
    /**
     * Displays the welcome screen when the program starts, featuring the logo and welcome message.
     */
    public void showWelcome() {
        String logo = " __        ___  _ _\n"
                + " \\ \\      / (_) | | __ _\n"
                + "  \\ \\ /\\ / /| | | |/ _` |\n"
                + "   \\ V  V / | | | | (_| |\n"
                + "    \\_/\\_/  |_|_|_|\\__,_|\n";
        System.out.println(LINE + "\n     Hello from\n" + logo + "\n     What can I do for you?");
    }
    
    /**
     * Displays error messages in the console, standardizing the error output format.
     * @param message The specific content of the error message
     */
    public void showError(String message) {
        System.out.println("     [OOPS] " + message);
    }
    
    /**
     * Displays general prompt messages in the console, standardizing the output format.
     * @param message The specific content of the general prompt
     */
    public void showMessage(String message) {
        System.out.println("     " + message);
    }
}

/**
 * Data persistence class responsible for loading task lists from files and saving task lists to files.
 * Supports serialization and deserialization of three task types: Todo, Deadline, and Event.
 */
class Storage {
    /** Storage path for task data files */
    private String filePath;
    
    /**
     * Constructs a Storage instance, specifying the storage path for data files.
     * @param filePath The path to the task data file (e.g., ./data/willa.txt)
     */
    public Storage(String filePath) { this.filePath = filePath; }
    
    /**
     * Loads a task list from a file at the specified path, parses the file content, and converts it into a list of Task objects.
     * @return The loaded list of Task objects. Returns an empty list if the file does not exist.
     * @throws WillaException Throws an exception if file loading fails or content parsing errors occur.
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
                if (p[0].equals("T")) {
                    t = new Todo(p[2]);
                } else if (p[0].equals("D")) {
                    // when load Deadline, decode date string to LocalDateTime
                    LocalDateTime by = Deadline.parseBy(p[3]);
                    t = new Deadline(p[2], by);
                } else {
                    t = new Event(p[2], p[3], p[4]);
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
     * Saves the current task list to a file at the specified path, serializing Task objects into text format.
     * @param tasks The list of Task objects to save
     */
    public void save(List<Task> tasks) {
        try {
            Path path = Paths.get(filePath);
            Files.createDirectories(path.getParent());
            List<String> lines = new ArrayList<>();
            for (Task t : tasks) {
                String type = (t instanceof Todo) ? "T" : (t instanceof Deadline) ? "D" : "E";
                String done = t.isDone ? "1" : "0";
                String base = type + " | " + done + " | " + t.description;
                if (t instanceof Deadline) {
                    // store Deadline, store standard format string
                    base += " | " + ((Deadline) t).getByAsString();
                } else if (t instanceof Event) {
                    base += " | " + ((Event) t).from + " | " + ((Event) t).to;
                }
                lines.add(base);
            }
            Files.write(path, lines);
        } catch (IOException e) {
            System.out.println("     [Error] Failed to save tasks: " + e.getMessage());
        }
    }
}

/**
 * Command parsing class responsible for parsing user-input command strings and executing corresponding business logic.
 * Supports commands including list, mark, unmark, delete, find, todo, deadline, and event.
 */
class Parser {
    /**
     * Parses user-input commands and executes corresponding actions.
     * @param input Raw command string entered by the user
     * @param tasks Task list instance for manipulating task data
     * @param ui UI interaction instance for outputting results
     * @param storage Data persistence instance for subsequent data saving (This method does not save directly)
     * @throws WillaException Throws an exception when the command format is incorrect or execution fails
     */
    public static void parseAndExecute(String input, TaskList tasks, Ui ui, Storage storage) throws WillaException {
        if (input.equalsIgnoreCase("list")) {
            ui.showMessage("Here are the tasks in your list:");
            for (int i = 0; i < tasks.getSize(); i++) {
                ui.showMessage((i + 1) + "." + tasks.getAllTasks().get(i));
            }
        } else if (input.startsWith("mark ") || input.startsWith("unmark ")) {
            boolean isMark = input.startsWith("mark ");
            int idx = Integer.parseInt(input.split(" ")[1]) - 1;
            Task t = tasks.markTask(idx, isMark);
            ui.showMessage(isMark ? "Nice! I've marked this task as done:" : "OK, I've marked this task as not done yet:");
            ui.showMessage("  " + t);
        } else if (input.startsWith("delete ")) {
            int idx = Integer.parseInt(input.substring(7).trim()) - 1;
            Task removed = tasks.deleteTask(idx);
            ui.showMessage("Noted. I've removed this task:\n    " + removed);
            ui.showMessage("Now you have " + tasks.getSize() + " tasks in the list.");
        } else if (input.startsWith("find ")) { // add find
            String keyword = input.substring(5).trim();
            if (keyword.isEmpty()) {
                throw new WillaException("Search keyword cannot be empty! Use: find <keyword>");
            }
            Map<Integer, Task> matchingTasks = tasks.findTasksByKeyword(keyword);
            ui.showMessage("Here are the matching tasks in your list:");
            if (matchingTasks.isEmpty()) {
                ui.showMessage("  No matching tasks found.");
            } else {
                // show original index
                for (Map.Entry<Integer, Task> entry : matchingTasks.entrySet()) {
                    int originalIndex = entry.getKey();
                    Task task = entry.getValue();
                    ui.showMessage("  " + originalIndex + "." + task);
                }
            }
        } else {
            // add logic
            Task newTask = handleAdding(input);
            tasks.addTask(newTask);
            ui.showMessage("Got it. I've added:\n    " + newTask + "\n     Now you have " + tasks.getSize() + " tasks in the list.");
        }
    }
    
    /**
     * Handles commands for adding new tasks (todo/deadline/event), parses command parameters, and creates corresponding Task instances.
     * @param input User-input command string for adding a new task
     * @return Instance of the created Task subclass (Todo/Deadline/Event)
     * @throws WillaException Throws an exception if the command format is incorrect or parameters are missing
     */
    private static Task handleAdding(String input) throws WillaException {
        if (input.startsWith("todo")) {
            String detail = input.substring(4).trim();
            if (detail.isEmpty()) throw new WillaException("Todo description cannot be empty.");
            return new Todo(detail);
        } else if (input.startsWith("deadline")) {
            String[] parts = input.substring(8).split(" /by ", 2);
            if (parts.length < 2) throw new WillaException("Use: deadline <desc> /by <time> (format: yyyy-MM-dd or yyyy-MM-dd HHmm)");
            String desc = parts[0].trim();
            String byStr = parts[1].trim();
            if (desc.isEmpty()) throw new WillaException("Deadline description cannot be empty.");
            // decode to LocalDateTime
            LocalDateTime by = Deadline.parseBy(byStr);
            return new Deadline(desc, by);
        } else if (input.startsWith("event")) {
            String[] parts = input.substring(5).split(" /from | /to ", 3);
            if (parts.length < 3) throw new WillaException("Use: event <desc> /from <start> /to <end>");
            return new Event(parts[0].trim(), parts[1].trim(), parts[2].trim());
        }
        throw new WillaException("I'm not sure what that means. Supported commands: todo, deadline, event, list, mark, unmark, delete, bye");
    }
}

/**
 * Task list management class responsible for maintaining the collection of all Task objects and providing core operations such as add, delete, update, and query.
 */
class TaskList {
    /** List storing all Task objects */
    private List<Task> tasks;
    
    /**
     * Constructs an empty TaskList instance, initializing an empty task list.
     */
    public TaskList() { this.tasks = new ArrayList<>(); }
    
    /**
     * Constructs a TaskList instance, initializing it with an existing list of Tasks.
     * @param tasks An existing list of Task objects
     */
    public TaskList(List<Task> tasks) { this.tasks = tasks; }
    
    /**
     * Retrieves a list of all tasks (read-only; externally traversable but direct modification is not recommended).
     * @return A List instance containing all Task objects.
     */
    public List<Task> getAllTasks() { return tasks; }
    
    /**
     * Retrieves the number of tasks in the current task list.
     * @return The total number of tasks
     */
    public int getSize() { return tasks.size(); }
    
    /**
     * Adds a new task to the task list.
     * @param task The Task instance to be added
     * @return The added Task instance (for chained calls)
     */
    public Task addTask(Task task) {
        tasks.add(task);
        return task;
    }
    
    /**
     * Deletes the task at the specified index from the task list.
     * @param index The index of the task in the list (zero-based)
     * @return The deleted Task instance
     * @throws WillaException Throws an exception if the index is out of bounds
     */
    public Task deleteTask(int index) throws WillaException {
        if (index < 0 || index >= tasks.size()) throw new WillaException("Task number out of range.");
        return tasks.remove(index);
    }
    
    /**
     * Marks the task at the specified index as completed or uncompleted.
     * @param index The index of the task in the list (starting from 0)
     * @param isDone true to mark as completed, false to mark as uncompleted
     * @return The marked Task instance
     * @throws WillaException Throws an exception if the index is out of bounds
     */
    public Task markTask(int index, boolean isDone) throws WillaException {
        if (index < 0 || index >= tasks.size()) throw new WillaException("Task number out of range.");
        Task t = tasks.get(index);
        if (isDone) t.markAsDone(); else t.markAsNotDone();
        return t;
    }
    
    /**
     * Searches for tasks based on a keyword and returns a mapped list of matching tasks with their original sequence numbers.
     * The search is case-insensitive and matches tasks whose descriptions contain the keyword.
     * @param keyword The search keyword
     * @return A LinkedHashMap where the Key is the task's original sequence number in the list (starting from 1), and the Value is the matching Task instance.
     */
    public Map<Integer, Task> findTasksByKeyword(String keyword) {
        Map<Integer, Task> matchingTasks = new LinkedHashMap<>();
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            // check if has key word
            if (task.description.toLowerCase().contains(keyword.toLowerCase())) {
                matchingTasks.put(i + 1, task); // same index in list
            }
        }
        return matchingTasks;
    }
}

/**
 * The main class of the Willa task management program, responsible for initializing core components and starting the main program loop.
 * Integrates components such as Storage, TaskList, Ui, and Parser to provide complete task management functionality.
 */
public class Willa {
    /** Data Persistence Component Instance */
    private Storage storage;
    
    /** Task List Management Component Instance */
    private TaskList tasks;
    
    /** UI Interaction Component Instance */
    private Ui ui;
    
    /**
     * Constructs a Willa program instance, initializes core components, and loads historical tasks.
     * @param filePath The storage path for task data files
     */
    public Willa(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (WillaException e) {
            ui.showError("Loading error. Starting with empty list.");
            tasks = new TaskList();
        }
    }
    
    /**
     * The main loop that starts the Willa program, handling user input until the bye command is entered to exit.
     */
    public void run() {
        ui.showWelcome();
        ui.showLine();
        boolean isExit = false;
        while (!isExit) {
            String fullCommand = ui.readCommand();
            ui.showLine();
            if (fullCommand.equalsIgnoreCase("bye")) {
                ui.showMessage("Bye. Hope to see you again soon!");
                isExit = true;
            } else {
                try {
                    Parser.parseAndExecute(fullCommand, tasks, ui, storage);
                    storage.save(tasks.getAllTasks());
                } catch (Exception e) {
                    ui.showError(e.getMessage());
                }
            }
            ui.showLine();
        }
    }
    
    /**
     * Entry point method for the program. Creates a Willa instance and starts the program.
     * @param args Command-line arguments (not used)
     */
    public static void main(String[] args) {
        new Willa("./data/willa.txt").run();
    }
}