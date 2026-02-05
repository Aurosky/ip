import java.util.Scanner;

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

    public void markAsDone() { this.isDone = true; }
    public void markAsNotDone() { this.isDone = false; }

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}

class Todo extends Task {
    public Todo(String description) { super(description); }
    @Override
    public String toString() { return "[T]" + super.toString(); }
}

class Deadline extends Task {
    protected String by;
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }
    @Override
    public String toString() { return "[D]" + super.toString() + " (by: " + by + ")"; }
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
    public String toString() { return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")"; }
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

            if (input.equalsIgnoreCase("bye")) {
                System.out.println(LINE);
                System.out.println("     Bye. Hope to see you again soon!");
                System.out.println(LINE);
                break;
            }
            
            System.out.println(LINE);

            if (input.equalsIgnoreCase("list")) {
                System.out.println("     Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println("     " + (i + 1) + "." + tasks[i]);
                }
            } else if (input.startsWith("mark ") || input.startsWith("unmark ")) {
                handleMarkingInternal(input, tasks);
            } else {
                Task newTask = null;
                if (input.startsWith("todo ")) {
                    newTask = new Todo(input.substring(5));
                } else if (input.startsWith("deadline ")) {
                    String[] parts = input.substring(9).split(" /by ");
                    newTask = new Deadline(parts[0], parts[1]);
                } else if (input.startsWith("event ")) {
                    String[] parts = input.substring(6).split(" /from | /to ");
                    newTask = new Event(parts[0], parts[1], parts[2]);
                }

                if (newTask != null) {
                    tasks[taskCount++] = newTask;
                    System.out.println("     Got it. I've added this task:");
                    System.out.println("       " + newTask);
                    System.out.println("     Now you have " + taskCount + " tasks in the list.");
                } else {
                    System.out.println("     " + input);
                }
            }
            
            System.out.println(LINE);
        }
        scanner.close();
    }

    private static void handleMarkingInternal(String input, Task[] tasks) {
        String[] parts = input.split(" ");
        int idx = Integer.parseInt(parts[1]) - 1;
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
