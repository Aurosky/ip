import java.util.Scanner;

class Task3 {
    protected String description;
    protected boolean isDone;
    public Task3(String description) {
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

public class WillaLev3 {
    public static void main(String[] args) {
        String line = "    ____________________________________________________________";
        Task[] tasks = new Task[100];
        int taskCount = 0;

        System.out.println(line);
        System.out.println("     Hello! I'm Willa");
        System.out.println("     What can I do for you?");
        System.out.println(line);

        Scanner scanner = new Scanner(System.in);

        while (true) {
            String input = scanner.nextLine();
            System.out.println(line);
            if (input.equalsIgnoreCase("bye")) {
                System.out.println("     Bye. Hope to see you again soon!");
                break;
            } else if (input.equalsIgnoreCase("list")) {
                System.out.println("     Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println("     " + (i + 1) + "." + tasks[i]);
                }
            } else if (input.startsWith("mark ")) {
                int idx = Integer.parseInt(input.substring(5).trim()) - 1;
                tasks[idx].markAsDone();
                System.out.println("     Nice! I've marked this task as done:");
                System.out.println("       " + tasks[idx]);
            } else if (input.startsWith("unmark ")) {
                int idx = Integer.parseInt(input.substring(7).trim()) - 1;
                tasks[idx].markAsNotDone();
                System.out.println("     OK, I've marked this task as not done yet:");
                System.out.println("       " + tasks[idx]);
            } else {
                tasks[taskCount] = new Task(input);
                taskCount++;
                System.out.println("     added: " + input);
            }
            System.out.println(line);
        }
        scanner.close();
    }
}
