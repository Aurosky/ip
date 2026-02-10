import java.util.Scanner;

class DukeException extends Exception {
    public DukeException(String message) {
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
        Task[] tasks = new Task[100];
        int taskCount = 0;
        String logo = " __        ___  _ _       \n"
                + " \\ \\      / (_) | | __ _ \n"
                + "  \\ \\ /\\ / /| | | |/ _` |\n"
                + "   \\ V  V / | | | | (_| |\n"
                + "    \\_/\\_/  |_|_|_|\\__,_|\n";
        System.out.println(LINE);
        System.out.println("     Hello from \n" + logo);
        System.out.println("     What can I do for you?");
        System.out.println(LINE);
        
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            String input = scanner.nextLine();
            System.out.println(LINE);
            try {
                if (input.equalsIgnoreCase("bye")) {
                    System.out.println("     Bye. Hope to see you again soon!");
                    System.out.println(LINE);
                    break;
                } else if (input.equalsIgnoreCase("list")) {
                    System.out.println("     Here are the tasks in your list:");
                    for (int i = 0; i < taskCount; i++) {
                        System.out.println("     " + (i + 1) + "." + tasks[i]);
                    }
                } else if (input.startsWith("mark ") || input.startsWith("unmark ")) {
                    handleMarkingInternal(input, tasks, taskCount);
                } else {
                    Task newTask = null;
                    if (input.startsWith("todo")) {
                        String detail = input.substring(4).trim();
                        if (detail.isEmpty()) {
                            throw new DukeException("The description of a todo cannot be empty.");
                        }
                        newTask = new Todo(detail);
                    } else if (input.startsWith("deadline")) {
                        String detail = input.substring(8).trim();
                        if (!detail.contains(" /by ")) {
                            throw new DukeException("Deadline format should be: deadline <desc> /by <time>");
                        }
                        String[] parts = detail.split(" /by ", 2);
                        if (parts.length < 2 || parts[0].isEmpty() || parts[1].isEmpty()) {
                            throw new DukeException("Missing description or /by time in deadline.");
                        }
                        newTask = new Deadline(parts[0], parts[1]);
                    } else if (input.startsWith("event")) {
                        String detail = input.substring(5).trim();
                        String[] parts = detail.split(" /from ", 2);
                        if (parts.length < 2 || parts[0].isEmpty() || !parts[1].contains(" /to ")) {
                            throw new DukeException("Event format should be: event <desc> /from <start> /to <end>");
                        }
                        String[] times = parts[1].split(" /to ", 2);
                        if (times.length < 2 || times[0].isEmpty() || times[1].isEmpty()) {
                            throw new DukeException("Missing start or end time for event.");
                        }
                        newTask = new Event(parts[0], times[0], times[1]);
                    } else {
                        throw new DukeException("I'm not sure what that means. Try a valid command.");
                    }
                    
                    tasks[taskCount++] = newTask;
                    System.out.println("     Got it. I've added this task:");
                    System.out.println("       " + newTask);
                    System.out.println("     Now you have " + taskCount + " tasks in the list.");
                }
            } catch (DukeException e) {
                System.out.println("     [OOPS] " + e.getMessage());
            } catch (Exception e) {
                System.out.println("     Something went wrong! " + e.getMessage());
            } finally {
                System.out.println(LINE);
            }
        }
        scanner.close();
    }
    
    private static void handleMarkingInternal(String input, Task[] tasks, int taskCount) throws DukeException {
        String[] parts = input.split(" ");
        if (parts.length != 2) {
            throw new DukeException("Please specify the task number correctly.");
        }
        
        int idx;
        try {
            idx = Integer.parseInt(parts[1]) - 1;
        } catch (NumberFormatException e) {
            throw new DukeException("Task number must be an integer.");
        }
        
        if (idx < 0 || idx >= taskCount) {
            throw new DukeException("Task number is out of range.");
        }
        
        if (input.startsWith("mark ")) {
            tasks[idx].markAsDone();
            System.out.println("     Nice! I've marked this task as done:");
        } else {
            tasks[idx].markAsNotDone();
            System.out.println("     OK, I've marked this task as not done yet:");
        }
        System.out.println("       " + tasks[idx]);
    }
}
