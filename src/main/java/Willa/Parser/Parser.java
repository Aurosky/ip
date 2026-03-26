package Willa.Parser;

import Willa.Command.*;
import Willa.Exception.WillaException;

/**
 * Handles the parsing of raw user input into executable Command objects.
 * This class acts as a dispatcher, identifying the command type and delegating
 * the actual argument processing to the respective Command classes.
 */
public class Parser {
    
    /**
     * Translates a raw input string into a specific Command instance.
     * * @param input The full line of text entered by the user.
     * @return A Command object corresponding to the user's intent.
     * @throws WillaException If the command word is unrecognized or the arguments
     * fail initial validation by the command factory.
     */
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
                    ". I'm not sure what that means. Supported commands: todo, deadline, event, list, mark, unmark, delete, find, bye");
        };
    }
}
