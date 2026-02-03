import java.util.Scanner;

public class WillaLev2 {
    public static void main(String[] args) {
        String line = "    ____________________________________________________________";
        String[] tasks = new String[100];
        int itemCount = 0;
        System.out.println(line);
        System.out.println("     Hello! I'm Willa");
        System.out.println("     What can I do for you?");
        System.out.println(line);
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();
            if (input.equals("bye")) {
                System.out.println(line);
                System.out.println("     Bye. Hope to see you again soon!");
                System.out.println(line);
                break;
            } else if (input.equals("list")) {
                System.out.println(line);
                for (int i = 0; i < itemCount; i++) {
                    System.out.println("     " + (i + 1) + ". " + tasks[i]);
                }
                System.out.println(line);
            } else {
                tasks[itemCount] = input;
                itemCount++;
                System.out.println(line);
                System.out.println("     added: " + input);
                System.out.println(line);
            }
        }
        scanner.close();
    }
}