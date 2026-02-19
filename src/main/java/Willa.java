import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
    protected String by;
    
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }
    
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
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

public class Willa {
    private static final String LINE = "    ____________________________________________________________";
    private static final String FILE_PATH = "./data/willa.txt";
    
    public static void main(String[] args) {
        ArrayList<Task> tasks = new ArrayList<>();
        loadTasks(tasks);
        
        String logo = " __        ___  _ _\n"
                + " \\ \\      / (_) | | __ _\n"
                + "  \\ \\ /\\ / /| | | |/ _` |\n"
                + "   \\ V  V / | | | | (_| |\n"
                + "    \\_/\\_/  |_|_|_|\\__,_|\n";
        System.out.println(LINE + "\n     Hello from\n" + logo + "\n     What can I do for you?\n" + LINE);
        
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();
            try {
                if (input.equalsIgnoreCase("bye")) {
                    System.out.println("     Bye. Hope to see you again soon!");
                    break;
                } else if (input.equalsIgnoreCase("list")) {
                    printList(tasks);
                } else if (input.startsWith("mark ") || input.startsWith("unmark ")) {
                    handleMarkingInternal(input, tasks);
                } else {
                    handleAdding(input, tasks);
                }
                saveTasks(tasks);
            } catch (WillaException e) {
                System.out.println("     [OOPS] " + e.getMessage());
            } catch (Exception e) {
                System.out.println("     Something went wrong! " + e.getMessage());
            } finally {
                System.out.println(LINE);
            }
        }
        scanner.close();
    }
    
    private static void printList(ArrayList<Task> tasks){
        System.out.println("     Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++)
            System.out.println("     " + (i + 1) + "." + tasks.get(i));
    }
    
    private static void handleAdding(String input, ArrayList<Task> tasks) throws WillaException {
        Task newTask;
        if (input.startsWith("todo")) {
            String detail = input.substring(4).trim();
            if (detail.isEmpty())
                throw new WillaException("The description of a todo cannot be empty.");
            newTask = new Todo(detail);
        } else if (input.startsWith("deadline")) {
            String detail = input.substring(8).trim();
            if (!detail.contains(" /by "))
                throw new WillaException("Deadline format should be: deadline <desc> /by <time>");
            String[] parts = detail.split(" /by ", 2);
            if (parts.length < 2 || parts[0].isEmpty() || parts[1].isEmpty())
                throw new WillaException("Missing description or /by time in deadline.");
            newTask = new Deadline(parts[0], parts[1]);
        } else if (input.startsWith("event")) {
            String[] parts = input.substring(5).split(" /from | /to ", 3);
            if (parts.length < 3)
                throw new WillaException("Format: event <desc> /from <start> /to <end>");
            newTask = new Event(parts[0].trim(), parts[1].trim(), parts[2].trim());
        } else {
            throw new WillaException("I'm not sure what that means. Try a valid command.");
        }
        
        tasks.add(newTask);
        System.out.println("     Got it. I've added:\n       " + newTask + "\n     Now: " + tasks.size() + " tasks.");
    }
    
    private static void handleMarkingInternal(String input, ArrayList<Task> tasks) throws WillaException {
        String[] parts = input.split(" ");
        if (parts.length != 2) {
            throw new WillaException("Please specify the task number correctly.");
        }
        
        int idx = Integer.parseInt(parts[1]) - 1;
        if (idx < 0 || idx >= tasks.size()) throw new WillaException("Task number is out of range.");
        
        Task t = tasks.get(idx);
        if (input.startsWith("mark ")) {
            t.markAsDone();
            System.out.println("     Nice! I've marked this task as done:");
        } else {
            t.markAsNotDone();
            System.out.println("     OK, I've marked this task as not done yet:");
        }
        System.out.println("       " + t);
    }
    
    private static void saveTasks(ArrayList<Task> tasks) {
        try {
            Path path = Paths.get(FILE_PATH);
            Files.createDirectories(path.getParent()); // 自动创建 data 文件夹
            List<String> lines = new ArrayList<>();
            for (Task t : tasks) {
                String type = (t instanceof Todo) ? "T" : (t instanceof Deadline) ? "D" : "E";
                String done = t.getStatusIcon().equals("X") ? "1" : "0";
                String base = type + " | " + done + " | " + t.description;
                if (t instanceof Deadline) base += " | " + ((Deadline) t).by;
                else if (t instanceof Event) base += " | " + ((Event) t).from + " | " + ((Event) t).to;
                lines.add(base);
            }
            Files.write(path, lines);
        } catch (IOException e) {
            System.out.println("     [Error] Failed to save tasks.");
        }
    }
    
    private static void loadTasks(ArrayList<Task> tasks) {
        Path path = Paths.get(FILE_PATH);
        if (!Files.exists(path)) return;
        try {
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                String[] p = line.split(" \\| ");
                Task t = p[0].equals("T") ? new Todo(p[2]) :
                        p[0].equals("D") ? new Deadline(p[2], p[3]) : new Event(p[2], p[3], p[4]);
                if (p[1].equals("1")) t.markAsDone();
                tasks.add(t);
            }
        } catch (Exception e) {
            System.out.println("     [Notice] Data file initialization or corruption handled.");
        }
    }
}
