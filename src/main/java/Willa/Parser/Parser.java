package Willa.Parser;

import Willa.Command.*;
import Willa.Exception.WillaException;

public class Parser {
    
    public static Command parse(String input) throws WillaException {
        String[] parts = input.trim().split(" ", 2);
        String commandWord = parts[0].toLowerCase();
        String arguments = parts.length > 1 ? parts[1].trim() : "";
        
        return switch (commandWord) {
            case "bye"      -> new byeCommand();
            case "list"     -> new listCommand();
            case "mark"     -> markCommand.of(arguments, true);
            case "unmark"   -> markCommand.of(arguments, false);
            case "delete"   -> deleteCommand.of(arguments);
            case "find"     -> new findCommand(arguments);
            case "todo"     -> todoCommand.of(arguments);
            case "deadline" -> deadlineCommand.of(arguments);
            case "event"    -> eventCommand.of(arguments);
            default         -> throw new WillaException("Unknown command: " + commandWord +
                    "I'm not sure what that means. Supported commands: todo, deadline, event, list, mark, unmark, delete, bye");

        };
    }
}
