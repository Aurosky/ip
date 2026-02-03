import java.util.Scanner;

class Task {
    protected String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
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

public class Willa {
    public static void main(String[] args) {
        String line = "    ____________________________________________________________";
        Task[] tasks = new Task[100];
        int taskCount = 0;

        System.out.println(line + "\n     Hello! I'm Willa.\n     What can I do for you?\n" + line);
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("bye")) {
                System.out.println(line + "\n     Bye. Hope to see you again soon!\n" + line);
                break;
            }else if (input.equalsIgnoreCase("list")) {
                System.out.println(line + "\n     Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println("     " + (i + 1) + "." + tasks[i]);
                }
                System.out.println(line);
            } else if (input.startsWith("mark ")) {
                // Extract number after "mark "
                int idx = Integer.parseInt(input.substring(5)) - 1;
                tasks[idx].markAsDone();
                System.out.println(line + "\n     Nice! I've marked this task as done:\n       " + tasks[idx] + "\n" + line);
            } else if (input.startsWith("unmark ")) {
                int idx = Integer.parseInt(input.substring(7)) - 1;
                tasks[idx].markAsNotDone();
                System.out.println(line + "\n     OK, I've marked this task as not done yet:\n       " + tasks[idx] + "\n" + line);
            } else {
                tasks[taskCount] = new Task(input);
                taskCount++;
                System.out.println(line + "\n     added: " + input + "\n" + line);
            }
        }
        scanner.close();
    }
}