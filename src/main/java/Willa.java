import java.util.Scanner;
import java.util.ArrayList;

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
    
    public static void main(String[] args) {
        ArrayList<Task> tasks = new ArrayList<>();
        
        String logo = " __        ___  _ _\n"
                + " \\ \\      / (_) | | __ _\n"
                + "  \\ \\ /\\ / /| | | |/ _` |\n"
                + "   \\ V  V / | | | | (_| |\n"
                + "    \\_/\\_/  |_|_|_|\\__,_|\n";
        System.out.println(LINE);
        System.out.println("     Hello from\n" + logo);
        System.out.println("     What can I do for you?");
        System.out.println(LINE);
        
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            String input = scanner.nextLine();
            try {
                if (input.equalsIgnoreCase("bye")) {
                    System.out.println("     Bye. Hope to see you again soon!");
                    break;
                } else if (input.equalsIgnoreCase("list")) {
                    System.out.println("     Here are the tasks in your list:");
                    for (int i = 0; i < tasks.size(); i++) {
                        System.out.println("     " + (i + 1) + "." + tasks.get(i));
                    }
                } else if (input.startsWith("mark ") || input.startsWith("unmark ")) {
                    handleMarkingInternal(input, tasks);
                }else if (input.startsWith("delete ")) { // 记得在这里调用删除
                    handleDelete(input, tasks);
                }else {
                    Task newTask = null;
                    if (input.startsWith("todo")) {
                        String detail = input.substring(4).trim();
                        if (detail.isEmpty()) {
                            throw new WillaException("The description of a todo cannot be empty.");
                        }
                        newTask = new Todo(detail);
                    } else if (input.startsWith("deadline")) {
                        String detail = input.substring(8).trim();
                        if (!detail.contains(" /by ")) {
                            throw new WillaException("Deadline format should be: deadline <desc> /by <time>");
                        }
                        String[] parts = detail.split(" /by ", 2);
                        if (parts.length < 2 || parts[0].isEmpty() || parts[1].isEmpty()) {
                            throw new WillaException("Missing description or /by time in deadline.");
                        }
                        newTask = new Deadline(parts[0], parts[1]);
                    } else if (input.startsWith("event")) {
                        String detail = input.substring(5).trim();
                        String[] parts = detail.split(" /from ", 2);
                        if (parts.length < 2 || parts[0].isEmpty() || !parts[1].contains(" /to ")) {
                            throw new WillaException("Event format should be: event <desc> /from <start> /to <end>");
                        }
                        String[] times = parts[1].split(" /to ", 2);
                        if (times.length < 2 || times[0].isEmpty() || times[1].isEmpty()) {
                            throw new WillaException("Missing start or end time for event.");
                        }
                        newTask = new Event(parts[0], times[0], times[1]);
                    } else {
                        throw new WillaException("I'm not sure what that means. Try a valid command.");
                    }
                    
                    tasks.add(newTask);
                    System.out.println("     Got it. I've added this task:");
                    System.out.println("       " + newTask);
                    System.out.println("     Now you have " + tasks.size() + " tasks in the list.");
                }
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
    
    private static void handleDelete(String input, ArrayList<Task> tasks) throws WillaException {
        try {
            int idx = Integer.parseInt(input.substring(7).trim()) - 1;
            if (idx < 0 || idx >= tasks.size()) throw new WillaException("Task number is out of range.");
            
            Task removedTask = tasks.remove(idx); // 直接从 list 中移除
            System.out.println("     Noted. I've removed this task:");
            System.out.println("       " + removedTask);
            System.out.println("     Now you have " + tasks.size() + " tasks in the list.");
        } catch (NumberFormatException e) {
            throw new WillaException("Please provide a valid task number to delete.");
        }
    }
    
    private static void handleMarkingInternal(String input, ArrayList<Task> tasks) throws WillaException {
        String[] parts = input.split(" ");
        if (parts.length != 2) throw new WillaException("Specify the task number.");
        
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
}