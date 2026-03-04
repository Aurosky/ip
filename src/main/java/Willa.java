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

class WillaException extends Exception {
    public WillaException(String message) {
        super(message);
    }
}

class Task {
    protected String description;
    protected boolean isDone;
    
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }
    
    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }
    
    public void markAsDone() {
        this.isDone = true;
    }
    
    public void markAsNotDone() {
        this.isDone = false;
    }
    
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}

class Todo extends Task {
    public Todo(String description) {
        super(description);
    }
    
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}

class Deadline extends Task {
    // String2LocalDateTime, support time
    protected LocalDateTime by;
    // decode:（yyyy-MM-dd 或 yyyy-MM-dd HHmm), show(MMM dd yyyy)
    private static final DateTimeFormatter PARSE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd[ HHmm]");
    private static final DateTimeFormatter DISPLAY_FORMATTER = DateTimeFormatter.ofPattern("MMM dd yyyy hh:mm a");
    
    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }
    
    // for load Storage: from standard to LocalDateTime format
    public static LocalDateTime parseBy(String byStr) throws WillaException {
        try {
            return LocalDateTime.parse(byStr, PARSE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new WillaException("Invalid date format! Please use yyyy-MM-dd or yyyy-MM-dd HHmm (e.g., 2019-12-02 or 2019-12-02 1800)");
        }
    }
    
    // for Storage: get standard
    public String getByAsString() {
        return by.format(PARSE_FORMATTER);
    }
    
    @Override
    public String toString() {
        // show certain format (Oct 02 2019 06:00 PM)
        return "[D]" + super.toString() + " (by: " + by.format(DISPLAY_FORMATTER) + ")";
    }
}

class Event extends Task {
    protected String from;
    protected String to;
    
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }
    
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}

// UI
class Ui {
    private static final String LINE = "    ____________________________________________________________";
    private Scanner scanner;
    
    public Ui() { this.scanner = new Scanner(System.in); }
    
    public String readCommand() { return scanner.nextLine(); }
    
    public void showLine() { System.out.println(LINE); }
    
    public void showWelcome() {
        String logo = " __        ___  _ _\n"
                + " \\ \\      / (_) | | __ _\n"
                + "  \\ \\ /\\ / /| | | |/ _` |\n"
                + "   \\ V  V / | | | | (_| |\n"
                + "    \\_/\\_/  |_|_|_|\\__,_|\n";
        System.out.println(LINE + "\n     Hello from\n" + logo + "\n     What can I do for you?");
    }
    
    public void showError(String message) {
        System.out.println("     [OOPS] " + message);
    }
    
    public void showMessage(String message) {
        System.out.println("     " + message);
    }
}

// Storage
class Storage {
    private String filePath;
    
    public Storage(String filePath) { this.filePath = filePath; }
    
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

// Parser
class Parser {
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
        } else {
            // add logic
            Task newTask = handleAdding(input);
            tasks.addTask(newTask);
            ui.showMessage("Got it. I've added:\n    " + newTask + "\n     Now you have " + tasks.getSize() + " tasks in the list.");
        }
    }
    
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

// TaskList
class TaskList {
    private List<Task> tasks;
    
    public TaskList() { this.tasks = new ArrayList<>(); }
    public TaskList(List<Task> tasks) { this.tasks = tasks; }
    
    public List<Task> getAllTasks() { return tasks; }
    public int getSize() { return tasks.size(); }
    
    public Task addTask(Task task) {
        tasks.add(task);
        return task;
    }
    
    public Task deleteTask(int index) throws WillaException {
        if (index < 0 || index >= tasks.size()) throw new WillaException("Task number out of range.");
        return tasks.remove(index);
    }
    
    public Task markTask(int index, boolean isDone) throws WillaException {
        if (index < 0 || index >= tasks.size()) throw new WillaException("Task number out of range.");
        Task t = tasks.get(index);
        if (isDone) t.markAsDone(); else t.markAsNotDone();
        return t;
    }
}

// Willa
public class Willa {
    private Storage storage;
    private TaskList tasks;
    private Ui ui;
    
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
    
    public static void main(String[] args) {
        new Willa("./data/willa.txt").run();
    }
}